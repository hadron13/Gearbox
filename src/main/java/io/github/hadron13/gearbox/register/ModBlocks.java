package io.github.hadron13.gearbox.register;

import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.entry.BlockEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerBlock;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlock;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MaterialColor;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
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
//            .properties(BlockBehaviour.Properties::noOcclusion)
            .properties(p -> p.noOcclusion().color((MaterialColor.METAL)) )
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((c, p) -> p.simpleBlock(c.get(), AssetLookup.standardModel(c, p)) )
            .transform(BlockStressDefaults.setImpact(4.0))
            .item()
            .transform(customItemModel())
            .register();

}