package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.*;
import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class MirrorBlock extends RotatedPillarKineticBlock implements IBE<MirrorBlockEntity>, IWrenchable {

    public MirrorBlock(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public Class<MirrorBlockEntity> getBlockEntityClass() {
        return MirrorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MirrorBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.MIRROR.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(AXIS);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.getValue(AXIS);
    }
}
