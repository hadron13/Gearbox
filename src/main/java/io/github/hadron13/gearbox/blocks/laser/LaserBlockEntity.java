package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;


import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements ILaserEmitter,IHaveGoggleInformation {
    public Laser laserBeam;

    public final InternalEnergyStorage energyStorage;

    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(8192, 256, 0);

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
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        energyStorage.write(tag);
        tag.putBoolean("enabled", laserBeam.enabled);
        super.write(tag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        energyStorage.read(tag);
        laserBeam.setEnabled(tag.getBoolean("enabled"));
        super.read(tag, registries, clientPacket);
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                GearboxBlockEntities.LASER.get(),
                (be, context) -> {
                    if (context == null || context == be.getBlockState().getValue(HORIZONTAL_FACING).getOpposite()){
                        return be.energyStorage;
                    }
                    return null;
                }
        );
    }

    @Override
    public List<Laser> getLasers() {
        return List.of(laserBeam);
    }
}
