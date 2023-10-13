package io.github.hadron13.gearbox.register;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;
import io.github.hadron13.gearbox.Gearbox;

public class ModPartialModels {
    public static final PartialModel    SAPPER_HEAD = block("sapper/head"),
                                        SAPPER_POLE = block("sapper/pole"),
                                        COMPRESSOR_ROLL = block("compressor/roll");

    private static PartialModel block(String path) {
        return new PartialModel(Gearbox.asResource("block/" + path));
    }

    private static PartialModel entity(String path) {
        return new PartialModel(Gearbox.asResource("entity/" + path));
    }

    public static void init() {
        // init static fields
    }

}
