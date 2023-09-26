package io.github.hadron13.gearbox.blocks.sapper;


import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SappingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public SappingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.SAPPING, params);

        Item logItem = this.getIngredients().get(0).getItems()[0].getItem();
        Item leafItem = this.getIngredients().get(1).getItems()[0].getItem();

        if(logItem instanceof BlockItem && leafItem instanceof BlockItem){
            FluidStack result = this.getFluidResults().get(0);

            SapperBlockEntity.TreeType.registerTree(((BlockItem) logItem).getBlock(), ((BlockItem) leafItem).getBlock(), result);
        }else{
            Gearbox.LOGGER.warn("Sapping recipe id: "+ this.getId().toString() +" contains non-block ingredients");
        }

    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return false;
    }
    @Override
    protected boolean canSpecifyDuration() {
        return false;
    }
    @Override
    protected int getMaxInputCount() {
        return 2;
    }
    @Override
    protected int getMaxOutputCount(){return 0;}
    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }

}