package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;

public class ModPonderTags {

    public static final ResourceLocation
            LASER_STUFF = Gearbox.asResource("laser_stuff")
                    ;

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        helper.registerTag(LASER_STUFF)
                .addToIndex()
                .item(ModBlocks.LASER.get(), true, false)
                .title("Lasers")
                .description("Machines that emmit or interact with lasers")
                .register();

        HELPER.addToTag(FLUIDS)
                .add(ModBlocks.SAPPER)
                .add(ModBlocks.COMPRESSOR)
                .add(ModBlocks.CENTRIFUGE)
                .add(ModBlocks.ELECTROLYZER)
                .add(ModBlocks.PUMPJACK_WELL)
                .add(ModBlocks.DIPPER);

        HELPER.addToTag(KINETIC_APPLIANCES)
                .add(ModBlocks.SAPPER)
                .add(ModBlocks.COMPRESSOR)
                .add(ModBlocks.CENTRIFUGE)
                .add(ModBlocks.KILN)
                .add(ModBlocks.PUMPJACK_CRANK);

        HELPER.addToTag(LASER_STUFF)
                .add(ModBlocks.LASER)
                .add(ModBlocks.LARGE_LASER)
                .add(ModBlocks.IRRADIATOR)
                .add(ModBlocks.MIRROR)
                .add(ModBlocks.LASER_DRILL)
                .add(ModBlocks.SPECTROMETER);
    }
}
