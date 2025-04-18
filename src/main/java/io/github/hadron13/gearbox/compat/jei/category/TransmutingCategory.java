package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.Components;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedIrradiator;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TransmutingCategory extends CreateRecipeCategory<TransmutingRecipe> {

    private final AnimatedIrradiator irradiator = new AnimatedIrradiator(false);
    public TransmutingCategory(Info<TransmutingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransmutingRecipe recipe, IFocusGroup focuses) {


        builder
                .addSlot(RecipeIngredientRole.INPUT, 27, 51)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        List<ProcessingOutput> results = recipe.getRollableResults();
        int i = 0;
        for (ProcessingOutput output : results) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 131 + 19 * i, 51)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    @Override
    public void draw(TransmutingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(matrixStack, 61, 41 + 23);
//        AllGuiTextures.JEI_LONG_ARROW.render(matrixStack, 52, 54 - 23);
        AllGuiTextures.JEI_DOWN_ARROW.render(matrixStack, 126, 29);


        Font font = Minecraft.getInstance().font;


        Component power = Components.literal("\u2592 " + SpectrometerBlockEntity.truncatePrecision(recipe.requiredPower, 2));

        Component red = Components.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getRed()/255f, 2));
        Component green = Components.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getGreen()/255f, 2));
        Component blue = Components.literal("\u2588 " +
                SpectrometerBlockEntity.truncatePrecision(recipe.requiredColor.getBlue()/255f, 2));

        font.drawShadow(matrixStack, power, 20, 1, 0xffffff);
        font.draw(matrixStack, red, 20, 11, 0xbd5252);
        font.draw(matrixStack, green, 20, 21, 0x2d9636);
        font.draw(matrixStack, blue, 20, 31, 0x2e2d96);

        irradiator.color = recipe.requiredColor.getRGB();
        irradiator.draw(matrixStack, getBackground().getWidth() / 2 - 17, 22);
    }

}
