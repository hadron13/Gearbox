package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;


import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModPartialModels;

import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;

public class LaserBeamInstance<T extends SmartBlockEntity> extends AbstractBlockEntityVisual<T> implements SimpleDynamicVisual {
    public Map<Direction, TransformedInstance> beamsData = null;
    public LaserBeamInstance(VisualizationContext context, T blockEntity, float partialTicks) {
        super(context, blockEntity, partialTicks);
    }

    public void init(){

        if(beamsData == null) {
            beamsData = new HashMap<>();
        }else {
            _delete();
            beamsData.clear();
        }
        LaserBeamBehavior behavior = blockEntity.getBehaviour(LaserBeamBehavior.TYPE);
        if(behavior == null) {
            return;
        }
        for(Direction direction : behavior.beams.keySet()){
            TransformedInstance beam = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(ModPartialModels.LASER_BEAM)).createInstance();

            beamsData.put(direction, beam);
        }
    }
    @Override
    public void beginFrame(DynamicVisual.Context ctx) {
        LaserBeamBehavior behavior = blockEntity.getBehaviour(LaserBeamBehavior.TYPE);
        if(behavior == null)
            return;
        if(behavior.wrenched){
            init();
            behavior.wrenched = false;
        }
        for(LaserBeamBehavior.LaserBeam beam : behavior.beams.values()) {
            if(!beam.enabled) {
                beamsData.get(beam.facing).setZeroTransform();
                continue;
            }
            updateBeam(beam);
        }
    }

    public void updateBeam(LaserBeamBehavior.LaserBeam beam){
        Direction facing = beam.facing;
        float scale = beam.length;
        BlockPos origin = beam.origin.subtract(visualizationContext.renderOrigin());
        TransformedInstance beamData = beamsData.get(facing);

        if(scale == 0) {
            return;
        }

        float xScale = (facing.getAxis() == X)? scale : 1;
        float zScale = (facing.getAxis() == Z)? scale : 1;

        beamData.setIdentityTransform()
                .scale(xScale, 1, zScale)
                .translate( (origin.getX() + (facing.getAxis() == X? 0.5f:0) )/xScale,
                                origin.getY(),
                            (origin.getZ() + (facing.getAxis() == Z? 0.5f:0) )/zScale  )
                .rotateCentered(AnimationTickHolder.getRenderTime()/30f, facing);

        switch(facing){
            case NORTH, WEST:
                beamData.translate(facing.getStepX(), 0, facing.getStepZ());
            case SOUTH, EAST:
                beamData.rotateCentered((float) Math.toRadians(facing.toYRot()), Direction.UP);
                break;
        }
        beamData.colorRgb(beam.color.setAlpha((int)Mth.clamp(Math.pow(beam.power*20, 2), 100, 200)).getRGB());
    }

    @Override
    public void updateLight(float partialTick) {
        for(TransformedInstance beam : beamsData.values()) {
            relight(beam);
        }
    }
    @Override
    protected void _delete() {
        for (TransformedInstance beam : beamsData.values()) {
            beam.delete();
        }
    }

    @Override
    public void collectCrumblingInstances(Consumer<@Nullable Instance> consumer) {

    }
}
