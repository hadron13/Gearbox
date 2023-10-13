package io.github.hadron13.gearbox.blocks.compressor;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.DummyCraftingContainer;
import com.simibubi.create.foundation.utility.Iterate;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel.KINDLED;
import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel.SMOULDERING;
import static com.simibubi.create.content.processing.recipe.HeatCondition.HEATED;
import static com.simibubi.create.content.processing.recipe.HeatCondition.NONE;

public class CompressingRecipe extends ProcessingRecipe<RecipeWrapper> {
    public CompressingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.COMPRESSING, params);
    }
    public static boolean match(CompressorBlockEntity blockEntity, CompressingRecipe recipe) {
        if(recipe == null || blockEntity == null || blockEntity.getLevel() == null || blockEntity.getLevel().isClientSide)
            return false;

        HeatCondition heatRequirement = recipe.getRequiredHeat();

        BlazeBurnerBlock.HeatLevel heatProvided = blockEntity.getHeat();

        if(heatRequirement == HEATED && !heatProvided.isAtLeast(KINDLED))
            return false;

        if(heatRequirement == NONE && heatProvided != SMOULDERING)
            return false;

        IFluidHandler fluidCapability = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
                .orElse(null);

        if (fluidCapability == null)
            return false;

        FluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);

        FluidStack available = fluidCapability.getFluidInTank(0);

        boolean ingredientMatch = fluidIngredient.test(available);
        boolean enoughFluid = available.getAmount() >= fluidIngredient.getRequiredAmount();

        return ingredientMatch && enoughFluid;
    }

    public boolean sameInOutAs(CompressingRecipe otherRecipe){
        if(otherRecipe == null)
            return false;

        if(!this.getFluidIngredients().equals(otherRecipe.fluidIngredients))
            return false;

        if(!this.getResultItem().equals(otherRecipe.getResultItem()))
            return false;

        return true;
    }

    @Override
    public boolean matches(RecipeWrapper inv, @Nonnull Level worldIn) {
        return false;
    }
    @Override
    public int getMaxInputCount(){
        return 0;
    }
    @Override
    public int getMaxOutputCount(){
        return 1;
    }
    @Override
    public int getMaxFluidInputCount(){
        return 1;
    }

    @Override
    public boolean canRequireHeat() {return true;}


}
