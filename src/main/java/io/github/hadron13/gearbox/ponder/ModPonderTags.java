package io.github.hadron13.gearbox.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.infrastructure.ponder.AllPonderTags;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModBlocks;

import static com.simibubi.create.infrastructure.ponder.AllPonderTags.FLUIDS;
import static com.simibubi.create.infrastructure.ponder.AllPonderTags.KINETIC_APPLIANCES;

public class ModPonderTags {

    private static PonderTag create(String id) {
        return new PonderTag(Gearbox.asResource(id));
    }
    public static final PonderTag LASER_STUFF= create("laser_stuff").item(ModBlocks.LASER.get())
            .defaultLang("Lasers", "Machines that emmit or interact with lasers")
            .addToIndex();


    public static void register() {
        PonderRegistry.TAGS.forTag(FLUIDS)
                .add(ModBlocks.SAPPER)
                .add(ModBlocks.COMPRESSOR)
                .add(ModBlocks.CENTRIFUGE)
                .add(ModBlocks.ELECTROLYZER)
                .add(ModBlocks.PUMPJACK_WELL)
                .add(ModBlocks.DIPPER);

        PonderRegistry.TAGS.forTag(KINETIC_APPLIANCES)
                .add(ModBlocks.SAPPER)
                .add(ModBlocks.COMPRESSOR)
                .add(ModBlocks.CENTRIFUGE)
                .add(ModBlocks.PUMPJACK_CRANK);

        PonderRegistry.TAGS.forTag(LASER_STUFF)
                .add(ModBlocks.LASER)
                .add(ModBlocks.LARGE_LASER)
                .add(ModBlocks.IRRADIATOR)
                .add(ModBlocks.MIRROR)
                .add(ModBlocks.LASER_DRILL)
                .add(ModBlocks.SPECTROMETER);

    }
}
