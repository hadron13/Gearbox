package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ElectrolyzerRenderer extends SafeBlockEntityRenderer<ElectrolyzerBlockEntity> {
    public ElectrolyzerRenderer(BlockEntityRendererProvider.Context context) {
        super();
    }

    @Override
    protected void renderSafe(ElectrolyzerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if(Backend.canUseInstancing(be.getLevel()))
            return;


        BlockState blockState = be.getBlockState();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());


        float renderedHeadOffset = be.getRenderedHeadOffset(partialTicks);
        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;

        SuperByteBuffer poleRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_POLE, blockState);
        poleRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        VertexConsumer vbCutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer headRender = CachedBufferer.partial(ModPartialModels.ELECTROLYZER_HEAD, blockState);
        headRender.rotateCentered(Direction.UP, angle)
                .translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vbCutout);
    }
}
