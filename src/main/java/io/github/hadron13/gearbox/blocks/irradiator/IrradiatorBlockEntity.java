package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IrradiatorBlockEntity extends KineticBlockEntity implements ILaserReceiver {

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {
        return false;
    }
}
