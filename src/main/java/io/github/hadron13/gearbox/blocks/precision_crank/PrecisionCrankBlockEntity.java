package io.github.hadron13.gearbox.blocks.precision_crank;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlockEntity;
import com.simibubi.create.content.kinetics.crank.ValveHandleVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
