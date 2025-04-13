package io.github.hadron13.gearbox.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedSapper;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.fluids.FluidStack;

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
        builder.addSlot(RecipeIngredientRole.OUTPUT, 142, yCenter + 16)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(ForgeTypes.FLUID_STACK, fluidResult)
                .setFluidRenderer(fluidResult.getAmount(), false, 16, 16);

    }


    public void draw(SappingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        int yCenter = 103/2;

        AllGuiTextures.JEI_SHADOW.render(graphics, 80, 103-16);
        AllGuiTextures.JEI_SHADOW.render(graphics, 40, 103-11);

        BlockItem logItem  = (BlockItem)( recipe.getIngredients().get(0).getItems()[0].getItem() );
        BlockItem leafItem = (BlockItem)( recipe.getIngredients().get(1).getItems()[0].getItem() );

        sapper.log = logItem.getBlock();
        sapper.leaf = leafItem.getBlock();

        sapper.draw(graphics, 48, 27);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 136, yCenter);


    }
}
