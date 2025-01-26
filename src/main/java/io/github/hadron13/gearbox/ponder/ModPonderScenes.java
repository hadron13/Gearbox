package io.github.hadron13.gearbox.ponder;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.InputWindowElement;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import com.simibubi.create.foundation.utility.Pointing;
import com.tterrag.registrate.util.entry.BlockEntry;

import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class ModPonderScenes {

    public static void sapper(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("sapper", "Using a sapper to gather resources");
        scene.configureBasePlate(0, 0, 7);

        Selection power = util.select.fromTo(4, 1, 4, 5, 1, 7)
                .add(util.select.position(6, 0 ,7));

        Selection tree = util.select.fromTo(2, 1, 3, 2, 4, 3)
                .add(util.select.fromTo(0, 3, 1, 4, 7, 6));

        BlockPos sapper = util.grid.at(4, 1, 3);
        BlockPos pump = sapper.east(2);

        Selection pipes = util.select.fromTo(5, 1, 3, 7, 1, 3)
                .add(util.select.position(6,1,4))
                .add(util.select.position(7, 0, 3));

        scene.world.showSection(util.select.fromTo(0, 0, 0, 6, 0, 6), Direction.UP);
        scene.idle(10);
        scene.world.setKineticSpeed(util.select.position(sapper), 0f);
        scene.world.setKineticSpeed(util.select.position(pump), 0f);

        scene.world.showSection(util.select.position(sapper), Direction.DOWN);
        scene.idle(10);
        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Sappers are machines used to sap fluids from trees")
                .pointAt(util.vector.topOf(sapper));

        scene.idle(70);

        scene.world.showSection(power, Direction.DOWN);
        scene.world.setKineticSpeed(util.select.position(sapper), -32f);

        scene.overlay.showText(45)
                .placeNearTarget()
                .attachKeyFrame()
                .text("You can power them with cogs")
                .pointAt(util.vector.topOf(sapper.south()));

        scene.idle(50);

        scene.world.showSection(tree, Direction.DOWN);

        scene.idle(20);

        scene.world.showSection(pipes, Direction.DOWN);
        scene.world.setKineticSpeed(util.select.position(pump), 32f);
        scene.world.modifyBlockEntity(sapper, SapperBlockEntity.class, be -> {
            be.ponderFill(300);
        });

        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("And then pull the output fluids from the back")
                .pointAt(util.vector.topOf(sapper));

        scene.world.propagatePipeChange(util.grid.at(6, 1, 3));

        scene.idle(30);


    }

    public static void laser(SceneBuilder scene, SceneBuildingUtil util) {

        scene.title("laser", "Using a laser");
        scene.configureBasePlate(0, 0, 5);


        scene.world.showSection(util.select.fromTo(0, 0, 0, 4, 1, 4), Direction.UP);



    }

    public static void pumpjack(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("pumpjack", "Using a pumpjack to gather resources");
        scene.configureBasePlate(0, 0, 8);

        BlockPos arm = util.grid.at(3, 3, 3);
        BlockPos well = util.grid.at(1, 1, 3);
        BlockPos crank= util.grid.at(5, 1, 3);


        scene.world.showSection(util.select.fromTo(0, 0, 0, 6, 0, 6), Direction.UP);
        scene.idle(10);
        scene.world.showSection(util.select.fromTo(1, 1, 3, 5, 4, 3), Direction.DOWN);

        scene.idle(20);

        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The pumpjack is a multi-block resource extractor")
                .pointAt(Vec3.atCenterOf(arm));

        scene.idle(80);

        scene.overlay.showText(50)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It consists of 3 blocks")
                .pointAt(Vec3.atCenterOf(arm));

        scene.idle(70);

        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Arm")
                .pointAt(Vec3.atCenterOf(arm));

        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Crank")
                .pointAt(Vec3.atCenterOf(crank));

        scene.overlay.showText(60)
                .independent()
                .placeNearTarget()
                .attachKeyFrame()
                .text("Well")
                .pointAt(Vec3.atCenterOf(well));

        scene.idle(80);


        scene.world.hideSection(util.select.fromTo(0, 0, 0, 1, 0, 2), Direction.DOWN);

        scene.overlay.showText(80)
                .independent()
                .placeNearTarget()
                .attachKeyFrame()
                .text("The well needs to be connected to bedrock through pipes")
                .pointAt(Vec3.atCenterOf(well.below()));
        scene.idle(100);

        scene.world.showSection(util.select.fromTo(0, 0, 0, 1, 0, 2), Direction.UP);

        scene.rotateCameraY(90);

        scene.world.showSection(util.select.fromTo(7, 0, 2, 7, 1,3), Direction.EAST);
        scene.world.showSection(util.select.position(6, 1, 3), Direction.EAST);
        scene.idle(20);

        scene.overlay.showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("And the crank be connected to a shaft to power the machine")
                .pointAt(Vec3.atCenterOf(crank));
        scene.idle(20);
        scene.world.setKineticSpeed(util.select.position(7, 0, 2), -32f);
        scene.world.setKineticSpeed(util.select.fromTo(7, 1, 3, 5, 1,3), 64f);

        scene.idle(80);

        scene.rotateCameraY(-180);

        scene.idle(20);

        scene.overlay.showText(80)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The results can then be extracted from the well with a pipe")
                .pointAt(Vec3.atCenterOf(well));

        scene.idle(40);

        scene.world.showSection(util.select.fromTo(1, 1, 4, 7, 2, 5), Direction.DOWN);
        scene.world.setKineticSpeed(util.select.fromTo(1, 1, 4, 7, 2, 4), -64f);
        scene.world.setKineticSpeed(util.select.position(2, 1, 5), 64f);

        scene.world.propagatePipeChange(util.grid.at(2, 1, 5));
        scene.idle(80);
        scene.rotateCameraY(90);



    }

}
