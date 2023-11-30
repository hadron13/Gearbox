package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import io.github.hadron13.gearbox.compat.jei.ModGuiTextures;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedCampfire;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedCompressor;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

import static com.simibubi.create.content.processing.recipe.HeatCondition.NONE;

public class CompressingCategory extends CreateRecipeCategory<CompressingRecipe> {

    AnimatedCompressor compressor = new AnimatedCompressor();
    AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
    AnimatedCampfire campfire = new AnimatedCampfire();

    public CompressingCategory(CreateRecipeCategory.Info<CompressingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompressingRecipe recipe, IFocusGroup focuses) {
        int yCenter = 75/2;

        FluidIngredient fluidIngredient = recipe.getFluidIngredients().get(0);

        builder
                .addSlot(RecipeIngredientRole.INPUT, getBackground().getWidth() / 4 - 19 / 2, 23)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(ForgeTypes.FLUID_STACK, withImprovedVisibility(fluidIngredient.getMatchingFluidStacks()))
                .addTooltipCallback(addFluidTooltip(fluidIngredient.getRequiredAmount()));

        int size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        int i = 0;

        for (ProcessingOutput result : recipe.getRollableResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPosition = -19 * (i / 2) + 23;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
                    .setBackground(getRenderedSlot(result), -1, -1)
                    .addItemStack(result.getStack())
                    .addTooltipCallback(addStochasticTooltip(result));
            i++;
        }



    }

    public void draw(CompressingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        HeatCondition requiredHeat = recipe.getRequiredHeat();

        boolean useCampfire = requiredHeat == NONE;

        AllGuiTextures.JEI_LIGHT.render(matrixStack, 81 - 17, 60);
        ModGuiTextures.JEI_HEAT_BAR_CENTERED.render(matrixStack, 4, 52);

        ModGuiTextures.JEI_SHORT_ARROW.render(matrixStack, getBackground().getWidth() / 2 + 20, 27);

        String translationKey = (useCampfire)? "recipe.compressing.campfire_heat" : requiredHeat.getTranslationKey();

        Minecraft.getInstance().font.draw(matrixStack, Lang.translateDirect(translationKey), 9,
                58, requiredHeat.getColor());

        if (useCampfire){
            campfire.draw(matrixStack, getBackground().getWidth() / 2 + 3 - 17, 27);

        }else{
            heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
                    .draw(matrixStack, getBackground().getWidth() / 2 + 3 - 17, 27);
        }

        compressor.draw(matrixStack, getBackground().getWidth() / 2 + 3 - 17, 6);

    }
}
