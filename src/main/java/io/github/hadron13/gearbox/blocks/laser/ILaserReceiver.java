package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import net.minecraft.core.Direction;

public interface ILaserReceiver {
    /**
     * @param face  face in which the laser comes from
     * @param color color of the laser
     * @param power power of the laser
     * @return whether the face can receive a laser
     */
    public boolean receiveLaser(Direction face, Color color, float power);

    /**
     * same as receiveLaser, but recursive
     * @param face face in which the laser comes from
     * @param color color of the laser
     * @param power power of the laser
     * @param index recursion limit so your stack turn into the website
     */
    public default void propagate(Direction face, Color color, float power, int index){}
    public default float getLoss(){
        return 0;
    }
}
