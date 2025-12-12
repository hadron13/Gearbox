package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements ILaserEmitter,IHaveGoggleInformation {
    public Laser laserBeam;

    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;

    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(8192, 256, 0);
        lazyEnergy = LazyOptional.of(() -> energyStorage);

        laserBeam = new Laser(0xFFFFFF, getBlockPos().getCenter(), new Vec3(getFacing().step()));
        laserBeam.disable();
    }

    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(100);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours){
    }
    @Override
    public void tick(){
        super.tick();

        if(level != null) {
            laserBeam.tick(level);
        }


        if(level.isClientSide)
            return;

        if(laserBeam.enabled && !isVirtual()) {
            int consumed = energyStorage.internalConsumeEnergy(128);
            if(consumed < 128)  laserBeam.disable();
            sendData();
        }else{
            if(energyStorage.getEnergyStored() > 200) {
                laserBeam.enable();
                sendData();
            }
        }
        laserBeam.setEnabled(energyStorage.getEnergyStored() > 0);

    }

    public void ponderEnableLaser(){
        laserBeam.enable();
        laserBeam.tick(getLevel());
    }

    @Override
    public void remove() {
        lazyEnergy.invalidate();
        laserBeam.disable();
        super.remove();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        energyStorage.storedEnergyTooltip(tooltip);
        InternalEnergyStorage.energyConsumptionTooltip(tooltip, energyStorage.getEnergyStored() > 0? 128 : 0 );
        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        energyStorage.write(compound);
        compound.putBoolean("enabled", laserBeam.enabled);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        energyStorage.read(compound);
        laserBeam.setEnabled(compound.getBoolean("enabled"));
        super.read(compound, clientPacket);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && (side == null || side == getBlockState().getValue(HORIZONTAL_FACING).getOpposite()))// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }

    @Override
    public List<Laser> getLasers() {
        return List.of(laserBeam);
    }
}
