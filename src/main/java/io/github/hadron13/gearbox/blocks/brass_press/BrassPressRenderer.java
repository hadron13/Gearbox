package io.github.hadron13.gearbox.blocks.brass_press;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.backend.Backend;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.backend.SimpleBackend;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class BrassPressRenderer extends KineticBlockEntityRenderer<BrassPressBlockEntity> {

	public BrassPressRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public boolean shouldRenderOffScreen(BrassPressBlockEntity be) {
		return true;
	}

	@Override
	protected void renderSafe(BrassPressBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(be, partialTicks, ms, buffer, light, overlay);

		if (VisualizationManager.supportsVisualization(be.getLevel()))
			return;

		BlockState blockState = be.getBlockState();


		float renderedHeadRotation = be.getRenderedHeadRotation(partialTicks);
		float renderedHeadOffset =
				be.getRenderedHeadOffset(partialTicks) * be.pressingBehaviour.mode.headOffset;

		SuperByteBuffer headRender = CachedBuffers.partialFacing(ModPartialModels.BRASS_PRESS_HEAD, blockState,
				blockState.getValue(HORIZONTAL_FACING));
		SuperByteBuffer poleRender = CachedBuffers.partialFacing(ModPartialModels.BRASS_PRESS_POLE, blockState,
				blockState.getValue(HORIZONTAL_FACING));

		poleRender.translate(0, -renderedHeadOffset, 0)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.solid()));

		headRender.translate(0, -renderedHeadOffset, 0)
				.rotateCentered(renderedHeadRotation * Mth.DEG_TO_RAD, Direction.UP)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.solid()));
	}

	@Override
	protected BlockState getRenderedBlockState(BrassPressBlockEntity be) {
		return shaft(getRotationAxisOf(be));
	}

}
