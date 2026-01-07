package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.ponder.scenes.fluids.CompressorScenes;
import io.github.hadron13.gearbox.ponder.scenes.fluids.PumpjackScenes;
import io.github.hadron13.gearbox.ponder.scenes.fluids.SapperScenes;
import io.github.hadron13.gearbox.ponder.scenes.kinetics.KilnScenes;
import io.github.hadron13.gearbox.ponder.scenes.lasers.LaserScenes;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;
import static io.github.hadron13.gearbox.ponder.GearboxPonderTags.LASER_STUFF;

public class GearboxPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(GearboxBlocks.COMPRESSOR)
                .addStoryBoard("compressor", CompressorScenes::compressor, KINETIC_APPLIANCES, FLUIDS);

        HELPER.forComponents(GearboxBlocks.SAPPER)
                .addStoryBoard("sapper", SapperScenes::sapper, KINETIC_APPLIANCES, FLUIDS);

        HELPER.forComponents(GearboxBlocks.PUMPJACK_WELL, GearboxBlocks.PUMPJACK_CRANK, GearboxBlocks.PUMPJACK_ARM)
                .addStoryBoard("pumpjack", PumpjackScenes::pumpjack, FLUIDS);

        HELPER.forComponents(GearboxBlocks.KILN)
                .addStoryBoard("kiln", KilnScenes::kiln, KINETIC_APPLIANCES);

        HELPER.forComponents(GearboxBlocks.LASER)
                .addStoryBoard("laser", LaserScenes::laser, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.MIRROR)
                .addStoryBoard("mirror", LaserScenes::mirror, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.PRISM)
                .addStoryBoard("prism", LaserScenes::prism, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.COMBINER)
                .addStoryBoard("combiner", LaserScenes::combiner, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.ATTENUATOR)
                .addStoryBoard("attenuator", LaserScenes::attenuator, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.AMPLIFIER)
                .addStoryBoard("amplifier", LaserScenes::amplifier, LASER_STUFF);

        HELPER.forComponents(GearboxBlocks.SPECTROMETER)
                .addStoryBoard("spectrometer", LaserScenes::spectrometer, LASER_STUFF);
    }

}
