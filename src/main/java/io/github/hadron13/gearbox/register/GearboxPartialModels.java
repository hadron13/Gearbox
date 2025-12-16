package io.github.hadron13.gearbox.register;

import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.hadron13.gearbox.Gearbox;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Direction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GearboxPartialModels {
    public static final PartialModel
            SAPPER_HEAD = block("sapper/head"),
            SAPPER_POLE = block("sapper/pole"),
            COMPRESSOR_ROLL = block("compressor/roll"),
            BRASS_PRESS_HEAD = block("brass_press/head"),
            BRASS_PRESS_POLE = block("brass_press/pole"),
            OUTER_LASER_BEAM = block("laser/laser_beam"),
            SPECTROGAUGE = block("spectrometer/head"),
            IRRADIATOR_LENS = block("irradiator/lens"),
            THICK_BEAM = block("irradiator/thick_beam"),
            LARGE_LASER_LENS = block("large_laser/lens"),
            ELECTROLYZER_HEAD = block("electrolyzer/head"),
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
            MIRROR = block("mirror/mirror"),
            PRECISION_CRANK = block("precision_crank/block"),
            SHAFT_DUAL_TINY = block("shaft_dual_tiny"),
            ULTIMATE_MECH_CORE = item("ultimate_mechanism/core"),
            TAU_CANNON_COIL = item("tau_cannon/coil"),
            CORE_DRILL_TUBE = item("core_tube"),
            ORE_CORE = item("ore_core"),
            STEEL_FLUID_PIPE_CASING = block("steel_fluid_pipe/casing");


    public static final Map<FluidTransportBehaviour.AttachmentTypes.ComponentPartials, Map<Direction, PartialModel>> STEEL_PIPE_ATTACHMENTS =
            new EnumMap<>(FluidTransportBehaviour.AttachmentTypes.ComponentPartials.class);



    static {
        for (FluidTransportBehaviour.AttachmentTypes.ComponentPartials type : FluidTransportBehaviour.AttachmentTypes.ComponentPartials
                .values()) {
            Map<Direction, PartialModel> map = new HashMap<>();
            for (Direction d : Iterate.directions) {
                String asId = Lang.asId(type.name());
                map.put(d, block("steel_fluid_pipe/" + asId + "/" + Lang.asId(d.getSerializedName())));
            }
            STEEL_PIPE_ATTACHMENTS.put(type, map);
        }
    }

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
