package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.api.event.BlockEntityBehaviourEvent;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements LaserEmitter {

    LaserBeamBehavior beamBehavior;
    public int redstoneSignal = 0;
    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.facing = getBlockState().getValue(HORIZONTAL_FACING);
        beamBehavior.origin = getBlockPos();
        beamBehavior.color = Color.RED;
        beamBehavior.power = 0.5f;
    }

    public void neighbourChanged(){
        if(level == null)
            return;
        redstoneSignal = level.getBestNeighborSignal(worldPosition);
        sendData();
    }

    @Override
    public void tick(){
        super.tick();

        beamBehavior.color = Color.rainbowColor(redstoneSignal * 100);
    }

    @Override
    public Direction getDirection() {
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    public Color getColor() {
        return beamBehavior.color;
    }
    @Override
    public float getPower() {
        return beamBehavior.power;
    }

    @Override
    public float getLength() {
        return beamBehavior.length;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("signal", redstoneSignal);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        redstoneSignal = compound.getInt("signal");
        super.read(compound, clientPacket);
    }

}
