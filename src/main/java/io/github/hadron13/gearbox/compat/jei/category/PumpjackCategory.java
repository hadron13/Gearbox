package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedBrassPress;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedPumpjackWell;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class PumpjackCategory extends CreateRecipeCategory<PumpjackRecipe>{
    public final AnimatedPumpjackWell well = new AnimatedPumpjackWell();

    public static final int xcenter = 177/2;
    public static final int ycenter = 65/2;

    public PumpjackCategory(CreateRecipeCategory.Info<PumpjackRecipe> info){super(info);}

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PumpjackRecipe recipe, IFocusGroup focuses) {


        FluidStack fluidResult = recipe.getFluidResults().get(0);
        builder
                .addSlot(RecipeIngredientRole.OUTPUT, xcenter + 50, ycenter+5)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
                .addTooltipCallback(addFluidTooltip(fluidResult.getAmount()));
    }

    @Override
    public void draw(PumpjackRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {

        String biome_name = recipe.biome.location().getPath();

        Font font = Minecraft.getInstance().font;
        int width = font.width(biome_name);
//        font.draw(matrixStack, biome_name, xcenter - width/2, ycenter-20, 0);
        font.drawShadow(matrixStack, biome_name, xcenter - width/2, ycenter-20, 0xFFFFFF);

        AllGuiTextures.JEI_ARROW.render(matrixStack, xcenter-15, ycenter+10);
        well.draw(matrixStack, xcenter - 60, ycenter+22);

    }

}
