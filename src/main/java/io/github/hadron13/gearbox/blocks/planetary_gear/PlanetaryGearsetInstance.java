package io.github.hadron13.gearbox.blocks.planetary_gear;

import com.jozufozu.flywheel.api.InstanceData;
import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.AxisDirection;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;

public class PlanetaryGearsetInstance extends KineticBlockEntityInstance<PlanetaryGearsetBlockEntity> {

    protected final RotatingData ringGear;
    protected final RotatingData sunGear;
    protected final EnumMap<Direction, RotatingData> keys;

    public PlanetaryGearsetInstance(MaterialManager materialManager, PlanetaryGearsetBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        BlockState blockState = blockEntity.getBlockState();
        Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        int blockLight = world.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = world.getBrightness(LightLayer.SKY, pos);

        ringGear = getRotatingMaterial().getModel(ModPartialModels.PG_RING_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> PlanetaryGearsetRenderer.rotateToAxis(axis))
                .createInstance();
        ringGear
                .setRotationAxis(axis)
                .setRotationOffset(getRotationOffset(axis)).setColor(blockEntity)
                .setRotationalSpeed(getBlockEntitySpeed())
                .setPosition(getInstancePosition())
                .setBlockLight(blockLight)
                .setSkyLight(skyLight);

        sunGear = getRotatingMaterial().getModel(ModPartialModels.PG_SUN_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> PlanetaryGearsetRenderer.rotateToAxis(axis))
                .createInstance();
        sunGear
                .setRotationAxis(axis)
                .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
                .setRotationalSpeed(-2 * getBlockEntitySpeed())
                .setPosition(getInstancePosition())
                .setBlockLight(blockLight)
                .setSkyLight(skyLight);

        keys = new EnumMap<>(Direction.class);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == axis) continue;

            Instancer<RotatingData> planetGear = getRotatingMaterial()
                    .getModel(ModPartialModels.PG_PLANET_GEAR, blockState, Direction.get(AxisDirection.POSITIVE, axis), () -> PlanetaryGearsetRenderer.rotateToAxis(axis));

            RotatingData key = planetGear.createInstance();

            Vector3f position = new Vector3f(getInstancePosition().getX(), getInstancePosition().getY(), getInstancePosition().getZ());
            Vector3f translation = direction.step();
            translation.mul(6.25f / 16f);

            position.add(translation);

            key
                    .setRotationAxis(axis)
                    .setRotationalSpeed(2 * getBlockEntitySpeed())
                    .setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos)).setColor(blockEntity)
                    .setPosition(position)
                    .setBlockLight(blockLight)
                    .setSkyLight(skyLight);

            keys.put(direction, key);
        };
    };

    @Override
    public void update() {
        Direction.Axis axis = KineticBlockEntityRenderer.getRotationAxisOf(blockEntity);
        updateRotation(ringGear, axis, getBlockEntitySpeed());
        updateRotation(sunGear, axis, -2 * getBlockEntitySpeed());
        sunGear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
        keys.values().forEach(gear -> {
            updateRotation(gear, axis, 2 * getBlockEntitySpeed());
            gear.setRotationOffset(BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, pos));
        });
    };

    @Override
    public void updateLight() {
        relight(pos, ringGear, sunGear);
        relight(pos, keys.values().stream());
    };

    @Override
    protected void remove() {
        ringGear.delete();
        sunGear.delete();
        keys.values().forEach(InstanceData::delete);
        keys.clear();
    };
}
