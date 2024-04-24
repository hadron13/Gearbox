package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.block.state.BlockState;

public class IrradiatorRenderer extends KineticBlockEntityRenderer<IrradiatorBlockEntity> {
    public IrradiatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(IrradiatorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        if(Backend.canUseInstancing(be.getLevel()))
            return;

        float lerpedLensPos =  Mth.lerp(AnimationTickHolder.getPartialTicks(),
                be.previousLensPos,
                be.lensPosition);

        VertexConsumer vb = buffer.getBuffer(RenderType.solid());

        CachedBufferer.partial(ModPartialModels.IRRADIATOR_LENS, be.getBlockState())
                .translate(0, lerpedLensPos, 0)
                .renderInto(ms, vb);

        if(be.totalPower < 0.1f) {
            return;
        }

        vb = buffer.getBuffer(RenderType.translucent());

        float thickness = 0.5f + (lerpedLensPos/0.4f)*1.3f;
        SuperByteBuffer thick_beam = CachedBufferer.partial(ModPartialModels.THICK_BEAM, be.getBlockState());
        thick_beam
                .scale(thickness, 0.5f + be.mode.headOffset, thickness)
                .translate(-0.5f, 0f, -0.5f)
                .color(be.mixedColor.getRGB())
                .renderInto(ms, vb);

    }

    @Override
    protected SuperByteBuffer getRotatedModel(IrradiatorBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP);
    }
}
