package io.github.hadron13.gearbox.blocks.large_laser;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.InternalEnergyStorage;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class LargeLaserBlockEntity extends SmartBlockEntity {

    public LaserBeamBehavior beamBehavior;
    public InternalEnergyStorage energyStorage;

    public LargeLaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
    }


}
