package io.github.hadron13.gearbox.blocks.sapper;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.core.Direction.*;
import static net.minecraft.core.Direction.SOUTH;

public class SapperRenderer extends KineticBlockEntityRenderer<SapperBlockEntity> {

    public SapperRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(SapperBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {

        if (Backend.canUseInstancing(be.getLevel())) return;

        BlockState blockState = be.getBlockState();
        Direction facing = blockState.getValue(HORIZONTAL_FACING);

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        SuperByteBuffer cogModel = CachedBufferer.partialDirectional(
                AllPartialModels.SHAFTLESS_COGWHEEL, blockState, facing, () -> {
            PoseStack poseStack = new PoseStack();
            TransformStack.cast(poseStack)
                    .centre()
                    .rotateToFace(facing)
                    .multiply(Vector3f.XN.rotationDegrees(90))
                    .unCentre();
            return poseStack;
        });

        standardKineticRotationTransform(cogModel, be, light).renderInto(ms, vb);

        float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;

        int x_multiplier = (facing==WEST)?  1 : (facing==EAST)?  -1 : 0;
        int z_multiplier = (facing==NORTH)? 1 : (facing==SOUTH)? -1 : 0;

        SuperByteBuffer poleRender = CachedBufferer.partialFacing(ModPartialModels.SAPPER_POLE, blockState, facing.getOpposite());
        poleRender.translate(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier)
                .light(light)
                .renderInto(ms, vb);

        VertexConsumer vbCutout = buffer.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer headRender = CachedBufferer.partialFacing(ModPartialModels.SAPPER_HEAD, blockState, facing.getOpposite());
        headRender.rotateCentered(facing.getOpposite(), angle)
                .translate(renderedHeadOffset * x_multiplier ,  0 , renderedHeadOffset * z_multiplier)
                .light(light)
                .renderInto(ms, vbCutout);

    }

}