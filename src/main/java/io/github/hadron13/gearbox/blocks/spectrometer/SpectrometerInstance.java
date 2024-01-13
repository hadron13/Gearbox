package io.github.hadron13.gearbox.blocks.spectrometer;

import com.jozufozu.flywheel.api.Instancer;
import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.GaugeInstance;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;


public class SpectrometerInstance extends GaugeInstance {
    public SpectrometerInstance(MaterialManager materialManager, GaugeBlockEntity blockEntity) {
        super(materialManager, blockEntity);
    }

    @Override
    protected Instancer<ModelData> getHeadModel() {
        return getTransformMaterial().getModel(ModPartialModels.SPECTROGAUGE, blockState);
    }
    @Override
    protected BlockState getRenderedBlockState() {
        return Blocks.AIR.defaultBlockState();
    }

}
