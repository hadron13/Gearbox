package io.github.hadron13.gearbox.blocks.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
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
            float thickness = 4 / 16f ;//+ Mth.sin(t) * 1/16f;
            Vec3 relativeLaserPos = l.position.subtract(blockPos.getCenter());
            TransformStack.of(ms)
                    .translate(relativeLaserPos)
                    .translate(0.5f )
                    .rotateTo(new Vector3f(0, 0, 1.0f), l.rotation.toVector3f())
                    .translate(-thickness/2f, -thickness/2f, 0)
            ;
            renderBeam(ms.last().pose(), laserVertexConsumer, 500.0f, thickness, l.color);
        }
    }




    private void renderBeam(Matrix4f pPose, VertexConsumer pConsumer, float size, float thickness, int color_rgb) {
        this.renderFaceNorth( pPose, pConsumer,(int)size, color_rgb, 0.0F, thickness, 0.0F, thickness, size, size, size, size, Direction.SOUTH);
        this.renderFaceNorth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, NORTH);
        this.renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, thickness, thickness, thickness, 0.0F, 0.0F, size, size, 0.0F, Direction.EAST);
        this.renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, 0.0F, 0.0F, 0.0F, thickness, 0.0F, size, size, 0.0F, Direction.WEST);
        this.renderFaceSouth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, 0.0F, 0.0F, 0.0F, 0.0F, size, size, Direction.DOWN);
        this.renderFaceUp( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, thickness, size, size, 0.0F, 0.0F, Direction.UP);
    }

    private void renderFaceSouth(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    //UV shenanigans
    private void renderFaceEastWest(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    private void renderFaceUp(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
    private void renderFaceNorth(Matrix4f pPose, VertexConsumer pConsumer, int laser_size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();
        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
}
