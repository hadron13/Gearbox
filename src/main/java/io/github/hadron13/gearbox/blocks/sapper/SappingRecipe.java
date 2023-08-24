package io.github.hadron13.gearbox.blocks.sapper;


import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SappingRecipe extends AbstractCrushingRecipe {

    public SappingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.SAPPING, params);
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