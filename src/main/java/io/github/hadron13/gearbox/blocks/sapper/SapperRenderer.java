package io.github.hadron13.gearbox.blocks.sapper;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class SapperRenderer extends KineticBlockEntityRenderer<SapperBlockEntity> {

    public SapperRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

//    @Override
//    protected SuperByteBuffer getRotatedModel(SapperBlockEntity be, BlockState state) {
//        return CachedBufferer.partialFacing(AllPartialModels.SHAFTLESS_COGWHEEL, state,
//                state.getValue(HORIZONTAL_FACING));
//    }

    @Override
    protected void renderSafe(SapperBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel())) return;

        BlockState blockState = be.getBlockState();

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        SuperByteBuffer superBuffer = CachedBufferer.partialFacing( AllPartialModels.SHAFTLESS_COGWHEEL, blockState,
                                                                    blockState.getValue(HORIZONTAL_FACING));
        standardKineticRotationTransform(superBuffer, be, light).renderInto(ms, vb);

//        float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
//        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
//        float time = AnimationTickHolder.getRenderTime(be.getLevel());
//        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;
//
//        SuperByteBuffer poleRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_POLE, blockState);
//        poleRender.translate(0, -renderedHeadOffset, 0)
//                .light(light)
//                .renderInto(ms, vb);
//
//        VertexConsumer vbCutout = buffer.getBuffer(RenderType.cutoutMipped());
//        SuperByteBuffer headRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState);
//        headRender.rotateCentered(Direction.UP, angle)
//                .translate(0, -renderedHeadOffset, 0)
//                .light(light)
//                .renderInto(ms, vbCutout);
    }

}