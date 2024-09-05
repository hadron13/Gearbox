package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.compat.jei.ModGuiTextures;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedCentrifuge;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;

import static com.simibubi.create.content.processing.recipe.HeatCondition.NONE;

public class CentrifugingCategory extends CreateRecipeCategory<CentrifugingRecipe> {

    AnimatedCentrifuge centrifuge = new AnimatedCentrifuge();

    public CentrifugingCategory(Info<CentrifugingRecipe> info) {
        super(info);
    }
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        int yCenter = 75/2;

        FluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);

        builder
                .addSlot(RecipeIngredientRole.INPUT, getBackground().getWidth() / 4 - 19 / 2, 23)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
                .addTooltipCallback(addFluidTooltip(fluidIngredient.getRequiredAmount()));

    }

    public void draw(CompressingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {


    }



}
