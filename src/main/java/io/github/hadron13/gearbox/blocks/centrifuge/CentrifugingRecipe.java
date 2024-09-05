package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Collections;
import java.util.List;

public class CentrifugingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public CentrifugingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.CENTRIFUGING, params);
    }

    public static boolean match(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe) {
        return apply(centrifuge, recipe, true);
    }

    public static boolean apply(CentrifugeBlockEntity centrifuge, CentrifugingRecipe recipe, boolean test){
        List<FluidIngredient> fluidIngredients = recipe.getFluidIngredients();

        IFluidHandler availableFluids = centrifuge.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                .orElse(null);

        int[] extractedFluidsFromTank = new int[availableFluids.getTanks()];

        for (boolean simulate : Iterate.trueAndFalse) {

            if (!simulate && test)
                return true;


            boolean fluidsAffected = false;
            FluidIngredients:
            for (int i = 0; i < fluidIngredients.size(); i++) {
                FluidIngredient fluidIngredient = fluidIngredients.get(i);
                int amountRequired = fluidIngredient.getRequiredAmount();

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


            IFluidHandler targetTank = centrifuge.outputTank.getCapability()
                    .orElse(null);

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
        return 3;
    }


    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }
}
