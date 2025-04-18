package io.github.hadron13.gearbox.blocks.useless_machine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class UselessMachineRenderer extends KineticBlockEntityRenderer<UselessMachineBlockEntity> {
    public UselessMachineRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    protected void renderSafe(UselessMachineBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        int aboveLight = be.getLevel().getLightEmission(be.getBlockPos().above());

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(UselessMachineBlockEntity  be, BlockState state) {
        return CachedBuffers.partial(ModPartialModels.USELESS_COG, state);
    }
}
