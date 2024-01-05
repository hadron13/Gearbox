package io.github.hadron13.gearbox.blocks.mirror;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.LaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.LaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MirrorBlockEntity extends SmartBlockEntity implements LaserReceiver, LaserEmitter {


    public MirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public Direction getDirection() {
        return null;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public float getPower() {
        return 0;
    }

    @Override
    public float getLength() {
        return 0;
    }


    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {
        return false;
    }
}
