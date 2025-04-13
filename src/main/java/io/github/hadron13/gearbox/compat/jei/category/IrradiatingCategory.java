package io.github.hadron13.gearbox.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedIrradiator;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.createmod.catnip.data.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class IrradiatingCategory extends CreateRecipeCategory<IrradiatingRecipe> {


    private final AnimatedIrradiator irradiator = new AnimatedIrradiator(true);
    public IrradiatingCategory(CreateRecipeCategory.Info<IrradiatingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IrradiatingRecipe recipe, IFocusGroup focuses) {

        List<Pair<Ingredient, MutableInt>> condensedIngredients = ItemHelper.condenseIngredients(recipe.getIngredients());

        int size = condensedIngredients.size() + recipe.getFluidIngredients().size();
        int xOffset = size < 3 ? (3 - size) * 19 / 2 : 0;
        int i = 0;

        for (Pair<Ingredient, MutableInt> pair : condensedIngredients) {
            List<ItemStack> stacks = new ArrayList<>();
            for (ItemStack itemStack : pair.getFirst().getItems()) {
                ItemStack copy = itemStack.copy();
                copy.setCount(pair.getSecond().getValue());
                stacks.add(copy);
            }

            builder
                    .addSlot(RecipeIngredientRole.INPUT, 7 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addItemStacks(stacks);
            i++;
        }
        for (FluidIngredient fluidIngredient : recipe.getFluidIngredients()) {
            builder
                    .addSlot(RecipeIngredientRole.INPUT, 7 + xOffset + (i % 3) * 19, 51 - (i / 3) * 19)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredients(ForgeTypes.FLUID_STACK, fluidIngredient.getMatchingFluidStacks())
                    .setFluidRenderer(fluidIngredient.getRequiredAmount(), false, 16, 16);
            i++;
        }

        size = recipe.getRollableResults().size() + recipe.getFluidResults().size();
        i = 0;

        for (ProcessingOutput result : recipe.getRollableResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPosition = -19 * (i / 2) + 51;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
                    .setBackground(getRenderedSlot(result), -1, -1)
                    .addItemStack(result.getStack()).addRichTooltipCallback(addStochasticTooltip(result));
            i++;
        }

        for (FluidStack fluidResult : recipe.getFluidResults()) {
            int xPosition = 142 - (size % 2 != 0 && i == size - 1 ? 0 : i % 2 == 0 ? 10 : -9);
            int yPosition = -19 * (i / 2) + 51;
            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, xPosition, yPosition)
                    .setBackground(getRenderedSlot(), -1, -1)
                    .addIngredient(ForgeTypes.FLUID_STACK, fluidResult)
                    .setFluidRenderer(fluidResult.getAmount(), false, 16, 16);
            i++;
        }
    }

    @Override
    public void draw(IrradiatingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 41 + 23);
//        AllGuiTextures.JEI_LONG_ARROW.render(matrixStack, 52, 54 - 23);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 126, 29);


        Component power = Component.literal("\u2592 " + SpectrometerBlockEntity.truncatePrecision(recipe.requiredPower, 2));

        Component red = Component.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getRed()/255f, 2));
        Component green = Component.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getGreen()/255f, 2));
        Component blue = Component.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getBlue()/255f, 2));

        graphics.drawString(Minecraft.getInstance().font, power, 20, 1, 0xffffff, true);
        graphics.drawString(Minecraft.getInstance().font, red, 20, 11, 0xbd5252, false);
        graphics.drawString(Minecraft.getInstance().font, green, 20, 21, 0x2d9636, false);
        graphics.drawString(Minecraft.getInstance().font, blue, 20, 31, 0x2e2d96, false);

        irradiator.color = recipe.requiredColor.getRGB();
        irradiator.draw(graphics, getBackground().getWidth() / 2 - 17, 22);


    }

}
