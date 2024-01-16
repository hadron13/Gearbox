package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.utility.AnimationTickHolder;


import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModPartialModels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;

public class LaserBeamInstance<T extends SmartBlockEntity> extends BlockEntityInstance<T> implements DynamicInstance {
    Map<Direction, ModelData> beamsData = null;
    public LaserBeamInstance(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);
    }

    public void init(){
        if(beamsData == null) {
            beamsData = new HashMap<>();
        }else {
            remove();
            beamsData.clear();
        }
        LaserBeamBehavior behavior = blockEntity.getBehaviour(LaserBeamBehavior.TYPE);
        if(behavior == null) {
            return;
        }
        for(Direction direction : behavior.beams.keySet()){
            ModelData beam = materialManager.defaultTransparent()
                    .material(Materials.TRANSFORMED)
                    .getModel(ModPartialModels.LASER_BEAM, blockState)
                    .createInstance();

            beamsData.put(direction, beam);
        }
    }
    @Override
    public void beginFrame() {
        LaserBeamBehavior behavior = blockEntity.getBehaviour(LaserBeamBehavior.TYPE);
        if(behavior == null)
            return;
        if(behavior.wrenched){
            init();
            behavior.wrenched = false;
        }
        for(LaserBeamBehavior.LaserBeam beam : behavior.beams.values()) {
            if(!beam.enabled) {
                beamsData.get(beam.facing).setEmptyTransform();
                continue;
            }
            updateBeam(beam);
        }
    }

    public void updateBeam(LaserBeamBehavior.LaserBeam beam){
        Direction facing = beam.facing;
        float scale = beam.length;
        BlockPos origin = beam.origin.subtract(materialManager.getOriginCoordinate());
        ModelData beamData = beamsData.get(facing);

        if(scale == 0) {
            return;
        }

        float xScale = (facing.getAxis() == X)? scale : 1;
        float zScale = (facing.getAxis() == Z)? scale : 1;

        beamData.loadIdentity()
                .scale(xScale, 1, zScale)
                .translate( (origin.getX() + (facing.getAxis() == X? 0.5f:0) )/xScale,
                                origin.getY(),
                            (origin.getZ() + (facing.getAxis() == Z? 0.5f:0) )/zScale  )
                .rotateCentered(facing, AnimationTickHolder.getRenderTime()/30f);

        switch(facing){
            case NORTH, WEST:
                beamData.translate(facing.getStepX(), 0, facing.getStepZ());
            case SOUTH, EAST:
                beamData.rotateCentered(Direction.UP, (float) Math.toRadians(facing.toYRot()));
                break;
        }
        beamData.setColor(beam.color.setAlpha((int)Mth.clamp(Math.pow(beam.power*20, 2), 100, 200)));
    }

    @Override
    public void updateLight() {
        super.updateLight();
        for(ModelData beam : beamsData.values()) {
            relight(15, 15, beam);
        }
    }
    @Override
    protected void remove() {
        for (ModelData beam : beamsData.values()) {
            beam.delete();
        }
    }

}
