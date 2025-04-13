package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import io.github.hadron13.gearbox.Gearbox;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    LaserBeamBehavior beamBehavior;

    public AABB renderBoundingBox;
    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;
    boolean noEnergyMode = false;
    public int redstoneSignal = 0;
    boolean firstTick = true;

    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(1000, 50, 50);
        lazyEnergy = LazyOptional.of(() -> energyStorage);
    }

    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
        }
        return renderBoundingBox;
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours){
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getFacing(), getBlockPos(), Color.RED, 2.0f);
        if(!noEnergyMode)
            beamBehavior.getLaser(getFacing()).enabled = false;
    }

    public void neighbourChanged(){
        if(level == null)
            return;
        redstoneSignal = level.getBestNeighborSignal(worldPosition);
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());
        if(beam == null)
            return;
        beam.color = Color.rainbowColor(redstoneSignal*(1536/15));
        if(redstoneSignal == 10){
            beam.color.setValue(0x0000FF);
        }else if (redstoneSignal == 5){
            beam.color.setValue(0x00FF00);
        }
        sendData();
    }

    @Override
    public void tick(){
        super.tick();

        if(firstTick) {
            neighbourChanged();

            renderBoundingBox = new AABB(worldPosition, worldPosition.relative(getBlockState().getValue(HORIZONTAL_FACING), LaserBeamBehavior.MAX_LENGTH).offset(1, 1, 1));
            firstTick = false;
        }
        if(level.isClientSide)
            return;

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(facing);

        if(noEnergyMode)
            return;

        if(beam.enabled) {
            int ext = energyStorage.internalConsumeEnergy(100);
            if (ext < 100) {
                beam.enabled = false;
                sendData();
            }
        }else{
            if(energyStorage.getEnergyStored() > 100){
                beam.enabled = true;
                sendData();
            }
        }
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        InternalEnergyStorage.energyConsumptionTooltip(tooltip, energyStorage.getEnergyStored() > 0? 100:0);
        return true;
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
        if (cap == ForgeCapabilities.ENERGY && side == getBlockState().getValue(HORIZONTAL_FACING).getOpposite())// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }

}
