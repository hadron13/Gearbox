package io.github.hadron13.gearbox.register;

import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlock;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlock;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactorBlock;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlock;
import io.github.hadron13.gearbox.blocks.dipper.DipperBlock;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlock;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlock;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlock;
import io.github.hadron13.gearbox.data.client.blockstates.KilnGenerator;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserGenerator;
import io.github.hadron13.gearbox.blocks.laser.LaserBlock;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillBlock;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillItem;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlock;
import io.github.hadron13.gearbox.blocks.pumpjack.*;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerGenerator;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineBlock;
import io.github.hadron13.gearbox.config.GearboxStress;
import io.github.hadron13.gearbox.data.client.blockstates.PartialAxisBlockStateGen;
import io.github.hadron13.gearbox.data.client.blockstates.PartialHorizontalBlockStateGen;
import io.github.hadron13.gearbox.data.client.blockstates.PumpjackGenerator;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {


    private static final CreateRegistrate REGISTRATE = Gearbox.registrate().setCreativeTab(ModCreativeTabs.MAIN_TAB);


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
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<MirrorBlock> MIRROR = REGISTRATE.block("mirror", MirrorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
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
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .transform(GearboxStress.setImpact(4.0f))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LargeLaserBlock> LARGE_LASER = REGISTRATE.block("large_laser", LargeLaserBlock::new)
            .initialProperties(SharedProperties::netheriteMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL))
            .blockstate(new LargeLaserGenerator()::generate)
            .item()
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
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            //.addLayer(() -> RenderType::cutoutMipped)
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();
/*
    public static final BlockEntry<PlanetaryGearsetBlock> PLANETARY_GEARSET = REGISTRATE.block("planetary_gearset", PlanetaryGearsetBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.DIRT)
            ).transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .item(CogwheelBlockItem::new)
            .transform(customItemModel())
            .register();

 */


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

    public static final BlockEntry<LaserDrillBlock> LASER_DRILL = REGISTRATE.block("laser_drill", LaserDrillBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            .item(LaserDrillItem::new)
            .transform(customItemModel())
            .register();
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


    public static final BlockEntry<DipperBlock> DIPPER = REGISTRATE.block("dipper", DipperBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            //.addLayer(() -> RenderType::cutoutMipped)
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();


    public static final BlockEntry<ReactorBlock> REACTOR = REGISTRATE.block("chemical_reactor", ReactorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.mapColor(MapColor.COLOR_GRAY).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.partialBaseModel(c, p)))
            //.addLayer(() -> RenderType::cutoutMipped)
            .transform(GearboxStress.setImpact(4.0))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();
}