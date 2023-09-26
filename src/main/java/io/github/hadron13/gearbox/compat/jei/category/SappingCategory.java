package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedKiln;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedSapper;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class SappingCategory extends CreateRecipeCategory<SappingRecipe> {

    private final AnimatedSapper sapper = new AnimatedSapper();
    public SappingCategory(CreateRecipeCategory.Info<SappingRecipe> info) {
        super(info);
    }


    public void setRecipe(IRecipeLayoutBuilder builder, SappingRecipe recipe, IFocusGroup focuses) {
        int yCenter = 103/2;

        builder .addSlot(RecipeIngredientRole.INPUT, 15, yCenter - 19)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(1));

        builder .addSlot(RecipeIngredientRole.INPUT, 15, yCenter)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        FluidStack fluidResult = recipe.getFluidResults().get(0);
        builder
                .addSlot(RecipeIngredientRole.OUTPUT, 142, yCenter + 16)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidResult))
                .addTooltipCallback(addFluidTooltip(fluidResult.getAmount()));

    }


    public void draw(SappingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        int yCenter = 103/2;

        AllGuiTextures.JEI_SHADOW.render(matrixStack, 80, 103-16);
        AllGuiTextures.JEI_SHADOW.render(matrixStack, 40, 103-11);

        BlockItem logItem  = (BlockItem)( recipe.getIngredients().get(0).getItems()[0].getItem() );
        BlockItem leafItem = (BlockItem)( recipe.getIngredients().get(1).getItems()[0].getItem() );

        sapper.log = logItem.getBlock();
        sapper.leaf = leafItem.getBlock();

        sapper.draw(matrixStack, 48, 27);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 136, yCenter);


    }
}
