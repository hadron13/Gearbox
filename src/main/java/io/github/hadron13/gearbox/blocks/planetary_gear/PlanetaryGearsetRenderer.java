package io.github.hadron13.gearbox.blocks.planetary_gear;

import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;


//  Stolen carefully  from
//
//  -* Petrolpark *-
//
//  link: https://github.com/petrolpark/Destroy/blob/1.20.1/src/main/java/com/petrolpark/destroy/block/renderer/PlanetaryGearsetRenderer.java


public class PlanetaryGearsetRenderer extends KineticBlockEntityRenderer<PlanetaryGearsetBlockEntity> {


    public PlanetaryGearsetRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(PlanetaryGearsetBlockEntity planetaryGearsetBlockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (Backend.canUseInstancing(planetaryGearsetBlockEntity.getLevel())) return;

        BlockState state = getRenderedBlockState(planetaryGearsetBlockEntity);
        Direction.Axis axis = state.getValue(RotatedPillarKineticBlock.AXIS);
        VertexConsumer vbSolid = buffer.getBuffer(RenderType.solid());

        float time = AnimationTickHolder.getRenderTime();
        float offset1 = Mth.PI * getRotationOffsetForPosition(planetaryGearsetBlockEntity, planetaryGearsetBlockEntity.getBlockPos(), axis) / 180f;
        float offset2 = Mth.PI * BracketedKineticBlockEntityRenderer.getShaftAngleOffset(axis, planetaryGearsetBlockEntity.getBlockPos()) / 180f;
        float angle = ((time * planetaryGearsetBlockEntity.getSpeed() * 3f / 10 + offset1) % 360) / 180 * Mth.PI;

        SuperByteBuffer ringGear = CachedBufferer.partialDirectional(ModPartialModels.PG_RING_GEAR, state, Direction.get(Direction.AxisDirection.POSITIVE, axis), () -> rotateToAxis(axis));
        kineticRotationTransform(ringGear, planetaryGearsetBlockEntity, axis, angle + offset1, light);
        ringGear.renderInto(ms, vbSolid);

        SuperByteBuffer sunGear = CachedBufferer.partialDirectional(ModPartialModels.PG_SUN_GEAR, state, Direction.get(Direction.AxisDirection.POSITIVE, axis), () -> rotateToAxis(axis));
        kineticRotationTransform(sunGear, planetaryGearsetBlockEntity, axis, (-2 * angle) + offset2, light);
        sunGear.renderInto(ms, vbSolid);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis() == axis) continue;
            SuperByteBuffer planetGear = CachedBufferer.partialDirectional(ModPartialModels.PG_PLANET_GEAR, state, Direction.get(Direction.AxisDirection.POSITIVE, axis), () -> rotateToAxis(axis));

            Vector3f translation = direction.step();
            translation.mul(6.25f / 16f);

            planetGear.translate(translation);
            kineticRotationTransform(planetGear, planetaryGearsetBlockEntity, axis, (2 * angle) + offset2, light);
            planetGear.renderInto(ms, vbSolid);
        }
    }
    public static PoseStack rotateToAxis(Direction.Axis axis) {
        Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
        PoseStack poseStack = new PoseStack();
        TransformStack.cast(poseStack)
                .centre()
                .rotateToFace(facing)
                .multiply(Direction.WEST.step().rotationDegrees(-90))
                .unCentre();
        return poseStack;
    };
}
