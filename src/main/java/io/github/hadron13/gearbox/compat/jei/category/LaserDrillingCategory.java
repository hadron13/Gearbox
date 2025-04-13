package io.github.hadron13.gearbox.compat.jei.category;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillingRecipe;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedLaserDrill;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LaserDrillingCategory extends CreateRecipeCategory<LaserDrillingRecipe> {

    private final AnimatedLaserDrill laser_drill = new AnimatedLaserDrill();
    public LaserDrillingCategory(Info<LaserDrillingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, LaserDrillingRecipe recipe, IFocusGroup focuses) {

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
    public void draw(LaserDrillingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 41 + 23);
//        AllGuiTextures.JEI_LONG_ARROW.render(matrixStack, 52, 54 - 23);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 126, 25);

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

        laser_drill.color = recipe.requiredColor.getRGB();
        laser_drill.draw(graphics, getBackground().getWidth() / 2 - 17, 22);
    }
}
