package io.github.hadron13.gearbox.blocks.black_hole;

import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public class BlackHoleRenderer extends SafeBlockEntityRenderer<BlackHoleBlockEntity> {
    public BlackHoleRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    protected void renderSafe(BlackHoleBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        ms.pushPose();
        float scale = Mth.sin(AnimationTickHolder.getRenderTime()/30f)/4 + 1.5f;
        ms.translate(-scale/2 + .5f, -scale/2 + .5f, -scale/2 + 0.5f);
        ms.scale(scale, scale, scale);

        TransformStack.cast(ms)
                .centre()
                .rotateY(AnimationTickHolder.getRenderTime())
                .rotateX(AnimationTickHolder.getRenderTime())
                .rotateZ(AnimationTickHolder.getRenderTime())
                .unCentre();
        Matrix4f matrix4f = ms.last().pose();

        this.renderCube(be, matrix4f,  bufferSource.getBuffer(RenderType.endGateway()));

        ms.popPose();
    }

    private void renderCube(BlackHoleBlockEntity pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer) {
        float f = 0f;
        float f1 = 1f;
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, Direction.SOUTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, Direction.NORTH);
        this.renderFace(pBlockEntity, pPose, pConsumer, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.EAST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, 1.0F, 0.0F, Direction.WEST);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, f, f, 0.0F, 0.0F, 1.0F, 1.0F, Direction.DOWN);
        this.renderFace(pBlockEntity, pPose, pConsumer, 0.0F, 1.0F, f1, f1, 1.0F, 1.0F, 0.0F, 0.0F, Direction.UP);
    }


    private void renderFace(BlackHoleBlockEntity pBlockEntity, Matrix4f pPose, VertexConsumer pConsumer, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        pConsumer.vertex(pPose, pX0, pY0, pZ0).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).endVertex();
    }
}
