package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class ModFluids {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);
//    public static final FluidEntry<ForgeFlowingFluid.Flowing> RESIN = REGISTRATE.standardFluid("resin")
//                    .lang("Resin")
//                    .attributes(b -> b.viscosity(2000)
//                            .density(1400))
//                    .properties(p -> p.levelDecreasePerBlock(2)
//                            .tickRate(25)
//                            .slopeFindDistance(3)
//                            .explosionResistance(100f))
//                    .source(ForgeFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
//                    .bucket()
//                    .tag(AllTags.forgeItemTag("buckets/resin"))
//                    .build()
//                    .register();

    public static void register() {}
}
