package io.github.hadron13.gearbox.blocks.large_laser;

import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.lwjgl.system.CallbackI;

import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.LargeLaserPart.*;

public class LargeLaserBlock extends Block {
    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final Property<LargeLaserPart> PART = EnumProperty.create("part", LargeLaserPart.class);

    public LargeLaserBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        builder.add(PART);
        super.createBlockStateDefinition(builder);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Direction facing = context.getHorizontalDirection().getOpposite();
        LargeLaserPart part = SINGLE;

        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();


        for(Direction direction : Iterate.horizontalDirections){
            BlockState front = level.getBlockState(pos.relative(direction));
            if( !(front.getBlock() instanceof LargeLaserBlock) )
                continue;
            if( front.getValue(HORIZONTAL_FACING).getAxis() != direction.getAxis() )
                continue;

            facing = front.getValue(HORIZONTAL_FACING);
            BlockState back = level.getBlockState(pos.relative(direction.getOpposite()));

            if(countLasers(level, pos, facing) >= 10 || countLasers(level, pos, facing.getOpposite()) >= 10) {
                part = SINGLE;
                break;
            }
            if( !(back.getBlock() instanceof LargeLaserBlock) ||
                  back.getValue(HORIZONTAL_FACING) != facing){
                part = (facing == direction)? BACK : FRONT;
                continue;
            }
            part = MIDDLE;
            break;
        }
        return defaultBlockState()
                .setValue(HORIZONTAL_FACING, facing)
                .setValue(PART, part);
    }

    public static int countLasers(Level world,BlockPos from, Direction direction){
        int number = 0;

        BlockState blockState = world.getBlockState(from.relative(direction));
        for(number = 0; number < 10 &&
                (blockState.getBlock() instanceof LargeLaserBlock); number++){
            blockState = world.getBlockState(from.relative(direction, number + 1 ));
            if(blockState.getValue(PART) == SINGLE)
                break;

        }

        return number;
    }


    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {

        Direction facing = state.getValue(HORIZONTAL_FACING);
        LargeLaserPart part = SINGLE;

        for(Direction direction : Iterate.horizontalDirections){
            BlockState front = worldIn.getBlockState(pos.relative(direction));
            if( !(front.getBlock() instanceof LargeLaserBlock) )
                continue;
            if( front.getValue(HORIZONTAL_FACING).getAxis() != direction.getAxis() )
                continue;

            facing = front.getValue(HORIZONTAL_FACING);
            BlockState back = worldIn.getBlockState(pos.relative(direction.getOpposite()));

            if(countLasers(worldIn, pos, facing) >= 10 || countLasers(worldIn, pos, facing.getOpposite()) >= 10) {
                part = SINGLE;
                break;
            }
            if( !(back.getBlock() instanceof LargeLaserBlock) ||
                    back.getValue(HORIZONTAL_FACING) != facing){
                part = (facing == direction)? BACK : FRONT;
                continue;
            }
            part = MIDDLE;
        }
        BlockState newState = defaultBlockState().setValue(HORIZONTAL_FACING, facing).setValue(PART, part);
//        state.setValue(HORIZONTAL_FACING, facing);
//        state.setValue(PART, part);
        worldIn.setBlock(pos, newState, 1 | 2 );
    }


    public enum LargeLaserPart implements StringRepresentable {
        SINGLE, FRONT, MIDDLE, BACK;
        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }

}
