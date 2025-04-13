package io.github.hadron13.gearbox.blocks.compressor;

import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.render.AllInstanceTypes;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class CompressorVisual extends KineticBlockEntityVisual<CompressorBlockEntity> {

    protected final RotatingInstance roll;
    final Direction direction;
    private final Direction opposite;

    public CompressorVisual(VisualizationContext context, CompressorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick);
        direction = blockState.getValue(HORIZONTAL_FACING);
        opposite = direction.getOpposite();

        roll = instancerProvider().instancer(AllInstanceTypes.ROTATING, Models.partial(ModPartialModels.COMPRESSOR_ROLL))
                .createInstance();

        roll.setup(blockEntity, blockEntity.getSpeed())
                .setPosition(getVisualPosition())
                .rotateToFace(Direction.SOUTH, opposite)
                .setChanged();
    }

    @Override
    public void update(float pt) {
        roll.setup(blockEntity, blockEntity.getSpeed())
                .setChanged();
    }

    @Override
    public void updateLight(float partialTick) {
        BlockPos inFront = pos.relative(direction);
        relight(inFront, roll);
    }

    @Override
    protected void _delete() {
        roll.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        consumer.accept(roll);
    }
}
