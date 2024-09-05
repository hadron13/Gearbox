package io.github.hadron13.gearbox.blocks.laser_drill;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.latvian.mods.rhino.ast.Block;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.irradiator.LaserRecipe;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class LaserDrillingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe {

    public float requiredPower;
    public Color requiredColor;

    public LaserDrillingRecipe( ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.LASER_DRILLING, params);
        Item block = this.getIngredients().get(0).getItems()[0].getItem();

        if(!(block instanceof BlockItem)){
            Gearbox.LOGGER.warn("recipe contains non-block items");
        }
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 3;
    }

    @Override
    public Color getColor() {
        return requiredColor;
    }

    @Override
    public float getPower() {
        return requiredPower;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }

    public static boolean match(LaserDrillBlockEntity entity, LaserDrillingRecipe recipe){

        Item input = recipe.getIngredients().get(0).getItems()[0].getItem();
        if(input instanceof BlockItem block){

            if(entity.blockUnder != block.getBlock())
                return false;

        }

        return false;
    }

}
