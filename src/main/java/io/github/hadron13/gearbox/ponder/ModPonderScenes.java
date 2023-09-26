package io.github.hadron13.gearbox.ponder;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
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

import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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


}
