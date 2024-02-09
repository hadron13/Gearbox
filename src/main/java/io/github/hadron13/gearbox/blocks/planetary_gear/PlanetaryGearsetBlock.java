package io.github.hadron13.gearbox.blocks.planetary_gear;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;


//  Stolen carefully  from
//
//  -* Petrolpark *-
//
//  link: https://github.com/petrolpark/Destroy/blob/1.20.1/src/main/java/com/petrolpark/destroy/block/PlanetaryGearsetBlock.java
public class PlanetaryGearsetBlock extends CogWheelBlock {

    public static final BooleanProperty SUPPORT_ONLY = BooleanProperty.create("support_only");

    public PlanetaryGearsetBlock(Properties properties) {
        super(true, properties);
        registerDefaultState(defaultBlockState().setValue(SUPPORT_ONLY, false));
    };

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SUPPORT_ONLY);
        super.createBlockStateDefinition(builder);
    };

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(SUPPORT_ONLY, true);
    };

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!state.getValue(SUPPORT_ONLY)) level.setBlockAndUpdate(pos, state.setValue(SUPPORT_ONLY, true));
        super.onPlace(state, level, pos, oldState, isMoving);
    };

//    @Override
//    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
//        return DestroyShapes.PLANETARY_GEARSET.get(state.getValue(RotatedPillarKineticBlock.AXIS));
//    };

    @Override
    public boolean isLargeCog() {
        return true;
    };

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return ModBlockEntities.PLANETARY_GEARSET.get();
    };

}
