package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {


    public ArrayList<Laser> lasers;


    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;

    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(1000, 50, 50);
        lazyEnergy = LazyOptional.of(() -> energyStorage);
    }

    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public AABB getRenderBoundingBox() {
//        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());
//        if (renderBoundingBox == null) {
//            renderBoundingBox = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
//        }
//        return renderBoundingBox;
//    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours){
    }

    public void neighbourChanged(){

    }

    @Override
    public void tick(){

    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
//        InternalEnergyStorage.energyConsumptionTooltip(tooltip, energyStorage.getEnergyStored() > 0? 100:0);
        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && side == getBlockState().getValue(HORIZONTAL_FACING).getOpposite())// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }

}
