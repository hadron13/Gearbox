package io.github.hadron13.gearbox.blocks.irradiator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
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
import net.minecraft.world.phys.Vec3;

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

        VertexConsumer vb;

        if(be.receivingLaser != null && be.receivingLaser.getPower() > 0.01f){
            vb = buffer.getBuffer(ModRenderTypes.laserBeam());
            float thickness = lerpedLensPos;
            float length = be.mode == PressingBehaviour.Mode.BELT? 1.70f : 2.0f;
            if(thickness > 0.01)
                LaserBeamRenderer.renderLaserBeamCustom(thickness, thickness + 2/16f, length, be.receivingLaser.getColor() | 0x55000000, new Vec3(0, -1, 0), new Vec3(0, 0, 0), ms, buffer, partialTicks);
        }

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
