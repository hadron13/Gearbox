package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import com.jozufozu.flywheel.util.Pair;
import net.minecraft.core.Direction;

public interface LaserReceiver {
    /**
     * @param face  face in which the laser comes from
     * @param color color of the laser
     * @param power power of the laser
     * @return whether the face can receive a laser
     */
    public boolean receiveLaser(Direction face, Color color, float power);
}
