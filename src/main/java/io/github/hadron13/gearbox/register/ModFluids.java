package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ModFluids {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static final FluidEntry<ForgeFlowingFluid.Flowing> PETROLEUM = REGISTRATE
            .fluid("petroleum",
                    Gearbox.asResource("fluid/petroleum_still"),
                    Gearbox.asResource("fluid/petroleum_flow"))
            .lang("Petroleum")
            .attributes(b -> b.viscosity(2000).density(1500))
            .properties(p -> p.levelDecreasePerBlock(3)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(AllTags.forgeItemTag("buckets/petroleum"))
            .build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> RESIN = REGISTRATE
            .fluid("resin",
                    Gearbox.asResource("fluid/resin_still"),
                    Gearbox.asResource("fluid/resin_flow"))
            .lang("Resin")
            .attributes(b -> b.viscosity(2000).density(1500))
            .properties(p -> p.levelDecreasePerBlock(3)
                    .tickRate(25)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .tag(AllTags.forgeItemTag("buckets/resin"))
            .build().register();

    public static final FluidEntry<ForgeFlowingFluid.Flowing> NITROGEN = gas("nitrogen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> OXYGEN = gas("oxygen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN = gas("hydrogen");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> ARGON = gas("argon");

    public static FluidEntry<ForgeFlowingFluid.Flowing> gas(String name){
        return REGISTRATE
            .fluid(name,
                    Gearbox.asResource("fluid/" + name + "_still"),
                    Gearbox.asResource("fluid/" + name + "_flow"),
                    TransparentFluidAttributes::new)
            .attributes(b -> b.viscosity(0)
                    .density(-100).gaseous())
            .properties(p -> p.levelDecreasePerBlock(7)
                    .tickRate(1)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .build()
            .register();
    }


    public static class TransparentFluidAttributes extends FluidAttributes {

        protected TransparentFluidAttributes(Builder builder, Fluid fluid) {
            super(builder, fluid);
        }

        @Override
        public int getColor(BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

    }

    public static void register() {}
}
