package io.github.hadron13.gearbox.blocks.compressor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.processing.basin.BasinBlock;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.data.IntAttached;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;


import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CompressorRenderer extends KineticBlockEntityRenderer<CompressorBlockEntity> {

    public CompressorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRenderOffScreen(CompressorBlockEntity be) {
        return true;
    }

    //@Override
    //protected SuperByteBuffer getRotatedModel(CompressorBlockEntity be, BlockState state) {
    //    Direction dir = state.getValue(CompressorBlock.HORIZONTAL_FACING);
    //    PoseStack transform = CachedBuffers.rotateToFaceVertical(dir).get();
    //    return SuperBufferFactory.getInstance().createForBlock(Models.partial(ModPartialModels.COMPRESSOR_ROLL), Blocks.AIR.defaultBlockState(), transform);
    //}

    @Override
    protected void renderSafe(CompressorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.getBlockState();
        ms = CachedBuffers.rotateToFaceVertical(blockState.getValue(HORIZONTAL_FACING)).get();

        VertexConsumer solid = buffer.getBuffer(RenderType.solid());
        if(!VisualizationManager.supportsVisualization(be.getLevel())) {
            SuperByteBuffer roll = CachedBuffers.partialFacing(ModPartialModels.COMPRESSOR_ROLL, blockState, blockState.getValue(HORIZONTAL_FACING));
            standardKineticRotationTransform(roll, be, light);
            roll.renderInto(ms, solid);
        }

        Direction direction = blockState.getValue(HORIZONTAL_FACING);

        Vec3 directionVec = Vec3.atLowerCornerOf(direction.getNormal());
        Vec3 outVec = VecHelper.getCenterOf(BlockPos.ZERO)
                .add(directionVec.scale(.55)
                        .subtract(0, 1 / 2f, 0));

        boolean outToBasin = be.getLevel()
                .getBlockState(be.getBlockPos()
                        .relative(direction))
                .getBlock() instanceof BasinBlock;

        for (IntAttached<ItemStack> intAttached : be.visualizedOutputItems) {
            float progress = 1 - (intAttached.getFirst() - partialTicks) / CompressorBlockEntity.OUTPUT_ANIMATION_TIME;

            if (!outToBasin && progress > .35f)
                continue;

            ms.pushPose();
            TransformStack.of(ms)
                    .translate(outVec)
                    .translate(new Vec3(0, Math.max(-.55f, -(progress * progress * 2)), 0))
                    .translate(directionVec.scale(progress * .5f))
                    .rotateY(AngleHelper.horizontalAngle(direction))
                    .rotateX(progress * 180);
            renderItem(ms, buffer, light, overlay, be.getLevel(), intAttached.getValue());
            ms.popPose();
        }
        if (VisualizationManager.supportsVisualization(be.getLevel()))
            return;
    }

    protected void renderItem(PoseStack ms, MultiBufferSource buffer, int light, int overlay, Level level, ItemStack stack) {
        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer, level, 0);
    }

    @Override
    protected BlockState getRenderedBlockState(CompressorBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }


}
