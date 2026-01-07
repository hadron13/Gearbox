package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;


public class CentrifugingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public CentrifugingRecipe(ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.CENTRIFUGING, params);
    }

    public static boolean match(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe) {
        return apply(centrifuge, recipe, true);
    }

    public static boolean apply(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe, boolean test){
        NonNullList<SizedFluidIngredient> fluidIngredients = recipe.getFluidIngredients();

        IFluidHandler availableFluids = centrifuge.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, centrifuge.getBlockPos(), null);
        int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;


            boolean fluidsAffected = false;
            FluidIngredients:
            for (int i = 0; i < fluidIngredients.size(); i++) {
                FluidIngredient fluidIngredient = fluidIngredients.get(i).ingredient();
                int amountRequired = fluidIngredients.get(i).amount();

                for (int tank = 0; tank < availableFluids.getTanks(); tank++) {
                    FluidStack fluidStack = availableFluids.getFluidInTank(tank);
                    if (fluidStack.getAmount() <= extractedFluidsFromTank[tank])
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
                centrifuge.getBehaviour(SmartFluidTankBehaviour.INPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
                centrifuge.getBehaviour(SmartFluidTankBehaviour.OUTPUT)
                        .forEach(SmartFluidTankBehaviour.TankSegment::onFluidStackChanged);
            }


            IFluidHandler targetTank = centrifuge.outputTank.getCapability();

            for (FluidStack fluidStack : recipe.getFluidResults()) {
                IFluidHandler.FluidAction action = simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE;
                int fill = targetTank instanceof SmartFluidTankBehaviour.InternalFluidHandler
                        ? ((SmartFluidTankBehaviour.InternalFluidHandler) targetTank).forceFill(fluidStack.copy(), action)
                        : targetTank.fill(fluidStack.copy(), action);
                if (fill != fluidStack.getAmount())
                    return false;
            }
        }
        return true;

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
        return 6;
    }


    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }
}
