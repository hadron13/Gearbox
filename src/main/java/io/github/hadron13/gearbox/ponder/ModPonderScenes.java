package io.github.hadron13.gearbox.ponder;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import io.github.hadron13.gearbox.ponder.scenes.fluids.CompressorScenes;
import io.github.hadron13.gearbox.ponder.scenes.fluids.PumpjackScenes;
import io.github.hadron13.gearbox.ponder.scenes.fluids.SapperScenes;
import io.github.hadron13.gearbox.ponder.scenes.kinetics.KilnScenes;
import io.github.hadron13.gearbox.ponder.scenes.lasers.LaserScenes;
import io.github.hadron13.gearbox.register.ModBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllCreatePonderTags.KINETIC_APPLIANCES;
import static io.github.hadron13.gearbox.ponder.ModPonderTags.LASER_STUFF;

public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(ModBlocks.COMPRESSOR)
                .addStoryBoard("compressor", CompressorScenes::compressor, KINETIC_APPLIANCES, FLUIDS);

        HELPER.forComponents(ModBlocks.SAPPER)
                .addStoryBoard("sapper", SapperScenes::sapper, KINETIC_APPLIANCES, FLUIDS);

        HELPER.forComponents(ModBlocks.PUMPJACK_WELL, ModBlocks.PUMPJACK_CRANK, ModBlocks.PUMPJACK_ARM)
                .addStoryBoard("pumpjack", PumpjackScenes::pumpjack, FLUIDS);

        HELPER.forComponents(ModBlocks.KILN)
                .addStoryBoard("kiln", KilnScenes::kiln, KINETIC_APPLIANCES);

        HELPER.forComponents(ModBlocks.LASER)
                .addStoryBoard("laser", LaserScenes::laser, LASER_STUFF);
    }

}
