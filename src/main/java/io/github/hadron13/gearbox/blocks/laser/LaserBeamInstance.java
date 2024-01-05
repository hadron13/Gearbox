package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlock;
import io.github.hadron13.gearbox.register.ModPartialModels;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;

public class LaserBeamInstance<T extends BlockEntity & LaserEmitter> extends BlockEntityInstance<T> implements DynamicInstance {
    ModelData beam;
    public LaserBeamInstance(MaterialManager materialManager, T blockEntity) {
        super(materialManager, blockEntity);
        beam = materialManager.defaultTransparent()
                .material(Materials.TRANSFORMED)
                .getModel(ModPartialModels.LASER_BEAM, blockState)
                .createInstance();
    }
    @Override
    public void beginFrame() {
        updateBeam(beam, getInstancePosition(), blockEntity.getDirection());
    }

    public void updateBeam(ModelData beam, BlockPos origin, Direction facing){
        float scale = blockEntity.getLength();

        float xScale = (facing.getAxis() == X)? scale : 1;
        float zScale = (facing.getAxis() == Z)? scale : 1;

        beam.loadIdentity()
                .scale(xScale, 1, zScale)
                .translate( (origin.getX() + (facing.getAxis() == X? 0.5f:0) )/xScale,
                                origin.getY(),
                            (origin.getZ() + (facing.getAxis() == Z? 0.5f:0) )/zScale  )
                .rotateCentered(facing, AnimationTickHolder.getRenderTime()/30f);

        switch(facing){
            case NORTH, WEST:
                beam.translate(facing.getStepX(), 0, facing.getStepZ());
            case SOUTH, EAST:
                beam.rotateCentered(Direction.UP, (float) Math.toRadians(facing.toYRot()));
                break;
        }

        beam.setColor(blockEntity.getColor().setAlpha(200));
    }

    @Override
    public void updateLight() {
        super.updateLight();
        relight(15, 15, beam);
    }
    @Override
    protected void remove() {
        beam.delete();
    }


}
