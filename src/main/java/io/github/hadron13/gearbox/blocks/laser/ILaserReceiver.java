package io.github.hadron13.gearbox.blocks.laser;

import net.minecraft.world.phys.Vec3;

public interface ILaserReceiver {
    public void receiveLaser(int color, Vec3 direction, float power);
}
