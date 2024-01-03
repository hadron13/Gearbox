package io.github.hadron13.gearbox.register;


import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlockEntity;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressInstance;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressRenderer;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorInstance;
import io.github.hadron13.gearbox.blocks.compressor.CompressorRenderer;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerBlockEntity;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerInstance;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.KilnRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnInstance;
import io.github.hadron13.gearbox.blocks.lasers.LaserBeamRenderer;
import io.github.hadron13.gearbox.blocks.lasers.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.lasers.LaserBeamInstance;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperInstance;
import io.github.hadron13.gearbox.blocks.sapper.SapperRenderer;


public class ModBlockEntities {
    public static final BlockEntityEntry<ExchangerBlockEntity> EXCHANGER = Gearbox.registrate()
            .blockEntity("exchanger", ExchangerBlockEntity::new)
            .instance(() -> ExchangerInstance::new)
            .validBlocks(ModBlocks.EXCHANGER)
            .renderer(() -> ExchangerRenderer::new)
            .register();

    public static final BlockEntityEntry<KilnBlockEntity> KILN = Gearbox.registrate()
            .blockEntity("kiln", KilnBlockEntity::new)
            .instance(() -> KilnInstance::new)
            .validBlocks(ModBlocks.KILN)
            .renderer(() -> KilnRenderer::new)
            .register();

    public static final BlockEntityEntry<SapperBlockEntity> SAPPER = Gearbox.registrate()
            .blockEntity("sapper", SapperBlockEntity::new)
            .instance(() -> SapperInstance::new)
            .validBlocks(ModBlocks.SAPPER)
            .renderer(() -> SapperRenderer::new)
            .register();

    public static final BlockEntityEntry<CompressorBlockEntity> COMPRESSOR = Gearbox.registrate()
            .blockEntity("compressor", CompressorBlockEntity::new)
            .instance(() -> CompressorInstance::new)
            .validBlocks(ModBlocks.COMPRESSOR)
            .renderer(() -> CompressorRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassPressBlockEntity> BRASS_PRESS = Gearbox.registrate()
            .blockEntity("brass_press", BrassPressBlockEntity::new)
            .instance(() -> BrassPressInstance::new)
            .validBlocks(ModBlocks.BRASS_PRESS)
            .renderer(() -> BrassPressRenderer::new)
            .register();
    public static final BlockEntityEntry<LaserBlockEntity> LASER = Gearbox.registrate()
            .blockEntity("laser", LaserBlockEntity::new)
            .instance(() -> LaserBeamInstance::new)
            .validBlocks(ModBlocks.LASER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();
    public static void register() {}
}