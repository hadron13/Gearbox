package io.github.hadron13.gearbox.blocks.large_laser;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingType;
import com.simibubi.create.foundation.render.AllMaterialSpecs;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamInstance;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.util.Mth;

import static net.minecraft.core.Direction.Axis.X;
import static net.minecraft.core.Direction.Axis.Z;

public class LargeLaserInstance extends LaserBeamInstance<LargeLaserBlockEntity> {
    public ModelData lens;

    protected float lastAngle = Float.NaN;

    public LargeLaserInstance(MaterialManager materialManager, LargeLaserBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        lens = materialManager.defaultSolid()
                .material(Materials.TRANSFORMED)
                .getModel(ModPartialModels.LARGE_LASER_LENS)
                .createInstance();

    }
    @Override
    public void beginFrame(){
        super.beginFrame();

        if(!blockEntity.isFront()){
            lens.setEmptyTransform();
            return;
        }
        LaserBeamBehavior.LaserBeam beam = blockEntity.beamBehavior.getLaser(blockEntity.getFacing());


        float partialTicks = AnimationTickHolder.getPartialTicks();

        float speed = blockEntity.visualSpeed.getValue(partialTicks) * 3 / 10f;
        float angle = blockEntity.angle + speed * partialTicks;

        if (Math.abs(angle - lastAngle) < 0.001)
            return;

        lens.loadIdentity()
                .translate(getInstancePosition())
                .translate(0.5f, 0.5f, 0.5f)
                .rotateToFace(blockEntity.getFacing())
                .rotate(angle, Z)
                .translate(-0.5f, -0.5f, -0.5f);

        lastAngle = angle;

    }

    @Override
    public void updateLight() {
        super.updateLight();
//        relight(pos, lens);
        relight(15, 15, lens);
    }
    @Override
    protected void remove() {
        super.remove();
        lens.delete();
    }

}
