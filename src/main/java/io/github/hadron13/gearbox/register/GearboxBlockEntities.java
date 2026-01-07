package io.github.hadron13.gearbox.register;


import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.Create;
import com.simibubi.create.content.fluids.FluidNetwork;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.GlassPipeVisual;
import com.simibubi.create.content.fluids.pipes.StraightPipeBlockEntity;
import com.simibubi.create.content.fluids.pipes.TransparentStraightPipeRenderer;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.content.kinetics.crank.HandCrankRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
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
import io.github.hadron13.gearbox.blocks.combiner.CombinerBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorVisual;
import io.github.hadron13.gearbox.blocks.compressor.CompressorRenderer;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillRenderer;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillVisual;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerRenderer;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputRenderer;
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

    private static final CreateRegistrate REGISTRATE = Gearbox.registrate();

    public static final BlockEntityEntry<KilnBlockEntity> KILN = REGISTRATE
            .blockEntity("kiln", KilnBlockEntity::new)
            .visual(() -> KilnVisual::new)
            .validBlocks(GearboxBlocks.KILN)
            .renderer(() -> KilnRenderer::new)
            .register();

    public static final BlockEntityEntry<SapperBlockEntity> SAPPER = REGISTRATE
            .blockEntity("sapper", SapperBlockEntity::new)
            .visual(() -> SapperVisual::new)
            .validBlocks(GearboxBlocks.SAPPER)
            .renderer(() -> SapperRenderer::new)
            .register();

    public static final BlockEntityEntry<CompressorBlockEntity> COMPRESSOR = REGISTRATE
            .blockEntity("compressor", CompressorBlockEntity::new)
            .visual(() -> CompressorVisual::new)
            .validBlocks(GearboxBlocks.COMPRESSOR)
            .renderer(() -> CompressorRenderer::new)
            .register();

    public static final BlockEntityEntry<BrassPressBlockEntity> BRASS_PRESS = REGISTRATE
            .blockEntity("brass_press", BrassPressBlockEntity::new)
            .visual(() -> BrassPressVisual::new)
            .validBlocks(GearboxBlocks.BRASS_PRESS)
            .renderer(() -> BrassPressRenderer::new)
            .register();

    public static final BlockEntityEntry<LaserBlockEntity> LASER = REGISTRATE
            .blockEntity("laser", LaserBlockEntity::new)
            .validBlocks(GearboxBlocks.LASER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();


    public static final BlockEntityEntry<MirrorBlockEntity> MIRROR = REGISTRATE
            .blockEntity("mirror", MirrorBlockEntity::new)
            .validBlocks(GearboxBlocks.MIRROR)
            .renderer(() -> MirrorRenderer::new)
            .register();


    public static final BlockEntityEntry<PrismBlockEntity> PRISM = REGISTRATE
            .blockEntity("prism", PrismBlockEntity::new)
            .validBlocks(GearboxBlocks.PRISM)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<CombinerBlockEntity> COMBINER = REGISTRATE
            .blockEntity("combiner", CombinerBlockEntity::new)
            .validBlocks(GearboxBlocks.COMBINER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<AttenuatorBlockEntity> ATTENUATOR = REGISTRATE
            .blockEntity("attenuator", AttenuatorBlockEntity::new)
            .validBlocks(GearboxBlocks.ATTENUATOR)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<AmplifierBlockEntity> AMPLIFIER = REGISTRATE
            .blockEntity("amplifier", AmplifierBlockEntity::new)
            .validBlocks(GearboxBlocks.AMPLIFIER)
            .renderer(() -> LaserBeamRenderer::new)
            .register();

    public static final BlockEntityEntry<SpectrometerBlockEntity> SPECTROMETER = REGISTRATE
            .blockEntity("spectrometer", SpectrometerBlockEntity::new)
            .visual(() -> SpectrometerVisual::new)
            .validBlocks(GearboxBlocks.SPECTROMETER)
            .renderer(() -> SpectrometerRenderer::new)
            .register();

    public static final BlockEntityEntry<IrradiatorBlockEntity> IRRADIATOR = REGISTRATE
            .blockEntity("irradiator", IrradiatorBlockEntity::new)
            .visual(() -> IrradiatorInstance::new)
            .validBlocks(GearboxBlocks.IRRADIATOR)
            .renderer(()-> IrradiatorRenderer::new)
            .register();

//    public static final BlockEntityEntry<BlackHoleBlockEntity> BLACK_HOLE = REGISTRATE
//            .blockEntity("black_hole", BlackHoleBlockEntity::new)
//            .validBlocks(ModBlocks.BLACK_HOLE)
//            .renderer(()-> BlackHoleRenderer::new)
//            .register();



    public static final BlockEntityEntry<ElectrolyzerBlockEntity> ELECTROLYZER = REGISTRATE
            .blockEntity("electrolyzer", ElectrolyzerBlockEntity::new)
            .visual(() -> ElectrolyzerVisual::new)
            .validBlocks(GearboxBlocks.ELECTROLYZER)
            .renderer(() -> ElectrolyzerRenderer::new)
            .register();

    public static final BlockEntityEntry<UselessMachineBlockEntity> USELESS_MACHINE = REGISTRATE
            .blockEntity("kinetic_machine", UselessMachineBlockEntity::new)
            .visual(() -> UselessMachineInstance::new)
            .validBlocks(GearboxBlocks.USELESS_MACHINE)
            .renderer(() -> UselessMachineRenderer::new)
            .register();

    public static final BlockEntityEntry<CentrifugeBlockEntity> CENTRIFUGE = REGISTRATE
            .blockEntity("centrifuge", CentrifugeBlockEntity::new)
            .visual(() -> CentrifugeVisual::new)
            .validBlocks(GearboxBlocks.CENTRIFUGE)
            .renderer(() -> CentrifugeRenderer::new)
            .register();

//    public static final BlockEntityEntry<LaserDrillBlockEntity> LASER_DRILL = REGISTRATE
//            .blockEntity("laser_drill", LaserDrillBlockEntity::new)
////            .instance(() -> LargeLaserVisual::new)
//            .validBlocks(GearboxBlocks.LASER_DRILL)
//            .renderer(() -> LaserDrillRenderer::new)
//            .register();
//

    public static final BlockEntityEntry<PumpjackArmBlockEntity> PUMPJACK_ARM = REGISTRATE
            .blockEntity("pumpjack_arm", PumpjackArmBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(GearboxBlocks.PUMPJACK_ARM)
            .renderer(() -> PumpjackArmRenderer::new)
            // TODO: instance
            .register();
    public static final BlockEntityEntry<PumpjackCrankBlockEntity> PUMPJACK_CRANK = REGISTRATE
            .blockEntity("pumpjack_crank", PumpjackCrankBlockEntity::new)
            //.instance(() -> ::new)
            .validBlocks(GearboxBlocks.PUMPJACK_CRANK)
            .renderer(() -> PumpjackCrankRenderer::new)
            // TODO: instance
            .register();

    public static final BlockEntityEntry<PumpjackWellBlockEntity> PUMPJACK_WELL = REGISTRATE
            .blockEntity("pumpjack_well", PumpjackWellBlockEntity::new)
            .validBlocks(GearboxBlocks.PUMPJACK_WELL)
            .register();

//    public static final BlockEntityEntry<DipperBlockEntity> DIPPER = REGISTRATE
//            .blockEntity("dipper", DipperBlockEntity::new)
//            .validBlocks(GearboxBlocks.DIPPER)
//
//            .register();
//
//    public static final BlockEntityEntry<ReactorBlockEntity> REACTOR = REGISTRATE
//            .blockEntity("chemical_reactor", ReactorBlockEntity::new)
//            .visual(() -> ReactorVisual::new)
//            .validBlocks(GearboxBlocks.REACTOR)
//            .renderer(() -> ReactorRenderer::new)
//            .register();

    public static final BlockEntityEntry<PrecisionCrankBlockEntity> PRECISION_CRANK = REGISTRATE
            .blockEntity("precision_crank", PrecisionCrankBlockEntity::new)
            .visual(() -> PrecisionCrankVisual::new)
            .validBlocks(GearboxBlocks.PRECISION_CRANK)
            .renderer(() -> HandCrankRenderer::new)
            .register();


    public static final BlockEntityEntry<CoreDrillBlockEntity> CORE_DRILL = REGISTRATE
            .blockEntity("core_drill", CoreDrillBlockEntity::new)
            .visual(() -> CoreDrillVisual::new)
            .validBlocks(GearboxBlocks.CORE_DRILL)
            .renderer(() -> CoreDrillRenderer::new)
            .register();

    public static final BlockEntityEntry<SteelTankBlockEntity> STEEL_FLUID_TANK = REGISTRATE
            .blockEntity("steel_fluid_tank", SteelTankBlockEntity::new)
            .validBlocks(GearboxBlocks.STEEL_FLUID_TANK)
            .renderer(() -> SteelFluidTankRenderer::new)
            .register();

    public static final BlockEntityEntry<FluidPipeBlockEntity> STEEL_FLUID_PIPE = REGISTRATE
            .blockEntity("steel_fluid_pipe", FluidPipeBlockEntity::new)
            .validBlocks(GearboxBlocks.STEEL_FLUID_PIPE)
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> STEEL_GLASS_FLUID_PIPE = REGISTRATE
            .blockEntity("steel_glass_fluid_pipe", StraightPipeBlockEntity::new)
            .visual(() -> GlassPipeVisual::new, false)
            .validBlocks(GearboxBlocks.STEEL_GLASS_FLUID_PIPE)
            .renderer(() -> TransparentStraightPipeRenderer::new)
            .register();

    public static final BlockEntityEntry<StraightPipeBlockEntity> STRAIGHT_STEEL_FLUID_PIPE = REGISTRATE
            .blockEntity("straight_steel_fluid_pipe", StraightPipeBlockEntity::new)
            .validBlocks(GearboxBlocks.STRAIGHT_STEEL_FLUID_PIPE)
            .register();

    public static final BlockEntityEntry<PumpBlockEntity> STEEL_FLUID_PUMP = REGISTRATE
            .blockEntity("steel_fluid_pump", PumpBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.ofZ(GearboxPartialModels.STEEL_PUMP_COG))
            .validBlocks(GearboxBlocks.STEEL_PUMP)
            .renderer(() -> PumpRenderer::new)
            .register();

    public static final BlockEntityEntry<DistillationControllerBlockEntity> DISTILLATION_CONTROLLER = REGISTRATE
            .blockEntity("distillation_controller", DistillationControllerBlockEntity::new)
            .validBlocks(GearboxBlocks.DISTILLATION_CONTROLLER)
            .renderer(() -> DistillationControllerRenderer::new)
            .register();

    public static final BlockEntityEntry<DistillationOutputBlockEntity> DISTILLATION_OUTPUT = REGISTRATE
            .blockEntity("distillation_output", DistillationOutputBlockEntity::new)
            .validBlocks(GearboxBlocks.DISTILLATION_OUTPUT)
            .renderer(() -> DistillationOutputRenderer::new)
            .register();

    public static void register() {}
}