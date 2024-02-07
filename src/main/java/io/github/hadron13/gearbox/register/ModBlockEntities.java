package io.github.hadron13.gearbox.register;


import com.simibubi.create.content.kinetics.mixer.MechanicalMixerRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.black_hole.BlackHoleBlockEntity;
import io.github.hadron13.gearbox.blocks.black_hole.BlackHoleRenderer;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlockEntity;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressInstance;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressRenderer;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorInstance;
import io.github.hadron13.gearbox.blocks.compressor.CompressorRenderer;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerInstance;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerRenderer;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerBlockEntity;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerInstance;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerRenderer;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorInstance;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.KilnRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnInstance;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlockEntity;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserInstance;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserRenderer;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamInstance;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperInstance;
import io.github.hadron13.gearbox.blocks.sapper.SapperRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerInstance;


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

    public static final BlockEntityEntry<MirrorBlockEntity> MIRROR = Gearbox.registrate()
            .blockEntity("mirror", MirrorBlockEntity::new)
            .instance(() -> LaserBeamInstance::new)
            .validBlocks(ModBlocks.MIRROR)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<SpectrometerBlockEntity> SPECTROMETER = Gearbox.registrate()
            .blockEntity("spectrometer", SpectrometerBlockEntity::new)
            .instance(() -> SpectrometerInstance::new)
            .validBlocks(ModBlocks.SPECTROMETER)
            .renderer(() -> SpectrometerRenderer::new)
            .register();

    public static final BlockEntityEntry<IrradiatorBlockEntity> IRRADIATOR = Gearbox.registrate()
            .blockEntity("irradiator", IrradiatorBlockEntity::new)
            .instance(() -> IrradiatorInstance::new)
            .validBlocks(ModBlocks.IRRADIATOR)
            .renderer(()-> IrradiatorRenderer::new)
            .register();

    public static final BlockEntityEntry<BlackHoleBlockEntity> BLACK_HOLE = Gearbox.registrate()
            .blockEntity("black_hole", BlackHoleBlockEntity::new)
            .validBlocks(ModBlocks.BLACK_HOLE)
            .renderer(()-> BlackHoleRenderer::new)
            .register();

    public static final BlockEntityEntry<LargeLaserBlockEntity> LARGE_LASER = Gearbox.registrate()
            .blockEntity("large_laser", LargeLaserBlockEntity::new)
            .instance(() -> LargeLaserInstance::new)
            .validBlocks(ModBlocks.LARGE_LASER)
            .renderer(() -> LargeLaserRenderer::new)
            .register();

    public static final BlockEntityEntry<ElectrolyzerBlockEntity> ELECTROLYZER = Gearbox.registrate()
            .blockEntity("electrolyzer", ElectrolyzerBlockEntity::new)
            .instance(() -> ElectrolyzerInstance::new)
            .validBlocks(ModBlocks.ELECTROLYZER)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();

    public static void register() {}
}