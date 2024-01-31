package io.github.hadron13.gearbox.blocks.large_laser;

import com.jozufozu.flywheel.api.MaterialManager;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamInstance;

public class LargeLaserInstance extends LaserBeamInstance<LargeLaserBlockEntity> {
    public LargeLaserInstance(MaterialManager materialManager, LargeLaserBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }
}
