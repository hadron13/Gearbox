package io.github.hadron13.gearbox.register;


import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlockEntity;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressInstance;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressRenderer;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeInstance;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeRenderer;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorBlockEntity;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorInstance;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorRenderer;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorInstance;
import io.github.hadron13.gearbox.blocks.compressor.CompressorRenderer;
import io.github.hadron13.gearbox.blocks.dipper.DipperBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerInstance;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerRenderer;
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
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillRenderer;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
//import io.github.hadron13.gearbox.blocks.planetary_gear.PlanetaryGearsetBlockEntity;
//import io.github.hadron13.gearbox.blocks.planetary_gear.PlanetaryGearsetInstance;
//import io.github.hadron13.gearbox.blocks.planetary_gear.PlanetaryGearsetRenderer;
import io.github.hadron13.gearbox.blocks.pumpjack.*;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperInstance;
import io.github.hadron13.gearbox.blocks.sapper.SapperRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerInstance;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineBlockEntity;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineInstance;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineRenderer;


public class ModBlockEntities {

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
            .validBlocks(ModBlocks.LASER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<MirrorBlockEntity> MIRROR = Gearbox.registrate()
            .blockEntity("mirror", MirrorBlockEntity::new)
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

//    public static final BlockEntityEntry<BlackHoleBlockEntity> BLACK_HOLE = Gearbox.registrate()
//            .blockEntity("black_hole", BlackHoleBlockEntity::new)
//            .validBlocks(ModBlocks.BLACK_HOLE)
//            .renderer(()-> BlackHoleRenderer::new)
//            .register();

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
//
//    public static final BlockEntityEntry<PlanetaryGearsetBlockEntity> PLANETARY_GEARSET = Gearbox.registrate()
//            .blockEntity("planetary_gearset", PlanetaryGearsetBlockEntity::new)
//            .instance(() -> PlanetaryGearsetInstance::new, false)
//            .validBlocks(ModBlocks.PLANETARY_GEARSET)
//            .renderer(() -> PlanetaryGearsetRenderer::new)
//            .register();

    public static final BlockEntityEntry<UselessMachineBlockEntity> USELESS_MACHINE = Gearbox.registrate()
            .blockEntity("kinetic_machine", UselessMachineBlockEntity::new)
            .instance(() -> UselessMachineInstance::new)
            .validBlocks(ModBlocks.USELESS_MACHINE)
            .renderer(() -> UselessMachineRenderer::new)
            .register();

    public static final BlockEntityEntry<CentrifugeBlockEntity> CENTRIFUGE = Gearbox.registrate()
            .blockEntity("centrifuge", CentrifugeBlockEntity::new)
            .instance(() -> CentrifugeInstance::new)
            .validBlocks(ModBlocks.CENTRIFUGE)
            .renderer(() -> CentrifugeRenderer::new)
            .register();

    public static final BlockEntityEntry<LaserDrillBlockEntity> LASER_DRILL = Gearbox.registrate()
            .blockEntity("laser_drill", LaserDrillBlockEntity::new)
//            .instance(() -> LargeLaserInstance::new)
            .validBlocks(ModBlocks.LASER_DRILL)
            .renderer(() -> LaserDrillRenderer::new)
            .register();


    public static final BlockEntityEntry<PumpjackArmBlockEntity> PUMPJACK_ARM = Gearbox.registrate()
            .blockEntity("pumpjack_arm", PumpjackArmBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(ModBlocks.PUMPJACK_ARM)
            .renderer(() -> PumpjackArmRenderer::new)
            // TODO: instance
            .register();
    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = Gearbox.registrate()
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(ModBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            // TODO: instance
            .register();

    public static final BlockEntityEntry<PumpjackWellBlockEntity> PUMPJACK_WELL = Gearbox.registrate()
            .blockEntity("pumpjack_well", PumpjackWellBlockEntity::new)
            .validBlocks(ModBlocks.PUMPJACK_WELL)
            .register();

    public static final BlockEntityEntry<DipperBlockEntity> DIPPER = Gearbox.registrate()
            .blockEntity("dipper", DipperBlockEntity::new)
            .validBlocks(ModBlocks.DIPPER)

            .register();

    public static final BlockEntityEntry<ReactorBlockEntity> REACTOR = Gearbox.registrate()
            .blockEntity("chemical_reactor", ReactorBlockEntity::new)
            .instance(() -> ReactorInstance::new)
            .validBlocks(ModBlocks.REACTOR)
            .renderer(() -> ReactorRenderer::new)
            .register();

    public static void register() {}
}