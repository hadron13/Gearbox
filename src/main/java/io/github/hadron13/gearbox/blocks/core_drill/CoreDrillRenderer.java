package io.github.hadron13.gearbox.blocks.core_drill;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

import static io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlock.HORIZONTAL_FACING;

public class CoreDrillRenderer extends KineticBlockEntityRenderer<CoreDrillBlockEntity> {
    public CoreDrillRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(CoreDrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        VertexConsumer solid = buffer.getBuffer(RenderType.solid());
        Direction facing = be.getBlockState().getValue(HORIZONTAL_FACING);

        SuperByteBuffer pole = CachedBuffers.partialFacing(AllPartialModels.MECHANICAL_MIXER_POLE, be.getBlockState(), facing);
        SuperByteBuffer tube = CachedBuffers.partial(ModPartialModels.CORE_DRILL_TUBE, be.getBlockState());


        ms.pushPose();
        TransformStack.of(ms)
            .translate(facing.step().mul(0.5f/16f))
            .rotateCentered(22.5f * Mth.DEG_TO_RAD, facing.getClockWise())
        ;

        float animation_lerp = (be.animationProgress + partialTicks * be.getSpeed())/20f;
        float tube_y_offset = 0;
        float pole_y_offset = 0;

        switch (be.drillState){
            case CoreDrillBlockEntity.IDLE -> {
                tube_y_offset = 0;
            }
            case CoreDrillBlockEntity.PUSHING ->{
                tube_y_offset = Math.min(animation_lerp, 0.5f) * 2 * 20/16f;
                pole_y_offset = animation_lerp < 0.5f? animation_lerp  : 1.0f - animation_lerp;
                pole_y_offset *=  2 * 20/16f;

                tube.light(light)
                    .translate(0, -tube_y_offset, 0)
                    .renderInto(ms, solid);
            }
            case CoreDrillBlockEntity.PULLING -> {

            }
            case CoreDrillBlockEntity.STORING -> {

            }
        }

        pole.light(light)
            .translate(0, -pole_y_offset, 0)
            .translate(0, 11/16f, 0)
            .renderInto(ms, solid);


        for(int i = 0; i < be.tubes; i++){
            tube.light(light)
                .translate(0, (i + 1)*-20/16f -tube_y_offset, 0)
                .renderInto(ms, solid);
        }

        ms.popPose();
    }
}
