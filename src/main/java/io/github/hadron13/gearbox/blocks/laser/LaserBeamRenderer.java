package io.github.hadron13.gearbox.blocks.laser;

import com.google.common.cache.Cache;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.Color;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.blocks.black_hole.BlackHoleBlockEntity;
import io.github.hadron13.gearbox.register.ModPartialModels;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;
import static net.minecraft.core.Direction.NORTH;

public class LaserBeamRenderer<T extends SmartBlockEntity> extends SafeBlockEntityRenderer<T> {


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
//        if(Backend.canUseInstancing(be.getLevel()))
//            return;


        LaserBeamBehavior beamBehavior = be.getBehaviour(LaserBeamBehavior.TYPE);
        if(beamBehavior == null)
            return;
        for(LaserBeamBehavior.LaserBeam beam : beamBehavior.beams.values()) {

            if(!beam.enabled) {
                continue;
            }
            Direction facing = beam.facing;
            float scale = beam.length;

            if(scale == 0)
                continue;

            float thickness =  Mth.clamp( (float)Math.sqrt((beam.power+20f)/20f), 1f, 3f ) * (1.4f/16f);

            ms.pushPose();
            TransformStack
                    .cast(ms)
                    .translate(0.5f - thickness/2, 0.5f - thickness/2, 0.5f - thickness/2)
                    .translate( thickness/2,  thickness/2, thickness/2)
                    .rotate(facing, AnimationTickHolder.getRenderTime()/30f)
                    .rotateToFace( facing.getOpposite())
                    .translate(- thickness/2, - thickness/2, -thickness/2)
            ;

            VertexConsumer vbTranslucent = bufferSource.getBuffer(ModRenderTypes.laserBeam());

            beam.color.setAlpha(0.8f);

            renderBeam(ms.last().pose(), vbTranslucent, scale, thickness, beam.color.getRGB());
            ms.popPose();
        }
    }



    private void renderBeam(Matrix4f pPose, VertexConsumer pConsumer, float size, float thickness, int color_rgb) {
        this.renderFace( pPose, pConsumer,(int)size, color_rgb, 0.0F, thickness, 0.0F, thickness, size, size, size, size, Direction.SOUTH);
        this.renderFace( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, NORTH);
        this.renderFace( pPose, pConsumer,(int)size,color_rgb, thickness, thickness, thickness, 0.0F, 0.0F, size, size, 0.0F, Direction.EAST);
        this.renderFace( pPose, pConsumer,(int)size,color_rgb, 0.0F, 0.0F, 0.0F, thickness, 0.0F, size, size, 0.0F, Direction.WEST);
        this.renderFace( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, 0.0F, 0.0F, 0.0F, 0.0F, size, size, Direction.DOWN);
        this.renderFace( pPose, pConsumer,(int)size,color_rgb, 0.0F, thickness, thickness, thickness, size, size, 0.0F, 0.0F, Direction.UP);
    }


    private void renderFace(Matrix4f pPose, VertexConsumer pConsumer, int laser_size, int color_rgb, float pX0, float pX1, float pY0, float pY1, float pZ0, float pZ1, float pZ2, float pZ3, Direction pDirection) {
        Vec3i normal = pDirection.getNormal();

        pConsumer.vertex(pPose, pX0, pY0, pZ0).color(color_rgb).uv(0.0F, 1.0F).uv2(laser_size).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY0, pZ1).color(color_rgb).uv(0.0F, 0.0F).uv2(laser_size).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX1, pY1, pZ2).color(color_rgb).uv(1.0F, 0.0F).uv2(laser_size).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();
        pConsumer.vertex(pPose, pX0, pY1, pZ3).color(color_rgb).uv(1.0F, 1.0F).uv2(laser_size).normal(normal.getX(), normal.getY(), normal.getZ()).endVertex();


    }


}
