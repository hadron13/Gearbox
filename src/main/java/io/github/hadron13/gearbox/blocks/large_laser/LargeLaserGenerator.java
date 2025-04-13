package io.github.hadron13.gearbox.blocks.large_laser;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import io.github.hadron13.gearbox.data.client.blockstates.HorizontalDirectionalBlockStateGen;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class LargeLaserGenerator extends HorizontalDirectionalBlockStateGen {

    @Override
    public <T extends Block> String getModelPrefix(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        LargeLaserBlock.LargeLaserPart part = state.getValue(LargeLaserBlock.PART);
        return "block/large_laser/" + part.getSerializedName();
    }
}
