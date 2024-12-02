package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraft.core.BlockPos;
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




        public static void register() {}
}
