package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.gauge.GaugeGenerator;
import com.simibubi.create.content.kinetics.simpleRelays.BracketedKineticBlockModel;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogwheelBlockItem;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.black_hole.BlackHoleBlock;
import io.github.hadron13.gearbox.blocks.brass_press.BrassPressBlock;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlock;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlock;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlock;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerBlock;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlock;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlock;
import io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock;
import io.github.hadron13.gearbox.blocks.laser.LaserBlock;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillBlock;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillItem;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlock;
//import io.github.hadron13.gearbox.blocks.planetary_gear.PlanetaryGearsetBlock;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock;
import io.github.hadron13.gearbox.blocks.useless_machine.UselessMachineBlock;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.Create.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class ModBlocks {


    private static final CreateRegistrate REGISTRATE = Gearbox.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    static {
        REGISTRATE.setTooltipModifierFactory(item -> {
            return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
                    .andThen(TooltipModifier.mapNull(KineticStats.create(item)));
        });
    }

    public static void register() {}
    public static final BlockEntry<ExchangerBlock> EXCHANGER = REGISTRATE.block("exchanger", ExchangerBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.METAL))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
            .transform(BlockStressDefaults.setImpact(2.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<KilnBlock> KILN = REGISTRATE.block("kiln", KilnBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p  .color(MaterialColor.METAL)
                                .lightLevel(s -> s.getValue(KilnBlock.POWERED) ? 15 : 0))
            .transform(pickaxeOnly())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<SapperBlock> SAPPER = REGISTRATE.block("sapper", SapperBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.noOcclusion().color((MaterialColor.METAL)) )
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)) )
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<CompressorBlock> COMPRESSOR = REGISTRATE.block("compressor", CompressorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.noOcclusion().color((MaterialColor.METAL)) )
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)) )
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<BrassPressBlock> BRASS_PRESS = REGISTRATE.block("brass_press", BrassPressBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.color(MaterialColor.METAL))
            .properties(BlockBehaviour.Properties::noOcclusion)
            .transform(axeOrPickaxe())
            .blockstate(BlockStateGen.horizontalBlockProvider(true))
            .transform(BlockStressDefaults.setImpact(16.0))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LaserBlock> LASER = REGISTRATE.block("laser", LaserBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<MirrorBlock> MIRROR = REGISTRATE.block("mirror", MirrorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();
    public static final BlockEntry<SpectrometerBlock> SPECTROMETER = REGISTRATE.block("spectrometer", SpectrometerBlock::new)
            .initialProperties(SharedProperties::wooden)
            .transform(axeOrPickaxe())
            .properties(p -> p.color(MaterialColor.PODZOL))
            .blockstate(new GaugeGenerator()::generate)
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<IrradiatorBlock> IRRADIATOR = REGISTRATE.block("irradiator", IrradiatorBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .transform(BlockStressDefaults.setImpact(4.0f))
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LargeLaserBlock> LARGE_LASER = REGISTRATE.block("large_laser", LargeLaserBlock::new)
            .initialProperties(SharedProperties::netheriteMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.METAL))
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<BlackHoleBlock> BLACK_HOLE  = REGISTRATE.block("black_hole", BlackHoleBlock::new)
            .initialProperties(SharedProperties::netheriteMetal)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.COLOR_BLACK).noCollission())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<ElectrolyzerBlock> ELECTROLYZER = REGISTRATE.block("electrolyzer", ElectrolyzerBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.COLOR_GRAY).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .addLayer(() -> RenderType::cutoutMipped)
            .item(AssemblyOperatorBlockItem::new)
            .transform(customItemModel())
            .register();
/*
    public static final BlockEntry<PlanetaryGearsetBlock> PLANETARY_GEARSET = REGISTRATE.block("planetary_gearset", PlanetaryGearsetBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p
                    .noOcclusion()
                    .sound(SoundType.WOOD)
                    .color(MaterialColor.DIRT)
            ).transform(axeOrPickaxe())
            .transform(BlockStressDefaults.setNoImpact())
            .item(CogwheelBlockItem::new)
            .transform(customItemModel())
            .register();

 */


    public static final BlockEntry<UselessMachineBlock> USELESS_MACHINE = REGISTRATE.block("kinetic_machine", UselessMachineBlock::new)
            .initialProperties(SharedProperties::wooden)
            .properties(p -> p.color(MaterialColor.METAL).noOcclusion())
            .transform(axeOrPickaxe())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .transform(BlockStressDefaults.setImpact(0))
            .item()
            .transform(customItemModel())
            .register();


    public static final BlockEntry<CentrifugeBlock> CENTRIFUGE = REGISTRATE.block("centrifuge", CentrifugeBlock::new)
            .initialProperties(SharedProperties::stone)
            .properties(p -> p.sound(SoundType.METAL).color(MaterialColor.METAL))
            .transform(pickaxeOnly())
            .blockstate(BlockStateGen.axisBlockProvider(false))
            .transform(BlockStressDefaults.setImpact(8.0))
            .item()
            .transform(customItemModel())
            .register();

    public static final BlockEntry<LaserDrillBlock> LASER_DRILL = REGISTRATE.block("laser_drill", LaserDrillBlock::new)
            .initialProperties(SharedProperties::stone)
            .transform(pickaxeOnly())
            .properties(p -> p.color(MaterialColor.METAL).noOcclusion())
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)))
            .item(LaserDrillItem::new)
            .transform(customItemModel())
            .register();
}