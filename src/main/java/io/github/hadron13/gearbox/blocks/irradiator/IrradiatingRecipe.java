package io.github.hadron13.gearbox.blocks.irradiator;


import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import io.github.hadron13.gearbox.mixin.accessor.BasinBlockEntityAccessor;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.data.Iterate;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class IrradiatingRecipe extends ProcessingRecipe<RecipeInput, LaserRecipeParams> implements LaserRecipe{
    public int requiredColor;
    public float requiredPower;

    public IrradiatingRecipe(LaserRecipeParams params) {
        super(GearboxRecipeTypes.IRRADIATING, params);
        requiredPower = params.power;
        requiredColor = params.color;
    }

    public static boolean match(IrradiatorBlockEntity be, IrradiatingRecipe recipe){
        if(recipe == null)
            return false;

        if(be.receivingLaser == null)
            return false;

        if(Mth.abs(be.getSpeed()) < 1f)
            return false;

        if(!LaserRecipe.matchLaser(recipe, be.receivingLaser))
            return false;

        return LaserRecipe.matchLaser(recipe, be.receivingLaser);
    }


    public static boolean matchBasin(BasinBlockEntity basin, Recipe<?> recipe) {
        FilteringBehaviour filter = basin.getFilter();
        if (filter == null)
            return false;

        boolean filterTest = filter.test(recipe.getResultItem(basin.getLevel()
                .registryAccess()));
        if (recipe instanceof IrradiatingRecipe basinRecipe) {
            if (basinRecipe.getRollableResults()
                    .isEmpty()
                    && !basinRecipe.getFluidResults()
                    .isEmpty())
                filterTest = filter.test(basinRecipe.getFluidResults()
                        .get(0));
        }

        if (!filterTest)
            return false;

        return apply(basin, recipe, true);
    }

    public static boolean apply(BasinBlockEntity basin, Recipe<?> recipe) {
        return apply(basin, recipe, false);
    }

    private static boolean apply(BasinBlockEntity basin, Recipe<?> recipe, boolean test) {
        boolean isBasinRecipe = recipe instanceof IrradiatingRecipe;
        IItemHandler availableItems = basin.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, basin.getBlockPos(), null);
        IFluidHandler availableFluids = basin.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, basin.getBlockPos(), null);

        if (availableItems == null || availableFluids == null)
            return false;

        BlazeBurnerBlock.HeatLevel heat = ((BasinBlockEntityAccessor)basin).getHeatLevelAccessor();
        if (isBasinRecipe && !((IrradiatingRecipe) recipe).getRequiredHeat()
                .testBlazeBurner(heat))
            return false;

        List<ItemStack> recipeOutputItems = new ArrayList<>();
        List<FluidStack> recipeOutputFluids = new ArrayList<>();

        List<Ingredient> ingredients = new LinkedList<>(recipe.getIngredients());
        List<SizedFluidIngredient> fluidIngredients =
                isBasinRecipe ? ((IrradiatingRecipe) recipe).getFluidIngredients() : Collections.emptyList();

        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;

            int[] extractedItemsFromSlot = new int[availableItems.getSlots()];
            int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

            Ingredients:
            for (Ingredient ingredient : ingredients) {
                for (int slot = 0; slot < availableItems.getSlots(); slot++) {
                    if (simulate && availableItems.getStackInSlot(slot)
                            .getCount() <= extractedItemsFromSlot[slot])
                        continue;
                    ItemStack extracted = availableItems.extractItem(slot, 1, true);
                    if (!ingredient.test(extracted))
                        continue;
                    if (!simulate)
                        availableItems.extractItem(slot, 1, false);
                    extractedItemsFromSlot[slot]++;
                    continue Ingredients;
                }

                // something wasn't found
                return false;
            }

            boolean fluidsAffected = false;
            FluidIngredients:
            for (SizedFluidIngredient fluidIngredient : fluidIngredients) {
                int amountRequired = fluidIngredient.amount();

                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (simulate && fluidStack.getAmount() <= extractedFluidsFromTank[tank])
                        continue;
                    if (!fluidIngredient.test(fluidStack))
                        continue;
                    int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                    if (!simulate) {
                        fluidStack.shrink(drainedAmount);
                        fluidsAffected = true;
                    }
                    amountRequired -= drainedAmount;
                    if (amountRequired != 0)
                        continue;
                    extractedFluidsFromTank[tank] += drainedAmount;
                    continue FluidIngredients;
                }

                // something wasn't found
                return false;
            }

            if (fluidsAffected) {
                basin.getBehaviour(SmartFluidTankBehaviour.INPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                basin.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
            }

            if (simulate) {
                CraftingInput remainderInput = new DummyCraftingContainer(availableItems, extractedItemsFromSlot)
                        .asCraftInput();

                if (recipe instanceof IrradiatingRecipe basinRecipe) {
                    recipeOutputItems.addAll(basinRecipe.rollResults(basin.getLevel().random));

                    for (FluidStack fluidStack : basinRecipe.getFluidResults())
                        if (!fluidStack.isEmpty())
                            recipeOutputFluids.add(fluidStack);
                    for (ItemStack stack : basinRecipe.getRemainingItems(remainderInput))
                        if (!stack.isEmpty())
                            recipeOutputItems.add(stack);

                } else {
                    recipeOutputItems.add(recipe.getResultItem(basin.getLevel()
                            .registryAccess()));

                    if (recipe instanceof CraftingRecipe craftingRecipe) {
                        for (ItemStack stack : craftingRecipe.getRemainingItems(remainderInput))
                            if (!stack.isEmpty())
                                recipeOutputItems.add(stack);
                    }
                }
            }

            if (!basin.acceptOutputs(recipeOutputItems, recipeOutputFluids, simulate))
                return false;
        }

        return true;
    }

    @Override
    protected int getMaxInputCount() {
        return 64;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 2;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 2;
    }

    @Override
    protected boolean canRequireHeat() {
        return true;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public int getColor() {
        return requiredColor;
    }

    @Override
    public float getPower() {
        return requiredPower;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return false;
    }

    public static class Serializer<R extends IrradiatingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<LaserRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, LaserRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, LaserRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
