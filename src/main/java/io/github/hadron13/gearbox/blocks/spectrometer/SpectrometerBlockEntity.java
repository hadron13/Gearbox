package io.github.hadron13.gearbox.blocks.spectrometer;

import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.LaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SpectrometerBlockEntity extends GaugeBlockEntity {
    public SpectrometerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
}
