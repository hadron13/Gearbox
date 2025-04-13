package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.FluidEntry;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import java.util.function.Consumer;

public class ModFluids {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate().setCreativeTab(ModCreativeTabs.MAIN_TAB);


    public static final FluidEntry<ForgeFlowingFluid.Flowing> PETROLEUM = REGISTRATE
            .fluid("petroleum",
                    Gearbox.asResource("fluid/petroleum_still"),
                    Gearbox.asResource("fluid/petroleum_flow"))
            .lang("Petroleum")
            .properties(p -> p.viscosity(2000).density(1500))
            .fluidProperties(p -> p.levelDecreasePerBlock(3)
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
            .properties(p -> p.density(1500).viscosity(2000))
            .fluidProperties(p -> p.levelDecreasePerBlock(3)
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

    public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM = gas("steam");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> AMMONIA = gas("ammonia");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> CHLORINE = gas("chlorine");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> DINITROGEN_TETROXIDE = gas("dinitrogen_tetroxide");
    public static final FluidEntry<ForgeFlowingFluid.Flowing> HYDROGEN_SULFIDE = gas("hydrogen_sulfide");

    public static FluidEntry<ForgeFlowingFluid.Flowing> gas(String name){
        return REGISTRATE
            .fluid(name, Gearbox.asResource("fluid/" + name + "_still"), Gearbox.asResource("fluid/" + name + "_flow"), TransparentFluidType::new)
            .properties(p -> p.viscosity(0).density(-100))
            .fluidProperties(p -> p.levelDecreasePerBlock(7)
                    .tickRate(1)
                    .slopeFindDistance(3)
                    .explosionResistance(100f))
            .source(ForgeFlowingFluid.Source::new)
            .bucket()
            .build()
            .register();
    }

    public static class TransparentFluidType extends FluidType {
        private ResourceLocation stillTexture;
        private ResourceLocation flowingTexture;

        protected TransparentFluidType(FluidType.Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties);
            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                public ResourceLocation getStillTexture() {
                    return TransparentFluidType.this.stillTexture;
                }

                public ResourceLocation getFlowingTexture() {
                    return TransparentFluidType.this.flowingTexture;
                }

                @Override
                public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                    return 0x00ffffff;
                }
            });
        }
    }

    public static void register() {}
}
