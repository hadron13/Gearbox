package io.github.hadron13.gearbox.blocks.large_laser;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.lwjgl.system.CallbackI;

import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.LargeLaserPart.*;

public class LargeLaserBlock extends Block implements IBE<LargeLaserBlockEntity> {
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
    public BlockState laserStateIn(Level level, BlockPos pos, Direction defaultFacing){
        Direction facing = defaultFacing;


        for(Direction direction : Iterate.horizontalDirections) {
            BlockState front = level.getBlockState(pos.relative(direction));
            if( !(front.getBlock() instanceof LargeLaserBlock) )
                continue;
            if( front.getValue(HORIZONTAL_FACING).getAxis() != direction.getAxis() )
                continue;
            if(laserLength(level, pos.relative(direction), direction) >= 10)
                continue;

            facing = front.getValue(HORIZONTAL_FACING);
            break;
        }
        BlockState frontBlock = level.getBlockState(pos.relative(facing));
        BlockState backBlock = level.getBlockState(pos.relative(facing.getOpposite()));

        boolean front = frontBlock.getBlock() instanceof LargeLaserBlock;
        boolean back = backBlock.getBlock() instanceof LargeLaserBlock;

        LargeLaserPart part = SINGLE;
        if(front && back){
            part = MIDDLE;
        }else if(front){
            part = BACK;
        }else if(back){
            part = FRONT;
        }

        return defaultBlockState()
                .setValue(HORIZONTAL_FACING, facing)
                .setValue(PART, part);
    }

    public int laserLength(Level level, BlockPos pos, Direction direction){
        int offset = 0;
        boolean oneEndingMet = false;
        boolean singleMet = false;

        seekLength:
        for(offset = 0; offset < 10; offset++ ){
            BlockState nextLaser = level.getBlockState(pos.relative(direction, offset));

            if( !(nextLaser.getBlock() instanceof LargeLaserBlock)){
                return offset;
            }
            if(nextLaser.getValue(HORIZONTAL_FACING).getAxis() != direction.getAxis())
                return offset + 1 ;

            switch (nextLaser.getValue(PART)){
                case FRONT, BACK -> {
                    if(!oneEndingMet)
                        oneEndingMet = true;
                    else
                        break seekLength;
                }
//                case SINGLE -> {
//                    break seekLength;
//                }
            }
        }
        return offset;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return laserStateIn(context.getLevel(), context.getClickedPos(), context.getHorizontalDirection().getOpposite());

    }
    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos,
                                boolean isMoving) {
        BlockState newState = laserStateIn(worldIn, pos, state.getValue(HORIZONTAL_FACING));
        withBlockEntityDo(worldIn, pos, laser -> laser.neighbourChanged(newState));

        worldIn.setBlock(pos, newState, 1 | 2 );

    }

    @Override
    public Class<LargeLaserBlockEntity> getBlockEntityClass() {
        return LargeLaserBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LargeLaserBlockEntity> getBlockEntityType() {
        return ModBlockEntities.LARGE_LASER.get();
    }


    public enum LargeLaserPart implements StringRepresentable {
        SINGLE, FRONT, MIDDLE, BACK;

//        public boolean isEnd(){
//            return this == FRONT || this == BACK;
//        }
        @Override
        public String getSerializedName() {
            return Lang.asId(name());
        }
    }

}
