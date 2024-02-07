package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedElectrolyzer;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;

public class ElectrolyzingCategory extends BasinCategory {

    private final AnimatedElectrolyzer electrolyzer = new AnimatedElectrolyzer();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
    public ElectrolyzingCategory(Info<BasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, matrixStack, mouseX, mouseY);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE)
            heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
                    .draw(matrixStack, getBackground().getWidth() / 2 + 3, 55);
        electrolyzer.draw(matrixStack, getBackground().getWidth() / 2 + 3, 34);
    }
}
