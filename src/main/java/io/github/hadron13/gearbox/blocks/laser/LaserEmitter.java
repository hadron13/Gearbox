package io.github.hadron13.gearbox.blocks.laser;
import com.jozufozu.flywheel.util.Color;
import net.minecraft.core.Direction;

public interface LaserEmitter {
    public Direction getDirection();
    public Color getColor();
    public float getPower();
    public float getLength();
}
