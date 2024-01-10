package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity {

    LaserBeamBehavior beamBehavior;
    public int redstoneSignal = 0;
    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getBlockState().getValue(HORIZONTAL_FACING), getBlockPos(), Color.RED, 1.0f);
        Gearbox.LOGGER.debug("comportamentado");
    }

    public void neighbourChanged(){
        if(level == null)
            return;
        redstoneSignal = level.getBestNeighborSignal(worldPosition);
        beamBehavior.getLaser(getBlockState().getValue(HORIZONTAL_FACING)).color = Color.rainbowColor(redstoneSignal*(1536/15));
        sendData();
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("signal", redstoneSignal);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        redstoneSignal = compound.getInt("signal");
    }

}
