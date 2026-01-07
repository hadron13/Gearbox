package io.github.hadron13.gearbox.blocks.precision_crank;

import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PrecisionCrankBlockEntity extends HandCrankBlockEntity {
    public PrecisionCrankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean shouldRenderShaft() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public SuperByteBuffer getRenderedHandle() {
        return CachedBuffers.block(getBlockState());
    }
}
