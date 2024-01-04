package io.github.hadron13.gearbox.blocks.lasers;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlock;
import io.github.hadron13.gearbox.register.ModPartialModels;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

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
        Direction facing = blockState.getValue(BrassPressBlock.HORIZONTAL_FACING);
    }
    @Override
    public void beginFrame() {
        Direction facing = blockState.getValue(BrassPressBlock.HORIZONTAL_FACING);
        float scale = blockEntity.getLength();

        float angle = 0;
        switch (facing){
            case NORTH -> angle = 0;
            case WEST -> angle = 90;
            case SOUTH -> angle = 180;
            case EAST -> angle = 270;
        }
        float xScale = (facing.getAxis() == X)? scale : 1;
        float zScale = (facing.getAxis() == Z)? scale : 1;

        Vec3i position = getInstancePosition();
        beam.loadIdentity()
                .scale(xScale, 1, zScale)
                .translate( (position.getX() + (facing.getAxis()== X? 0.5f:0) )/xScale,
                                position.getY(),
                            (position.getZ() + (facing.getAxis()== Z? 0.5f:0) )/zScale  )
                .rotateCentered(facing, AnimationTickHolder.getRenderTime()/30f);

        switch(facing){
            case NORTH:
                beam.translate(0, 0, -1);
                break;
            case WEST:
                beam.translate(-1, 0, 0);
            case SOUTH, EAST:
                beam.translate(.5f, .5f, 0.5f)
                    .rotate(Direction.UP, (float) Math.toRadians(angle))
                    .translate(-.5f, -.5f, -0.5f);
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
