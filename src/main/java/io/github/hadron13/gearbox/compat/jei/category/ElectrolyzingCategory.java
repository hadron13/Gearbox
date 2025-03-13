package io.github.hadron13.gearbox.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.utility.Components;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.compat.jei.category.animations.AnimatedElectrolyzer;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class ElectrolyzingCategory extends BasinCategory {

    private final AnimatedElectrolyzer electrolyzer = new AnimatedElectrolyzer();
    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();
    public ElectrolyzingCategory(Info<BasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, PoseStack matrixStack, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, matrixStack, mouseX, mouseY);
        ElectrolyzingRecipe electrolyzingRecipe = (ElectrolyzingRecipe) recipe;

        Font font = Minecraft.getInstance().font;

        int requiredEnergy = electrolyzingRecipe.requiredEnergy;
        String powerString = ((requiredEnergy > 999)? (float)requiredEnergy/1000f + "kFe"  :  requiredEnergy + "Fe") + "/tick";

        Component power = Components.literal("\u2592 " + powerString);

        font.draw(matrixStack, power, 20, 20, 0xffffff);

        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE)
            heater.withHeat(requiredHeat.visualizeAsBlazeBurner())
                    .draw(matrixStack, getBackground().getWidth() / 2 + 3, 55);
        electrolyzer.draw(matrixStack, getBackground().getWidth() / 2 + 3, 34);
    }



}
