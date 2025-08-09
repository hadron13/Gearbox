package io.github.hadron13.gearbox.blocks.mirror;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.ShaftRenderer;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import io.github.hadron13.gearbox.render.ModRenderTypes;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class MirrorRenderer extends KineticBlockEntityRenderer<MirrorBlockEntity> {

    public MirrorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected BlockState getRenderedBlockState(MirrorBlockEntity be) {
        return shaft(getRotationAxisOf(be));
    }


    @Override
    protected void renderSafe(MirrorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
//        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
        BlockState state = getRenderedBlockState(be);
        VertexConsumer solid = buffer.getBuffer(RenderType.solid());
        renderRotatingBuffer(be, getRotatedModel(be, state), ms, solid, light);

        SuperByteBuffer mirror = CachedBuffers.partial(ModPartialModels.MIRROR, state);

        mirror.rotateCentered((be.angle + (partialTicks * be.getSpeed()/8)) * Mth.DEG_TO_RAD, state.getValue(MirrorBlock.AXIS)).light(light).renderInto(ms, solid);


        VertexConsumer laserConsumer = buffer.getBuffer(ModRenderTypes.laserBeam());
        for(Laser l : be.lasers.values()){
            LaserBeamRenderer.renderLaserBeam(l, ms, laserConsumer, be.getBlockPos());
        }
    }
}
