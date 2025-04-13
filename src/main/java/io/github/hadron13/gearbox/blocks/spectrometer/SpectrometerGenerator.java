package io.github.hadron13.gearbox.blocks.spectrometer;

import com.simibubi.create.foundation.data.DirectionalAxisBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SpectrometerGenerator extends DirectionalAxisBlockStateGen {

    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov,
                                                   BlockState state) {
        return "block/spectrometer/base";
    }
}
