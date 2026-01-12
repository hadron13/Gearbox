package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public class DistillingRecipe extends ProcessingRecipe<RecipeInput, DistillationRecipeParams> {

    public DistillationControllerBlockEntity.DistilMode mode;

    public DistillingRecipe(DistillationRecipeParams params) {
        super(GearboxRecipeTypes.DISTILLING, params);
        mode = switch (params.mode){
            case "distil_flash" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_FLASH;
            case "distil_atmospheric" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_ATMOSPHERIC;
            case "distil_vacuum" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_VACUUM;
            default -> null;
        };
    }

    @Override
    public List<String> validate() {
        List<String> errors = super.validate();
        if(mode == null){
            errors.add("invalid distilling mode");
        }
        return errors;
    }

    public static  boolean match(DistillationControllerBlockEntity be, DistillingRecipe recipe){
        if(recipe == null)
            return false;
        SizedFluidIngredient fluidIngredient = recipe.fluidIngredients.get(0);

        IFluidHandler availableFluids = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
        if(availableFluids == null)
            return false;
        if(be.distilMode.get() != recipe.mode)
            return false;

        for(int i = 0; i < availableFluids.getTanks(); i++){
            FluidStack fluid = availableFluids.getFluidInTank(i);
            if(fluidIngredient.test(fluid) &&
                fluid.getAmount() >= fluidIngredient.amount()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 8;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    public static class Serializer<R extends DistillingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<DistillationRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, DistillationRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, DistillationRecipeParams.STREAM_CODEC);
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

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }
}
