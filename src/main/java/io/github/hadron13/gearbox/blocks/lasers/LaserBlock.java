package io.github.hadron13.gearbox.blocks.lasers;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlockEntity;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaserBlock extends Block implements IBE<LaserBlockEntity> {

    public LaserBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public Class<LaserBlockEntity> getBlockEntityClass() {
        return LaserBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LaserBlockEntity> getBlockEntityType() {
//        return ModBlockEntities.LASER.get();
        return null;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}
