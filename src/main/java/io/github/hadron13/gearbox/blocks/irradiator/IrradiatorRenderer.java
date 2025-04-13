package io.github.hadron13.gearbox.blocks.irradiator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.gearbox.register.ModPartialModels;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
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

        float lerpedLensPos =  Mth.lerp(AnimationTickHolder.getPartialTicks(),
                be.previousLensPos,
                be.lensPosition);

        if(be.totalPower < 0.1f) {
            return;
        }

        VertexConsumer vb = buffer.getBuffer(ModRenderTypes.laserBeam());

        float thickness = 0.5f + (lerpedLensPos/0.4f)*1.3f;
        SuperByteBuffer thick_beam = CachedBuffers.partial(ModPartialModels.THICK_BEAM, be.getBlockState());
        thick_beam
                .translate(0.5f - thickness/2.0f, -be.mode.headOffset, 0.5f - thickness/2.0f)
                .scale(thickness, 0.5f + be.mode.headOffset, thickness)
                //.translate(-0.5f, 0f, -0.5f)
                .color(be.mixedColor.getRGB())
                .renderInto(ms, vb);


        if(VisualizationManager.supportsVisualization(be.getLevel()))
            return;


        vb = buffer.getBuffer(RenderType.solid());


        CachedBuffers.partial(ModPartialModels.IRRADIATOR_LENS, be.getBlockState())
                .translate(0, lerpedLensPos, 0)
                .renderInto(ms, vb);



    }

    @Override
    protected SuperByteBuffer getRotatedModel(IrradiatorBlockEntity be, BlockState state) {
        return CachedBuffers.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP);
    }
}
