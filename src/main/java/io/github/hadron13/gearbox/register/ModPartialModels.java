package io.github.hadron13.gearbox.register;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.Create;
import io.github.hadron13.gearbox.Gearbox;

public class ModPartialModels {
    public static final PartialModel
            SAPPER_HEAD = block("sapper/head"),
            SAPPER_POLE = block("sapper/pole"),
            COMPRESSOR_ROLL = block("compressor/roll"),
            BRASS_PRESS_HEAD = block("brass_press/head"),
            BRASS_PRESS_POLE = block("brass_press/pole"),
            LASER_BEAM = block("lasers/laser_beam"),
            SPECTROGAUGE = block("spectrometer/head"),
            IRRADIATOR_LENS = block("irradiator/lens"),
            THICK_BEAM = block("irradiator/thick_beam"),
            LARGE_LASER_LENS = block("large_laser/lens"),
            ELECTROLYZER_HEAD = block("electrolyzer/head"),
            PG_RING_GEAR = block("planetary/ring_gear"),
            PG_SUN_GEAR = block("planetary/sun_gear"),
            PG_PLANET_GEAR = block("planetary/planet_gear"),
            USELESS_COG = block("useless/cog");

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
