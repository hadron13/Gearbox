package io.github.hadron13.gearbox.blocks.large_laser;

import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class LargeLaserRenderer extends LaserBeamRenderer<LargeLaserBlockEntity> {
    public LargeLaserRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
}
