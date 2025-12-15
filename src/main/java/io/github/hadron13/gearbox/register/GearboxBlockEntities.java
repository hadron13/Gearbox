package io.github.hadron13.gearbox.register;


import com.simibubi.create.content.kinetics.crank.HandCrankRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.amplifier.AmplifierBlockEntity;
import io.github.hadron13.gearbox.blocks.attenuator.AttenuatorBlockEntity;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlockEntity;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressVisual;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressRenderer;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeVisual;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeRenderer;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorBlockEntity;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorVisual;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorRenderer;
import io.github.hadron13.gearbox.blocks.combiner.CombinerBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorVisual;
import io.github.hadron13.gearbox.blocks.compressor.CompressorRenderer;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillRenderer;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillVisual;
import io.github.hadron13.gearbox.blocks.dipper.DipperBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerVisual;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerRenderer;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorInstance;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.KilnRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnVisual;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamRenderer;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillRenderer;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
import io.github.hadron13.gearbox.blocks.mirror.MirrorRenderer;
import io.github.hadron13.gearbox.blocks.precision_crank.PrecisionCrankBlockEntity;
import io.github.hadron13.gearbox.blocks.precision_crank.PrecisionCrankVisual;
import io.github.hadron13.gearbox.blocks.prism.PrismBlockEntity;
import io.github.hadron13.gearbox.blocks.pumpjack.*;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperVisual;
import io.github.hadron13.gearbox.blocks.sapper.SapperRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerRenderer;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerVisual;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelFluidTankRenderer;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineBlockEntity;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineInstance;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineRenderer;


public class GearboxBlockEntities {

    public static final BlockEntityEntry<KilnBlockEntity> KILN = Gearbox.registrate()
            .blockEntity("kiln", KilnBlockEntity::new)
            .visual(() -> KilnVisual::new)
            .validBlocks(GearboxBlocks.KILN)
            .renderer(() -> KilnRenderer::new)
            .register();

    public static final BlockEntityEntry<SapperBlockEntity> SAPPER = Gearbox.registrate()
            .blockEntity("sapper", SapperBlockEntity::new)
            .visual(() -> SapperVisual::new)
            .validBlocks(GearboxBlocks.SAPPER)
            .renderer(() -> SapperRenderer::new)
            .register();

    public static final BlockEntityEntry<CompressorBlockEntity> COMPRESSOR = Gearbox.registrate()
            .blockEntity("compressor", CompressorBlockEntity::new)
            .visual(() -> CompressorVisual::new)
            .validBlocks(GearboxBlocks.COMPRESSOR)
            .renderer(() -> CompressorRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassPressBlockEntity> BRASS_PRESS = Gearbox.registrate()
            .blockEntity("brass_press", BrassPressBlockEntity::new)
            .visual(() -> BrassPressVisual::new)
            .validBlocks(GearboxBlocks.BRASS_PRESS)
            .renderer(() -> BrassPressRenderer::new)
            .register();

    public static final BlockEntityEntry<LaserBlockEntity> LASER = Gearbox.registrate()
            .blockEntity("laser", LaserBlockEntity::new)
            .validBlocks(GearboxBlocks.LASER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();


    public static final BlockEntityEntry<MirrorBlockEntity> MIRROR = Gearbox.registrate()
            .blockEntity("mirror", MirrorBlockEntity::new)
            .validBlocks(GearboxBlocks.MIRROR)
            .renderer(() -> MirrorRenderer::new)
            .register();


    public static final BlockEntityEntry<PrismBlockEntity> PRISM = Gearbox.registrate()
            .blockEntity("prism", PrismBlockEntity::new)
            .validBlocks(GearboxBlocks.PRISM)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<CombinerBlockEntity> COMBINER = Gearbox.registrate()
            .blockEntity("combiner", CombinerBlockEntity::new)
            .validBlocks(GearboxBlocks.COMBINER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<AttenuatorBlockEntity> ATTENUATOR = Gearbox.registrate()
            .blockEntity("attenuator", AttenuatorBlockEntity::new)
            .validBlocks(GearboxBlocks.ATTENUATOR)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<AmplifierBlockEntity> AMPLIFIER = Gearbox.registrate()
            .blockEntity("amplifier", AmplifierBlockEntity::new)
            .validBlocks(GearboxBlocks.AMPLIFIER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<SpectrometerBlockEntity> SPECTROMETER = Gearbox.registrate()
            .blockEntity("spectrometer", SpectrometerBlockEntity::new)
            .visual(() -> SpectrometerVisual::new)
            .validBlocks(GearboxBlocks.SPECTROMETER)
            .renderer(() -> SpectrometerRenderer::new)
            .register();

    public static final BlockEntityEntry<IrradiatorBlockEntity> IRRADIATOR = Gearbox.registrate()
            .blockEntity("irradiator", IrradiatorBlockEntity::new)
            .visual(() -> IrradiatorInstance::new)
            .validBlocks(GearboxBlocks.IRRADIATOR)
            .renderer(()-> IrradiatorRenderer::new)
            .register();

//    public static final BlockEntityEntry<BlackHoleBlockEntity> BLACK_HOLE = Gearbox.registrate()
//            .blockEntity("black_hole", BlackHoleBlockEntity::new)
//            .validBlocks(ModBlocks.BLACK_HOLE)
//            .renderer(()-> BlackHoleRenderer::new)
//            .register();



    public static final BlockEntityEntry<ElectrolyzerBlockEntity> ELECTROLYZER = Gearbox.registrate()
            .blockEntity("electrolyzer", ElectrolyzerBlockEntity::new)
            .visual(() -> ElectrolyzerVisual::new)
            .validBlocks(GearboxBlocks.ELECTROLYZER)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();

    public static final BlockEntityEntry<UselessMachineBlockEntity> USELESS_MACHINE = Gearbox.registrate()
            .blockEntity("kinetic_machine", UselessMachineBlockEntity::new)
            .visual(() -> UselessMachineInstance::new)
            .validBlocks(GearboxBlocks.USELESS_MACHINE)
            .renderer(() -> UselessMachineRenderer::new)
            .register();

    public static final BlockEntityEntry<CentrifugeBlockEntity> CENTRIFUGE = Gearbox.registrate()
            .blockEntity("centrifuge", CentrifugeBlockEntity::new)
            .visual(() -> CentrifugeVisual::new)
            .validBlocks(GearboxBlocks.CENTRIFUGE)
            .renderer(() -> CentrifugeRenderer::new)
            .register();

    public static final BlockEntityEntry<LaserDrillBlockEntity> LASER_DRILL = Gearbox.registrate()
            .blockEntity("laser_drill", LaserDrillBlockEntity::new)
//            .instance(() -> LargeLaserVisual::new)
            .validBlocks(GearboxBlocks.LASER_DRILL)
            .renderer(() -> LaserDrillRenderer::new)
            .register();


    public static final BlockEntityEntry<PumpjackArmBlockEntity> PUMPJACK_ARM = Gearbox.registrate()
            .blockEntity("pumpjack_arm", PumpjackArmBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(GearboxBlocks.PUMPJACK_ARM)
            .renderer(() -> PumpjackArmRenderer::new)
            // TODO: instance
            .register();
    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = Gearbox.registrate()
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(GearboxBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            // TODO: instance
            .register();

    public static final BlockEntityEntry<PumpjackWellBlockEntity> PUMPJACK_WELL = Gearbox.registrate()
            .blockEntity("pumpjack_well", PumpjackWellBlockEntity::new)
            .validBlocks(GearboxBlocks.PUMPJACK_WELL)
            .register();

    public static final BlockEntityEntry<DipperBlockEntity> DIPPER = Gearbox.registrate()
            .blockEntity("dipper", DipperBlockEntity::new)
            .validBlocks(GearboxBlocks.DIPPER)

            .register();

    public static final BlockEntityEntry<ReactorBlockEntity> REACTOR = Gearbox.registrate()
            .blockEntity("chemical_reactor", ReactorBlockEntity::new)
            .visual(() -> ReactorVisual::new)
            .validBlocks(GearboxBlocks.REACTOR)
            .renderer(() -> ReactorRenderer::new)
            .register();

    public static final BlockEntityEntry<PrecisionCrankBlockEntity> PRECISION_CRANK = Gearbox.registrate()
            .blockEntity("precision_crank", PrecisionCrankBlockEntity::new)
            .visual(() -> PrecisionCrankVisual::new)
            .validBlocks(GearboxBlocks.PRECISION_CRANK)
            .renderer(() -> HandCrankRenderer::new)
            .register();


    public static final BlockEntityEntry<CoreDrillBlockEntity> CORE_DRILL = Gearbox.registrate()
            .blockEntity("core_drill", CoreDrillBlockEntity::new)
            .visual(() -> CoreDrillVisual::new)
            .validBlocks(GearboxBlocks.CORE_DRILL)
            .renderer(() -> CoreDrillRenderer::new)
            .register();

    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = Gearbox.registrate()
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(GearboxBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();

    public static void register() {}
}