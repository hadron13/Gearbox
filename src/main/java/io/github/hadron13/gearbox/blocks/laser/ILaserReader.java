package io.github.hadron13.gearbox.blocks.laser;

import net.createmod.catnip.theme.Color;
import net.minecraft.core.Direction;

/**
 * interface for reading laser values while letting it pass through
 */
public interface ILaserReader {
    /**
     * @brief method called every frame by each passing laser
     * @param laser laser that called this method
     * @return whether the laser was blocked
     */
    public boolean receiveLaser(Laser laser);

}
