package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityInstance;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ReactorInstance extends SingleRotatingInstance<ReactorBlockEntity> implements DynamicInstance {

    public final OrientedData pole;
    public final RotatingData head;

    public ReactorInstance(MaterialManager materialManager, ReactorBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        head = materialManager.defaultCutout()
                .material(AllMaterialSpecs.ROTATING)
                .getModel(AllPartialModels.MECHANICAL_MIXER_HEAD, blockState)
                .createInstance();

        head.setRotationAxis(Direction.Axis.Y);

        pole = getOrientedMaterial()
                .getModel(ModPartialModels.DIPPER_POLE, blockState)
                .createInstance();


        transformInstances();
    }
    @Override
    protected BlockState getRenderedBlockState() {
        return shaft(Direction.Axis.Y);
    }

    @Override
    public void beginFrame() {
        transformInstances();
    }

    public void transformInstances(){
        float renderedHeadOffset = 1.1f;

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
    public void remove() {
        super.remove();
        pole.delete();
        head.delete();
    }
}
