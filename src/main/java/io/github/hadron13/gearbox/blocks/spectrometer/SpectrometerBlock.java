package io.github.hadron13.gearbox.blocks.spectrometer;


import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import io.github.hadron13.gearbox.register.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Random;

import static com.simibubi.create.content.kinetics.gauge.GaugeBlock.GAUGE;


public class SpectrometerBlock extends Block implements IBE<SpectrometerBlockEntity> {
    public static final Property<Direction> FACING = BlockStateProperties.FACING;
    public static final BooleanProperty AXIS_ALONG_FIRST_COORDINATE = BooleanProperty.create("axis_along_first");

    public SpectrometerBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALONG_FIRST_COORDINATE);
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getClickedFace();
        boolean alongFirst = false;
        Direction.Axis faceAxis = facing.getAxis();

        if (faceAxis.isVertical()) {
            alongFirst = context.getHorizontalDirection().getAxis() == Direction.Axis.X;
        }
        if(faceAxis == Direction.Axis.Z){
            alongFirst = true;
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(AXIS_ALONG_FIRST_COORDINATE, alongFirst);
    }

    public boolean shouldRenderHeadOnFace(Level world, BlockPos pos, BlockState state, Direction face) {
        if (face.getAxis()
                .isVertical())
            return false;
        if (face == state.getValue(FACING)
                .getOpposite())
            return false;
        if (face.getAxis() == getRotationAxis(state))
            return false;
        if (getRotationAxis(state) == Direction.Axis.Y && face != state.getValue(FACING))
            return false;
        if (!Block.shouldRenderFace(state, world, pos, face, pos.relative(face)) && !(world instanceof WrappedWorld))
            return false;
        return true;
    }
    public Direction.Axis getRotationAxis(BlockState state) {
        Direction.Axis pistonAxis = state.getValue(FACING)
                .getAxis();
        boolean alongFirst = state.getValue(AXIS_ALONG_FIRST_COORDINATE);

        if (pistonAxis == Direction.Axis.X)
            return alongFirst ? Direction.Axis.Y : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Y)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Z)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Y;

        throw new IllegalStateException("Unknown axis??");
    }
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, Random rand) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (be == null || !(be instanceof SpectrometerBlockEntity))
            return;
        SpectrometerBlockEntity gaugeBE = (SpectrometerBlockEntity) be;
        if (gaugeBE.dialTarget == 0)
            return;
        int color = gaugeBE.color;

        for (Direction face : Iterate.directions) {
            if (!shouldRenderHeadOnFace(worldIn, pos, stateIn, face))
                continue;

            Vector3f rgb = new Color(color).asVectorF();
            Vec3 faceVec = Vec3.atLowerCornerOf(face.getNormal());
            Direction positiveFacing = Direction.get(Direction.AxisDirection.POSITIVE, face.getAxis());
            Vec3 positiveFaceVec = Vec3.atLowerCornerOf(positiveFacing.getNormal());
            int particleCount = gaugeBE.dialTarget > 1 ? 4 : 1;

            if (particleCount == 1 && rand.nextFloat() > 1 / 4f)
                continue;

            for (int i = 0; i < particleCount; i++) {
                Vec3 mul = VecHelper.offsetRandomly(Vec3.ZERO, rand, .25f)
                        .multiply(new Vec3(1, 1, 1).subtract(positiveFaceVec))
                        .normalize()
                        .scale(.3f);
                Vec3 offset = VecHelper.getCenterOf(pos)
                        .add(faceVec.scale(.55))
                        .add(mul);
                worldIn.addParticle(new DustParticleOptions(rgb, 1), offset.x, offset.y, offset.z, mul.x, mul.y, mul.z);
            }

        }

    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity be = worldIn.getBlockEntity(pos);
        if (be instanceof GaugeBlockEntity) {
            GaugeBlockEntity gaugeBlockEntity = (GaugeBlockEntity) be;
            return Mth.ceil(Mth.clamp(gaugeBlockEntity.dialTarget * 14, 0, 15));
        }
        return 0;
    }
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return GAUGE.get(state.getValue(FACING), state.getValue(AXIS_ALONG_FIRST_COORDINATE));
    }
    @Override
    public Class<SpectrometerBlockEntity> getBlockEntityClass() {
        return SpectrometerBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SpectrometerBlockEntity> getBlockEntityType() {
        return ModBlockEntities.SPECTROMETER.get();
    }


}