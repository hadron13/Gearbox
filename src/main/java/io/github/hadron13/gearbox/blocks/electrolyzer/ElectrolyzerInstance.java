package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;

public class ElectrolyzerInstance extends BlockEntityInstance<ElectrolyzerBlockEntity> implements DynamicInstance {

    public final OrientedData pole;
    public final RotatingData head;

    public ElectrolyzerInstance(MaterialManager materialManager, ElectrolyzerBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        head = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(ModPartialModels.ELECTROLYZER_HEAD, blockState)
                .createInstance();

        head.setRotationAxis(Direction.Axis.Y);

        pole = getOrientedMaterial()
                .getModel(AllPartialModels.MECHANICAL_MIXER_POLE, blockState)
                .createInstance();

        transformInstances();
    }
    @Override
    public void beginFrame() {
        transformInstances();
    }

    public void transformInstances(){
        float renderedHeadOffset = blockEntity.getRenderedHeadOffset(AnimationTickHolder.getPartialTicks());

        pole.setPosition(getInstancePosition())
                .nudge(0, -renderedHeadOffset, 0);

        float speed = blockEntity.getRenderedHeadRotationSpeed(AnimationTickHolder.getPartialTicks());

        head.setPosition(getInstancePosition())
                .nudge(0, -renderedHeadOffset, 0)
                .setRotationalSpeed(speed * 2);
    }

    @Override
    public void updateLight(){
        super.updateLight();
        relight(pos, pole);
        relight(pos.below(), head);
    }

    @Override
    protected void remove() {
        pole.delete();
        head.delete();
    }
}
