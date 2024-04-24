package io.github.hadron13.gearbox.blocks.laser_drill;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public class LaserDrillBlock extends Block{

    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public LaserDrillBlock(Properties pProperties) {
        super(pProperties);
    }


}
