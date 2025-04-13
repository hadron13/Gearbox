package io.github.hadron13.gearbox.ponder.scenes.lasers;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;

public class LaserScenes {
    public static void laser(SceneBuilder scene, SceneBuildingUtil util) {

        scene.title("laser", "Using a laser");
        scene.configureBasePlate(0, 0, 5);


        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 1, 4), Direction.UP);



    }
}
