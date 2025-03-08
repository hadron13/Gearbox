package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;


public class ReactorRenderer extends KineticBlockEntityRenderer<ReactorBlockEntity> {
    public ReactorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }


    @Override
    protected void renderSafe(ReactorBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        super.renderSafe(be, partialTicks, ms, bufferSource, light, overlay);


        FluidStack atmosphere = be.atmosphere_tank.getPrimaryHandler().getFluidInTank(0);
        float atm_level = be.atmosphere_tank.getPrimaryTank().getFluidLevel().getValue(partialTicks);


        if(Backend.canUseInstancing(be.getLevel())) {
            FluidRenderer.renderFluidBox(atmosphere, (float) 0, -(float)atm_level, (float) 0, 1.0F, 0.0f, 1.0F, bufferSource, ms, light, true);
            return;
        }


        BlockState blockState = be.getBlockState();

        VertexConsumer vb = bufferSource.getBuffer(RenderType.solid());



        float renderedHeadOffset = 1.1f;
        float speed = be.getRenderedHeadRotationSpeed(partialTicks);
        float time = AnimationTickHolder.getRenderTime(be.getLevel());
        float angle = ((time * speed * 6 / 10f) % 360) / 180 * (float) Math.PI;

        SuperByteBuffer poleRender = CachedBufferer.partial(ModPartialModels.DIPPER_POLE, blockState);
        poleRender.translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vb);

        VertexConsumer vbCutout = bufferSource.getBuffer(RenderType.cutoutMipped());
        SuperByteBuffer headRender = CachedBufferer.partial(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState);
        headRender.rotateCentered(Direction.UP, angle)
                .translate(0, -renderedHeadOffset, 0)
                .light(light)
                .renderInto(ms, vbCutout);

        FluidRenderer.renderFluidBox(atmosphere, (float) 0, -(float)atm_level, (float) 0, 1.0F, 0.0f, 1.0F, bufferSource, ms, light, true);
    }
    @Override
    protected SuperByteBuffer getRotatedModel(ReactorBlockEntity be, BlockState state) {
        return CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, state, Direction.UP);
    }

}
