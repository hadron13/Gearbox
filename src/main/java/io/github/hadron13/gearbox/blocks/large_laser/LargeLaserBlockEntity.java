package io.github.hadron13.gearbox.blocks.large_laser;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.InternalEnergyStorage;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.LaserBlock;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.lwjgl.system.CallbackI;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.HORIZONTAL_FACING;
import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.LargeLaserPart.*;
import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.PART;

public class LargeLaserBlockEntity extends SmartBlockEntity {

    public LaserBeamBehavior beamBehavior;
    public InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;
    public int redstoneSignal = 0;
    public BlockPos back;
    boolean firstTick = true;
    public LaserBeamBehavior.LaserBeam mainLaser;

    public LargeLaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean isFront(){
        return getBlockState().getValue(PART) == FRONT || getBlockState().getValue(PART) == SINGLE;
    }
    public boolean isBack(){
        return getBlockState().getValue(PART) == BACK || getBlockState().getValue(PART) == SINGLE;
    }
    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getFacing(), getBlockPos(), Color.RED, 20.0f);
//        beamBehavior.getLaser(getFacing()).enabled = false;
    }
    @Override
    public void tick(){
        super.tick();
        if(level == null || level.isClientSide)
            return;
        if(!isFront()) {
            return;
        }
        if(firstTick){
            neighbourChanged(getBlockState());
            firstTick = false;
        }

        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());

    }

    @Override
    public void lazyTick(){

    }

    public void neighbourChanged(BlockState newState){
        if(level == null)
            return;


        if(newState.getValue(PART) != FRONT && newState.getValue(PART) != SINGLE){
            beamBehavior.getLaser(getFacing()).enabled = false;
            sendData();
            return;
        }
        if(newState.getValue(HORIZONTAL_FACING) != getFacing()){
            beamBehavior.beams.clear();
            beamBehavior.addLaser(newState.getValue(HORIZONTAL_FACING), getBlockPos(), Color.BLACK, 20f);
            beamBehavior.wrenched = true;
        }

        redstoneSignal = level.getBestNeighborSignal(worldPosition);
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(newState.getValue(HORIZONTAL_FACING));

        beam.enabled = true;
        beam.color = Color.rainbowColor(redstoneSignal*(1536/15));
        if(redstoneSignal == 10){
            beam.color.setValue(0x0000FF);
        }else if (redstoneSignal == 5){
            beam.color.setValue(0x00FF00);
        }
        sendData();
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
        if (cap == CapabilityEnergy.ENERGY && isBack() && side == getFacing().getOpposite())// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }



}
