package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.fluids.PipeAttachmentModel;
import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.tank.*;
import com.simibubi.create.content.materials.ExperienceBlock;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.block.ItemUseOverrides;
import com.simibubi.create.foundation.data.*;
import com.simibubi.create.infrastructure.config.CStress;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.amplifier.AmplifierBlock;
import io.github.hadron13.gearbox.blocks.attenuator.AttenuatorBlock;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlock;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlock;
import io.github.hadron13.gearbox.blocks.combiner.CombinerBlock;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlock;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlock;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerBlock;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerGenerator;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlock;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlock;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlock;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlock;
import io.github.hadron13.gearbox.blocks.precision_crank.PrecisionCrankBlock;
import io.github.hadron13.gearbox.blocks.prism.PrismBlock;
import io.github.hadron13.gearbox.blocks.steel_pipe.SteelGlassPipeBlock;
import io.github.hadron13.gearbox.blocks.steel_pipe.SteelPipeAttachmentModel;
import io.github.hadron13.gearbox.blocks.steel_pipe.SteelPipeBlock;
import io.github.hadron13.gearbox.blocks.steel_pipe.StraightSteelPipeBlock;
import io.github.hadron13.gearbox.blocks.steel_pump.SteelPumpBlock;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelFluidTankModel;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlock;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankItem;
import io.github.hadron13.gearbox.data.client.blockstates.*;
import io.github.hadron13.gearbox.blocks.laser.LaserBlock;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlock;
import io.github.hadron13.gearbox.blocks.pumpjack.*;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerGenerator;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineBlock;
import io.github.hadron13.gearbox.config.GearboxStress;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

import static com.simibubi.create.api.behaviour.movement.MovementBehaviour.movementBehaviour;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class GearboxBlocks {


    private static final CreateRegistrate REGISTRATE = Gearbox.registrate().setCreativeTab(GearboxCreativeModeTabs.MAIN_TAB);

    public static void register() {}

//    public static final BlockEntry<ExchangerBlock> EXCHANGER = REGISTRATE.block("exchanger", ExchangerBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .properties(p -> p.mapColor(MapColor.METAL))
//            .transform(pickaxeOnly())
//            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
//            .transform(BlockStressDefaults.setImpact(2.0))
//            .item()
//            .transform(customItemModel())
//            .register();


    public static final BlockEntry<KilnBlock> KILN = REGISTRATE.block("kiln", KilnBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p  .mapColor(MapColor.METAL)
                                .lightLevel(s -> s.getValue(KilnBlock.POWERED) ? 15 : 0))
            .transform(pickaxeOnly())
            .blockstate(new KilnGenerator()::generate)
            .transform(GearboxStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SapperBlock> SAPPER = REGISTRATE.block("sapper", SapperBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.noOcclusion().mapColor(MapColor.METAL))
            //.addLayer(() -> RenderType::cutoutMipped)
            .blockstate(new PartialHorizontalBlockStateGen()::generate)
            .transform(GearboxStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CompressorBlock> COMPRESSOR = REGISTRATE.block("compressor", CompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.noOcclusion().mapColor(MapColor.METAL))
            .blockstate(new PartialHorizontalBlockStateGen().flipY(180)::generate)
            .transform(GearboxStress.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<BrassPressBlock> BRASS_PRESS = REGISTRATE.block("brass_press", BrassPressBlock::new)
            .lang("Mechanizing Press")
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(GearboxStress.setImpact(16.0))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LaserBlock> LASER = REGISTRATE.block("laser", LaserBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<MirrorBlock> MIRROR = REGISTRATE.block("mirror", MirrorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.axisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<PrismBlock> PRISM = REGISTRATE.block("prism", PrismBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalAxisBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<CombinerBlock> COMBINER = REGISTRATE.block("combiner", CombinerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<AttenuatorBlock> ATTENUATOR = REGISTRATE.block("attenuator", AttenuatorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<AmplifierBlock> AMPLIFIER = REGISTRATE.block("amplifier", AmplifierBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SpectrometerBlock> SPECTROMETER = REGISTRATE.block("spectrometer", SpectrometerBlock::new)
            .initialProperties(SharedProperties::wooden)
            .transform(axeOrPickaxe())
            .properties(p -> p.mapColor(MapColor.PODZOL))
            .blockstate(new SpectrometerGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<IrradiatorBlock> IRRADIATOR = REGISTRATE.block("irradiator", IrradiatorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(GearboxStress.setImpact(4.0f))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();
//    public static final BlockEntry<BlackHoleBlock> BLACK_HOLE  = REGISTRATE.block("black_hole", BlackHoleBlock::new)
//            .initialProperties(SharedProperties::netheriteMetal)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).noCollission())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
//            .item()
//            .transform(customItemModel())
//            .register();

    public static final BlockEntry<ElectrolyzerBlock> ELECTROLYZER = REGISTRATE.block("electrolyzer", ElectrolyzerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<UselessMachineBlock> USELESS_MACHINE = REGISTRATE.block("kinetic_machine", UselessMachineBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .transform(axeOrPickaxe())
            .blockstate(new PartialHorizontalBlockStateGen()::generate)
            .transform(GearboxStress.setImpact(0))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<CentrifugeBlock> CENTRIFUGE = REGISTRATE.block("centrifuge", CentrifugeBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(new PartialAxisBlockStateGen()::generate)
            .transform(GearboxStress.setImpact(8.0))
            .item()
            .transform(customItemModel())
            .register();

//    public static final BlockEntry<LaserDrillBlock> LASER_DRILL = REGISTRATE.block("laser_drill", LaserDrillBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
//            .item(LaserDrillItem::new)
//            .transform(customItemModel())
//            .register();

    public static final BlockEntry<PumpjackArmBlock> PUMPJACK_ARM = REGISTRATE.block("pumpjack_arm", PumpjackArmBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).noOcclusion())
            .blockstate(PumpjackGenerator.arm()::generate)
            .item(PumpjackArmBlockItem::new)
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Gearbox.asResource("block/pumpjack/arm_item")))
            .build()
            .register();
    public static final BlockEntry<PumpjackCrankBlock> PUMPJACK_CRANK = REGISTRATE.block("pumpjack_crank", PumpjackCrankBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.METAL).mapColor(MapColor.METAL).noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(PumpjackGenerator.crank()::generate)
            .transform(GearboxStress.setImpact(32.0))
            .item()
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Gearbox.asResource("block/pumpjack/crank_item")))
            .build()
            .register();


    public static final BlockEntry<PumpjackWellBlock> PUMPJACK_WELL = REGISTRATE.block("pumpjack_well", PumpjackWellBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate(PumpjackGenerator.well()::generate)
            .item()
            .model((ctx, prov) -> prov.withExistingParent(prov.name(ctx), Gearbox.asResource("block/pumpjack/well")))
            .build()
            .register();


//    public static final BlockEntry<DipperBlock> DIPPER = REGISTRATE.block("dipper", DipperBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
//            //.addLayer(() -> RenderType::cutoutMipped)
//            .item(AssemblyOperatorBlockItem::new)
//            .transform(customItemModel())
//            .register();
//
//
//    public static final BlockEntry<ReactorBlock> REACTOR = REGISTRATE.block("chemical_reactor", ReactorBlock::new)
//            .initialProperties(SharedProperties::stone)
//            .transform(pickaxeOnly())
//            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
//            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
//            //.addLayer(() -> RenderType::cutoutMipped)
//            .transform(GearboxStress.setImpact(4.0))
//            .item(AssemblyOperatorBlockItem::new)
//            .transform(customItemModel())
//            .register();

    public static final BlockEntry<PrecisionCrankBlock> PRECISION_CRANK = REGISTRATE.block("precision_crank", PrecisionCrankBlock::new)
           .initialProperties(SharedProperties::stone)
           .properties(p -> p.mapColor(MapColor.METAL))
           .transform(axeOrPickaxe())
           .blockstate(BlockStateGen.directionalBlockProvider(true))
           .transform(GearboxStress.setCapacity(8.0))
           .onRegister(BlockStressValues.setGeneratorSpeed(32))
           .tag(AllTags.AllBlockTags.BRITTLE.tag)
           .onRegister(ItemUseOverrides::addBlock)
           .item()
           .transform(customItemModel())
           .register();


    public static final BlockEntry<CoreDrillBlock> CORE_DRILL = REGISTRATE.block("core_drill", CoreDrillBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(GearboxStress.setImpact(16.0))
            .item()
            .transform(customItemModel())
            .register();

//    public static final BlockEntry<DistillationTankBlock> DISTILLATION_TANK = REGISTRATE.block("distillation_tank", DistillationTankBlock::new)
//            .initialProperties(SharedProperties::copperMetal)
//            .properties(p -> p.noOcclusion()
//                    .isRedstoneConductor((p1, p2, p3) -> true))
//            .transform(pickaxeOnly())
//            .blockstate(new FluidTankGenerator()::generate)
//            .onRegister(CreateRegistrate.blockModel(() -> FluidTankModel::standard))
//            .item(FluidTankItem::new)
//            .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
//            .build()
//            .register();

    public static final BlockEntry<SteelTankBlock> STEEL_FLUID_TANK = REGISTRATE.block("steel_fluid_tank", SteelTankBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion().sound(SoundType.METAL))
                    .transform(pickaxeOnly())
                    .blockstate(new FluidTankGenerator()::generate)
//                    .transform(mountedFluidStorage(CreateMountedStorageTypes.FLUID_TANK))
                    .onRegister(movementBehaviour(new FluidTankMovementBehavior()))
                    .onRegister(CreateRegistrate.blockModel(() -> SteelFluidTankModel::new))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(SteelTankItem::new)
                    .model(AssetLookup.customBlockItemModel("_", "block_single_window"))
                    .build()
                    .register();


    public static final BlockEntry<SteelPipeBlock> STEEL_FLUID_PIPE = REGISTRATE.block("steel_fluid_pipe", SteelPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.pipe())
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<SteelGlassPipeBlock> STEEL_GLASS_FLUID_PIPE =
            REGISTRATE.block("glass_fluid_pipe", SteelGlassPipeBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
                    .addLayer(() -> RenderType::cutoutMipped)
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> {
                        p.getVariantBuilder(c.getEntry())
                                .forAllStatesExcept(state -> {
                                    Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                                    return ConfiguredModel.builder()
                                            .modelFile(p.models()
                                                    .getExistingFile(p.modLoc("block/steel_fluid_pipe/window")))
                                            .uvLock(false)
                                            .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                            .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                            .build();
                                }, BlockStateProperties.WATERLOGGED);
                    })
                    .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
                    .loot((p, b) -> p.dropOther(b, STEEL_FLUID_PIPE.get()))
                    .register();


    public static final BlockEntry<StraightSteelPipeBlock> STRAIGHT_STEEL_FLUID_PIPE = REGISTRATE.block("straight_steel_fluid_pipe", StraightSteelPipeBlock::new)
            .initialProperties(SharedProperties::copperMetal)
            .properties(p -> p.forceSolidOff().sound(SoundType.METAL))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> {
                p.getVariantBuilder(c.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);
                            return ConfiguredModel.builder()
                                    .modelFile(p.models()
                                            .getExistingFile(p.modLoc("block/steel_fluid_pipe/straight")))
                                    .uvLock(false)
                                    .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                                    .rotationY(axis == Direction.Axis.X ? 90 : 0)
                                    .build();
                        }, BlockStateProperties.WATERLOGGED);
            })
            .onRegister(CreateRegistrate.blockModel(() -> SteelPipeAttachmentModel::withAO))
            .loot((p, b) -> p.dropOther(b, STEEL_FLUID_PIPE.get()))
            .register();

    public static final BlockEntry<DistillationControllerBlock> DISTILLATION_CONTROLLER = REGISTRATE.block("distillation_controller", DistillationControllerBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .transform(axeOrPickaxe())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate(new DistillationControllerGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SteelPumpBlock> STEEL_PUMP = REGISTRATE.block("steel_pump", SteelPumpBlock::new)
            .initialProperties(SharedProperties::softMetal)
            .properties(p -> p.mapColor(MapColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.directionalBlockProviderIgnoresWaterlogged(true))
            .onRegister(CreateRegistrate.blockModel(() -> PipeAttachmentModel::withAO))
            .transform(GearboxStress.setImpact(3.0))
            .item()
            .transform(customItemModel())
            .register();



    public static final BlockEntry<DistillationOutputBlock> DISTILLATION_OUTPUT = REGISTRATE.block("distillation_output", DistillationOutputBlock::new)
            .initialProperties(SharedProperties::netheriteMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<Block> ASPHALT_BLOCK =  REGISTRATE.block("asphalt", Block::new)
                    .initialProperties(SharedProperties::stone)
                    .properties(p -> p.mapColor(MapColor.TERRACOTTA_BLACK).speedFactor(1.5f))
                    .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
                    .transform(pickaxeOnly())
                    .lang("Asphalt Block")
                    .item()
                    .build()
                    .register();
}