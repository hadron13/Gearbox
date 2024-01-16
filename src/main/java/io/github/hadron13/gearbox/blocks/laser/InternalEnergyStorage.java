package io.github.hadron13.gearbox.blocks.laser;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.EnergyStorage;


//carefully stolen from https://github.com/mrh0/createaddition/blob/1.18/src/main/java/com/mrh0/createaddition/energy/InternalEnergyStorage.java
public class InternalEnergyStorage extends EnergyStorage {

    public InternalEnergyStorage(int capacity) {
        super(capacity, capacity, capacity, 0);
    }

    public InternalEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer, maxTransfer, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
    }

    public InternalEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
        super(capacity, maxReceive, maxExtract, energy);
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("energy", energy);
        return nbt;
    }

    public void read(CompoundTag nbt) {
        setEnergy(nbt.getInt("energy"));
    }

    public CompoundTag write(CompoundTag nbt, String name) {
        nbt.putInt("energy_"+name, energy);
        return nbt;
    }

    public void read(CompoundTag nbt, String name) {
        setEnergy(nbt.getInt("energy_"+name));
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    public int internalConsumeEnergy(int consume) {
        int oenergy = energy;
        energy = Math.max(0, energy - consume);
        return oenergy - energy;
    }

    public int internalProduceEnergy(int produce) {
        int oenergy = energy;
        energy = Math.min(capacity, energy + produce);
        return oenergy - energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
