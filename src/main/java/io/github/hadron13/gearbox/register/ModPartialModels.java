package io.github.hadron13.gearbox.register;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;
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
            USELESS_COG = block("kinetic_machine/cog"),
            CENTRIFUGE_COG = block("centrifuge/cogwheel"),
            LASER_DRILL_HEAD = block("laser_drill/head"),
            PUMPJACK_ARM = block("pumpjack/arm"),
            PUMPJACK_HEAD = block("pumpjack/head"),
            PUMPJACK_CONNECTOR = block("pumpjack/connector"),
            PUMPJACK_PITMAN = block("pumpjack/pitman"),
            PUMPJACK_CRANK = block("pumpjack/crank"),
            PUMPJACK_SMOOTHROD = block("pumpjack/smooth_rod"),
            PUMPJACK_TRUSS = block("pumpjack/truss"),
            DIPPER_POLE = block("dipper/pole"),

            ULTIMATE_MECH_CORE = item("ultimate_mechanism/core"),
            TAU_CANNON_COIL = item("tau_cannon/coil");


    private static PartialModel block(String path) {
        return PartialModel.of(Gearbox.asResource("block/" + path));
    }

    private static PartialModel item(String path) {
        return PartialModel.of(Gearbox.asResource("item/" + path));
    }

    private static PartialModel entity(String path) {
        return PartialModel.of(Gearbox.asResource("entity/" + path));
    }

    public static void init() {
        // init static fields
    }

}
