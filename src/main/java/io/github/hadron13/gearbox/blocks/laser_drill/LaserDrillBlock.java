package io.github.hadron13.gearbox.blocks.laser_drill;

import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import io.github.hadron13.gearbox.register.ModShapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LaserDrillBlock extends Block implements IBE<LaserDrillBlockEntity> {

    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn,
                              BlockPos pos) {
        return worldIn.getBlockState(pos.below()).isAir();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn,
                               BlockPos pos, CollisionContext context) {
        return ModShapes.LASER_DRILL.get(Direction.Axis.Y);
    }
    public LaserDrillBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public Class<LaserDrillBlockEntity> getBlockEntityClass() {
        return LaserDrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LaserDrillBlockEntity> getBlockEntityType() {
        return ModBlockEntities.LASER_DRILL.get();
    }
}
