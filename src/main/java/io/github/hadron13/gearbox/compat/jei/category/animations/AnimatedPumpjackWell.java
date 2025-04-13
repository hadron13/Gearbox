package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import io.github.hadron13.gearbox.register.ModBlocks;
import net.minecraft.client.gui.GuiGraphics;

import static io.github.hadron13.gearbox.blocks.kiln.KilnBlock.POWERED;

public class AnimatedPumpjackWell extends AnimatedKinetics {
    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0);

        blockElement(ModBlocks.PUMPJACK_WELL.getDefaultState())
                .rotateBlock(22.5, 22.5+180, 0)
                .scale(23)
                .render(graphics);
        matrixStack.popPose();
    }
}
