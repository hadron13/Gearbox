package io.github.hadron13.gearbox.compat.jei.category.animations;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.WEST;

public class AnimatedSapper extends AnimatedKinetics {

    public Block log;
    public Block leaf;
    @Override
    public void draw(PoseStack matrixStack, int xOffset, int yOffset) {
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 200);
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(-15.5f));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(22.5f));
        int scale = 23;

        final float blockSize = 1f;
        final float pixelSize = blockSize/16f;

        blockElement(cogwheel())
                .rotateBlock(0, getCurrentAngle() * 2, 90)
                .atLocal(0, blockSize * 3, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModBlocks.SAPPER.getDefaultState().setValue(HORIZONTAL_FACING, WEST))
                .atLocal(0, blockSize * 3, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.SAPPER_HEAD)
                .rotateBlock(getCurrentAngle() * -4, 90,0 )
                .atLocal(blockSize + pixelSize * 4, blockSize * 3, 0)
                .scale(scale)
                .render(matrixStack);

        blockElement(ModPartialModels.SAPPER_POLE)
                .rotateBlock(0, 90,0 )
                .atLocal(blockSize + pixelSize * 4, blockSize * 3, 0)
                .scale(scale)
                .render(matrixStack);

        // render da arvrinha
        for(int y = 1; y <= 3; y++) {
            blockElement(log.defaultBlockState())
                    .atLocal(2 * blockSize, blockSize * y, 0)
                    .scale(scale)
                    .render(matrixStack);
        }

        int[] leafX = {2, 2, 1, 3};
        int[] leafZ = {-1, 1, 0, 0};

        for(int i = 0; i < 4; i++) {
            blockElement(leaf.defaultBlockState())
                    .atLocal(leafX[i] * blockSize, blockSize, leafZ[i] * blockSize)
                    .scale(scale)
                    .render(matrixStack);
        }
        blockElement(leaf.defaultBlockState())
                .atLocal(blockSize * 2, 0, 0)
                .scale(scale)
                .render(matrixStack);

        matrixStack.popPose();
    }
}