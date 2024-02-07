package io.github.hadron13.gearbox.blocks.laser;

import com.google.common.cache.Cache;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;

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
        if(Backend.canUseInstancing(be.getLevel()))
            return;

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
                return;

            float xScale = (facing.getAxis() == X)? scale : 1;
            float zScale = (facing.getAxis() == Z)? scale : 1;

            TransformStack
                    .cast(ms)
                    .scale(xScale, 1, zScale)
                    .translate( (facing.getAxis() == X? 0.5f:0) /xScale,
                                0,
                                (facing.getAxis() == Z? 0.5f:0) /zScale  )
                    .rotateCentered(facing, AnimationTickHolder.getRenderTime()/30f);

            switch(facing){
                case NORTH, WEST:
                    TransformStack.cast(ms).translate(facing.getStepX(), 0, facing.getStepZ());
                case SOUTH, EAST:
                    TransformStack.cast(ms).rotateCentered(Direction.UP, (float) Math.toRadians(facing.toYRot()));
                    break;
            }
            VertexConsumer vbTranslucent = bufferSource.getBuffer(RenderType.translucent());
            SuperByteBuffer beamRender = CachedBufferer.partial(ModPartialModels.LASER_BEAM, be.getBlockState());

            beam.color.setAlpha((int) Mth.clamp(Math.pow(beam.power*20, 2), 100, 200) );
            beamRender.hybridLight()
                      .color(beam.color.getRGB())
                      .renderInto(ms, vbTranslucent);

        }
    }



}
