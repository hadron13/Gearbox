package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactingRecipe;
import io.github.hadron13.gearbox.compat.jei.ModGuiTextures;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedReactor;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class ReactingCategory extends BasinCategory {

    public final AnimatedReactor reactor = new AnimatedReactor();
    public final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    public ReactingCategory(Info<BasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
        super.setRecipe(builder, recipe, focuses);

        assert recipe instanceof ReactingRecipe;
        ReactingRecipe reactingRecipe = (ReactingRecipe)recipe;

        builder.addSlot(RecipeIngredientRole.INPUT, 45,15)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(reactingRecipe.atmosphere.getMatchingFluidStacks()))
                .addTooltipCallback(addFluidTooltip(reactingRecipe.atmosphere.getRequiredAmount()));
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, matrixStack, mouseX, mouseY);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE)
            heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
                    .draw(matrixStack, getBackground().getWidth() / 2 + 3, 55);
        reactor.draw(matrixStack, getBackground().getWidth() / 2 + 3, 34);

        assert recipe instanceof ReactingRecipe;

        ReactingRecipe reactingRecipe = (ReactingRecipe) recipe;
        if(reactingRecipe.atmosphere != FluidIngredient.EMPTY){
            ModGuiTextures.JEI_SHORT_ARROW.render(matrixStack, 65, 19);
        }


        Font font = Minecraft.getInstance().font;
        font.drawShadow(matrixStack, reactingRecipe.rpm_min + " <> " + reactingRecipe.rpm_max  +" RPM", 5, 1, 0xFFFFFF);


    }

}
