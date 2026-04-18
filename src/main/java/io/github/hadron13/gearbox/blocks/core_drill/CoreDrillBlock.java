package io.github.hadron13.gearbox.blocks.core_drill;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;

public class CoreDrillBlock extends KineticBlock implements IBE<CoreDrillBlockEntity> {

    public static final Property<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(HORIZONTAL_FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(HORIZONTAL_FACING, context.getHorizontalDirection()
                        .getOpposite());
    }



    public CoreDrillBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemInHand = player.getItemInHand(hand);
        if(itemInHand.getItem() == GearboxItems.CORE_TUBE.get() ){
            withBlockEntityDo(level, pos, (be) ->{
                if(level.isClientSide)
                    return;
                if(be.drillState != CoreDrillBlockEntity.IDLE || !be.poleOffset.settled())
                    return;
                if(!player.isCreative())
                    itemInHand.shrink(1);

                be.switchState(CoreDrillBlockEntity.PUSHING);
                be.sendData();
            });
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis();
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return state.getValue(HORIZONTAL_FACING).getClockWise().getAxis() == face.getAxis();
    }

    @Override
    public Class<CoreDrillBlockEntity> getBlockEntityClass() {
        return CoreDrillBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CoreDrillBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.CORE_DRILL.get();
    }
}
