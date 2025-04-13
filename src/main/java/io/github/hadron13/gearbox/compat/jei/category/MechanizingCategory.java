package io.github.hadron13.gearbox.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedBrassPress;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;

import java.util.List;

public class MechanizingCategory extends CreateRecipeCategory<MechanizingRecipe> {

    public final AnimatedBrassPress press = new AnimatedBrassPress();
    public MechanizingCategory(CreateRecipeCategory.Info<MechanizingRecipe> info){super(info);}

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MechanizingRecipe recipe, IFocusGroup focuses) {
        builder
                .addSlot(RecipeIngredientRole.INPUT, 27, 27)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        List<ProcessingOutput> results = recipe.getRollableResults();
        int i = 0;
        for (ProcessingOutput output : results) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 131 + 19 * i, 51)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack()).addRichTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    @Override
    public void draw(MechanizingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 41 + 23);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 126, 29);

        press.draw(graphics, getBackground().getWidth() / 2 - 17, 22);
    }

}
