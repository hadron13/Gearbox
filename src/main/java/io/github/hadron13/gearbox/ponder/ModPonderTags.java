package io.github.hadron13.gearbox.ponder;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModBlocks;

import static com.simibubi.create.infrastructure.ponder.AllPonderTags.FLUIDS;

public class ModPonderTags {


    private static PonderTag create(String id) {
        return new PonderTag(Gearbox.asResource(id));
    }

    public static void register() {
        PonderRegistry.TAGS.forTag(FLUIDS).add(ModBlocks.SAPPER);
    }
}
