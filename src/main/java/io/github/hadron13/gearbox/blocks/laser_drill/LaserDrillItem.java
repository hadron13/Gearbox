package io.github.hadron13.gearbox.blocks.laser_drill;

import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LaserDrillItem extends AssemblyOperatorBlockItem {

    public LaserDrillItem(Block block, Properties builder) {
        super(block, builder);
    }

    @Override
    protected boolean operatesOn(LevelReader world, BlockPos pos, BlockState placedOnState) {
        return true;
    }
}
