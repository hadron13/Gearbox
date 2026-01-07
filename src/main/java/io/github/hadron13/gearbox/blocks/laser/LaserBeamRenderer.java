package io.github.hadron13.gearbox.blocks.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
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
        return Laser.MAX_LENGTH;
    }

    @Override
    public boolean shouldRender(T pBlockEntity, Vec3 pCameraPos) {
        return true;
    }

    @Override
    protected void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        List<Laser> lasers = be.getLasers();
        BlockPos blockPos = be.getBlockPos();



        for(Laser l : lasers) {


//            if(be instanceof SmartBlockEntity smartBlockEntity && smartBlockEntity.isVirtual()){
//                if(!l.enabled || l.getPower() < 0.01) return;
//                int color = l.getColor() & 0xFFFFFF;
//                Vec3 relativeLaserPos = l.position.subtract(Vec3.atCenterOf(be.getBlockPos()));
//                Vec3 interpolatedDirection = l.lastDirection.lerp(l.direction, partialTicks);
//                renderLaserBeamCustom(2/16f, 0/16f, l.length,  color | (255 << 24), interpolatedDirection, relativeLaserPos, ms, bufferSource);
//                continue;
//            }

            renderLaserBeam(l, ms, bufferSource, blockPos);
        }
    }


    public static void renderLaserBeam(Laser laser, PoseStack ms, MultiBufferSource bufferSource, BlockPos blockEntityPos){
        renderLaserBeamInterpolated(laser, ms, bufferSource, blockEntityPos, 0);
    }

    public static void renderLaserBeamInterpolated(Laser laser, PoseStack ms, MultiBufferSource bufferSource, BlockPos blockEntityPos, float partialTicks){
        if(!laser.enabled || laser.getPower() < 0.01) return;

        int color = laser.getColor() & 0xFFFFFF;
        int alpha = Mth.clamp( (int)((Mth.sqrt(laser.getPower())/5.0 + 0.1) * 200.0), 100, 250);

        Vec3 relativeLaserPos = laser.position.subtract(Vec3.atCenterOf(blockEntityPos));
        Vec3 interpolatedDirection = laser.lastDirection.lerp(laser.direction, partialTicks);

        renderLaserBeamCustom(2/16f, 4/16f, laser.length,  color | (alpha << 24), interpolatedDirection, relativeLaserPos, ms, bufferSource);
    }



    public static void renderLaserBeamCustom(float thickness, float outer_thickness, float length, int color, Vec3 direction, Vec3 position, PoseStack ms, MultiBufferSource bufferSource){

        ms.pushPose();

        TransformStack.of(ms)
                .translate(position)
                .translate(0.5f )
                .rotateTo(new Vector3f(0, 0, 1.0f), direction.toVector3f())
                .translate(-thickness/2f, -thickness/2f, 0)
        ;


        VertexConsumer transluscentVertexConsumer = bufferSource.getBuffer(RenderType.translucent());
        SuperByteBuffer outerBeam = CachedBuffers.partial(GearboxPartialModels.OUTER_LASER_BEAM, GearboxBlocks.LASER.getDefaultState());


        outerBeam
                .scale(thickness/ (6 / 16f), thickness/ (6 / 16f), length)
                .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, 240)
                .light(255)
                .renderInto(ms, transluscentVertexConsumer);
        int alpha = color >> 24 & 0xff;
        if(outer_thickness > 0) {
            outerBeam
                    .translate(-1 / 16f, -1 / 16f, 0)
                    .scale(outer_thickness / (6 / 16f), outer_thickness / (6 / 16f), length)
                    .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, alpha/3)
                    .light(255)
                    .renderInto(ms, transluscentVertexConsumer);


            outer_thickness += 1/16f;
            outerBeam
                    .translate(-1.5 / 16f, -1.5 / 16f, 0)
                    .scale(outer_thickness / (6 / 16f), outer_thickness / (6 / 16f), length)
                    .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, alpha/3)
                    .light(255)
                    .renderInto(ms, transluscentVertexConsumer);


            outer_thickness += 1/16f;
            outerBeam
                    .translate(-2 / 16f, -2 / 16f, 0)
                    .scale(outer_thickness / (6 / 16f), outer_thickness / (6 / 16f), length)
                    .color((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, alpha/3)
                    .light(255)
                    .renderInto(ms, transluscentVertexConsumer);
        }

//        VertexConsumer laserVertexConsumer = bufferSource.getBuffer(ModRenderTypes.laserBeam());
//        renderBeam(ms.last().pose(), laserVertexConsumer, length, thickness, color);
        ms.popPose();
    }
//
//    private static void renderBeam(Matrix4f pPose, VertexConsumer pConsumer, float size, float thickness, int color_rgb) {
//        renderFaceNorth( pPose, pConsumer,(int)size, color_rgb, 0.0F, thickness, 0.0F, thickness, size, size, size, size, Direction.SOUTH);
//        renderFaceNorth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, NORTH);
//        renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, thickness, thickness, thickness, 0.0F, 0.0F, size, size, 0.0F, Direction.EAST);
//        renderFaceEastWest( pPose, pConsumer,(int)size,color_rgb, 0.0F, 0.0F, 0.0F, thickness, 0.0F, size, size, 0.0F, Direction.WEST);
//        renderFaceSouth( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, 0.0F, 0.0F, 0.0F, 0.0F, size, size, Direction.DOWN);
//        renderFaceUp( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, thickness, size, size, 0.0F, 0.0F, Direction.UP);
//    }
//
//    private static void renderFaceSouth(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
//        Vec3i normal = pDirection.getNormal();
//        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, size).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 0.0F).uv2(size,0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//    }
//    //UV shenanigans
//    private static void renderFaceEastWest(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
//        Vec3i normal = pDirection.getNormal();
//        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//    }
//    private static void renderFaceUp(Matrix4f pPose, VertexConsumer pConsumer, int size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
//        Vec3i normal = pDirection.getNormal();
//        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, size).uv2(size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//    }
//    private static void renderFaceNorth(Matrix4f pPose, VertexConsumer pConsumer, int laser_size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
//        Vec3i normal = pDirection.getNormal();
//        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size, 0).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
//    }
}
