package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity {

    LaserBeamBehavior beamBehavior;
    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;
    public int redstoneSignal = 0;
    boolean firstTick = true;
    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(100, 10, 10);
        lazyEnergy = LazyOptional.of(() -> energyStorage);

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getBlockState().getValue(HORIZONTAL_FACING), getBlockPos(), Color.RED, 2.0f);
    }

    public void neighbourChanged(){
        if(level == null)
            return;
        redstoneSignal = level.getBestNeighborSignal(worldPosition);
        beamBehavior.getLaser(getBlockState().getValue(HORIZONTAL_FACING)).color = Color.rainbowColor(redstoneSignal*(1536/15));
        if(redstoneSignal == 10){
            beamBehavior.getLaser(getBlockState().getValue(HORIZONTAL_FACING)).color.setValue(0x0000FF);
        }else if (redstoneSignal == 5){
            beamBehavior.getLaser(getBlockState().getValue(HORIZONTAL_FACING)).color.setValue(0x00FF00);
        }
        sendData();
    }

    @Override
    public void tick(){
        super.tick();
        if(firstTick) {
            neighbourChanged();
            firstTick = false;
        }
        if(level.isClientSide)
            return;

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(facing);

        //uncomment on release!!

//        if(beam.enabled) {
//            int ext = energyStorage.internalConsumeEnergy(10);
//            if (ext < 10) {
//                beam.enabled = false;
//                sendData();
//            }
//        }else{
//            if(energyStorage.getEnergyStored() > 10){
//                beam.enabled = true;
//                sendData();
//            }
//        }
//


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

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityEnergy.ENERGY && side == getBlockState().getValue(HORIZONTAL_FACING).getOpposite())// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }

}
