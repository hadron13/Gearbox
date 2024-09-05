package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.awt.*;

public class IrradiatorInstance extends SingleRotatingInstance<IrradiatorBlockEntity> implements DynamicInstance {

    public OrientedData lens;

    public IrradiatorInstance(MaterialManager materialManager, IrradiatorBlockEntity blockEntity) {
        super(materialManager, blockEntity);
        lens = getOrientedMaterial().getModel(ModPartialModels.IRRADIATOR_LENS).createInstance();
    }

    @Override
    public void beginFrame() {
        float lerpedLensPos =  Mth.lerp(AnimationTickHolder.getPartialTicks(),
                                        blockEntity.previousLensPos,
                                        blockEntity.lensPosition);

        lens.setPosition(getInstancePosition()).nudge(0, lerpedLensPos, 0);

    }

    @Override
    public Instancer<RotatingData> getModel(){
        BlockState referenceState = blockEntity.getBlockState();
        Direction facing = Direction.UP;
        return getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, referenceState, facing);
    }
    @Override
    public void updateLight(){
        super.updateLight();
        relight(pos, lens);
    }
    @Override
    public void remove(){
        super.remove();
        lens.delete();
    }
}
