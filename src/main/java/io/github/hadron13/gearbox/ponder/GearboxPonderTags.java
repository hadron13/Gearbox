package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class GearboxPonderTags {

    public static final ResourceLocation
            LASER_STUFF = Gearbox.asResource("laser_stuff")
                    ;

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(LASER_STUFF)
                .addToIndex()
                .item(GearboxBlocks.LASER.get(), true, false)
                .title("Lasers")
                .description("Machines that emmit or interact with lasers")
                .register();

        HELPER.addToTag(FLUIDS)
                .add(GearboxBlocks.SAPPER)
                .add(GearboxBlocks.COMPRESSOR)
                .add(GearboxBlocks.CENTRIFUGE)
                .add(GearboxBlocks.ELECTROLYZER)
                .add(GearboxBlocks.PUMPJACK_WELL)
                .add(GearboxBlocks.STEEL_FLUID_PIPE)
                .add(GearboxBlocks.STEEL_FLUID_TANK)
                .add(GearboxBlocks.DISTILLATION_CONTROLLER)
                .add(GearboxBlocks.DISTILLATION_OUTPUT);
//                .add(GearboxBlocks.DIPPER);

        HELPER.addToTag(KINETIC_APPLIANCES)
                .add(GearboxBlocks.SAPPER)
                .add(GearboxBlocks.COMPRESSOR)
                .add(GearboxBlocks.CENTRIFUGE)
                .add(GearboxBlocks.KILN)
                .add(GearboxBlocks.PUMPJACK_CRANK)
                .add(GearboxBlocks.CORE_DRILL);

        HELPER.addToTag(LASER_STUFF)
                .add(GearboxBlocks.LASER)
                .add(GearboxBlocks.AMPLIFIER)
                .add(GearboxBlocks.ATTENUATOR)
                .add(GearboxBlocks.MIRROR)
                .add(GearboxBlocks.PRECISION_CRANK)
                .add(GearboxBlocks.COMBINER)
                .add(GearboxBlocks.IRRADIATOR)
                .add(GearboxBlocks.SPECTROMETER);
    }
}
