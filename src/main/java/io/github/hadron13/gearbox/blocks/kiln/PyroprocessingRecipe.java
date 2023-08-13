package io.github.hadron13.gearbox.blocks.kiln;


import javax.annotation.ParametersAreNonnullByDefault;

import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class PyroprocessingRecipe extends AbstractCrushingRecipe {

    public PyroprocessingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.PYROPROCESSING, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

}