package io.github.hadron13.gearbox.blocks.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

import static net.minecraft.core.Direction.NORTH;

public class LaserBeamRenderer<T extends BlockEntity & ILaserEmitter> extends SafeBlockEntityRenderer<T> {


    public LaserBeamRenderer(BlockEntityRendererProvider.Context context) {

    }
    @Override
    public boolean shouldRenderOffScreen(T pBlockEntity) {
        return true;
    }

    @Override
    public int getViewDistance(){
        return LaserBeamBehavior.MAX_LENGTH;
    }

    @Override
    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    @Override
    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        VertexConsumer laserVertexConsumer = bufferSource.getBuffer(ModRenderTypes.laserBeam());
        List<Laser> lasers = be.getLasers();
        BlockPos blockPos = be.getBlockPos();

        for(Laser l : lasers) {
            renderLaserBeam(l, ms, laserVertexConsumer, blockPos);
        }
    }


    public static void renderLaserBeam(Laser laser, PoseStack ms, VertexConsumer vertexConsumer, BlockPos blockEntityPos){
        if(!laser.enabled) return;

        ms.pushPose();
        float thickness = 4 / 16f ;
        Vec3 relativeLaserPos = laser.position.subtract(Vec3.atCenterOf(blockEntityPos));
        TransformStack.of(ms)
                .translate(relativeLaserPos)
                .translate(0.5f )
                .rotateTo(new Vector3f(0, 0, 1.0f), laser.direction.toVector3f())
                .translate(-thickness/2f, -thickness/2f, 0)
        ;
        renderBeam(ms.last().pose(), vertexConsumer, laser.length, thickness, laser.color);
        ms.popPose();
    }



    private static void renderBeam(Matrix4f pPose, VertexConsumer pConsumer, float size, float thickness, int color_rgb) {
        renderFaceNorth( pPose, pConsumer,(int)size, color_rgb, 0.0F, thickness, 0.0F, thickness, size, size, size, size, Direction.SOUTH);
        renderFaceNorth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, NORTH);
        renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, thickness, thickness, thickness, 0.0F, 0.0F, size, size, 0.0F, Direction.EAST);
        renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, 0.0F, 0.0F, 0.0F, thickness, 0.0F, size, size, 0.0F, Direction.WEST);
        renderFaceSouth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, 0.0F, 0.0F, 0.0F, 0.0F, size, size, Direction.DOWN);
        renderFaceUp( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, thickness, size, size, 0.0F, 0.0F, Direction.UP);
    }

    private static void renderFaceSouth(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    //UV shenanigans
    private static void renderFaceEastWest(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    private static void renderFaceUp(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    private static void renderFaceNorth(Matrix4f pPose, VertexConsumer pConsumer, int laser_size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
}
