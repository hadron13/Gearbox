package io.github.hadron13.gearbox.blocks.brass_press;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.simibubi.create.content.kinetics.base.ShaftInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Color;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;

public class BrassPressInstance extends ShaftInstance<BrassPressBlockEntity> implements DynamicInstance {
	private final RotatingData pressHead;
	private final OrientedData pressPole;

	public BrassPressInstance(MaterialManager materialManager, BrassPressBlockEntity blockEntity) {
		super(materialManager, blockEntity);

		pressHead = materialManager.defaultCutout()
				.material(AllMaterialSpecs.ROTATING)
				.getModel(ModPartialModels.BRASS_PRESS_HEAD, blockState)
				.createInstance();
		pressHead.setRotationAxis(Direction.Axis.Y);

		pressPole = materialManager.defaultSolid()
				.material(Materials.ORIENTED)
				.getModel(ModPartialModels.BRASS_PRESS_POLE, blockState)
				.createInstance();

		Quaternion q = Vector3f.YP
			.rotationDegrees(AngleHelper.horizontalAngle(blockState.getValue(BrassPressBlock.HORIZONTAL_FACING)));

		pressPole.setRotation(q);

		transformModels();
	}

	@Override
	public void beginFrame() {
		transformModels();
	}

	private void transformModels() {
		float renderedHeadOffset = getRenderedHeadOffset(blockEntity);
		float renderedHeadRotation = blockEntity.getRenderedHeadRotation(AnimationTickHolder.getPartialTicks());

		pressHead.setPosition(getInstancePosition())
				.setRotationOffset(renderedHeadRotation)
				.nudge(0, -renderedHeadOffset, 0);


		pressPole.setPosition(getInstancePosition())
				.nudge(0, -renderedHeadOffset, 0);
	}

	private float getRenderedHeadOffset(BrassPressBlockEntity press) {
		PressingBehaviour pressingBehaviour = press.getPressingBehaviour();

		return press.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks())
			* pressingBehaviour.mode.headOffset;
	}

	@Override
	public void updateLight() {
		super.updateLight();

		relight(pos, pressHead);
		relight(pos, pressPole);
	}

	@Override
	public void remove() {
		super.remove();
		pressHead.delete();
		pressPole.delete();
	}
}
