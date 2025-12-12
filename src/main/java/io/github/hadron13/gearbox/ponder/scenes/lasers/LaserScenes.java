package io.github.hadron13.gearbox.ponder.scenes.lasers;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import io.github.hadron13.gearbox.blocks.amplifier.AmplifierBlockEntity;
import io.github.hadron13.gearbox.blocks.attenuator.AttenuatorBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlock;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
import io.github.hadron13.gearbox.blocks.precision_crank.PrecisionCrankBlock;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.data.ModDamageTypes;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.WalkAnimationState;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.IFluidHandler;

import static io.github.hadron13.gearbox.register.data.ModDamageTypes.laser;

public class LaserScenes {
    public static void laser(SceneBuilder scene, SceneBuildingUtil util) {

        scene.title("laser", "Using a laser emitter");
        scene.configureBasePlate(0, 0, 5);
        ;

        BlockPos laserPos = util.grid().at(2, 1, 3);


        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().fromTo(0, 1, 0, 4, 1, 4), Direction.DOWN);

        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The laser emitter is the basis of all laser machinery")
                .pointAt(Vec3.atCenterOf(laserPos));

        scene.idle(80);
        scene.rotateCameraY(-120);
        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It can be powered with energy from the back...")
                .pointAt(Vec3.atCenterOf(laserPos).add(0, 0, 0.5));
        scene.idle(70);

        scene.rotateCameraY(120);
        scene.idle(15);

        scene.world().modifyBlockEntity(laserPos, LaserBlockEntity.class, be -> be.laserBeam.enable());

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("... to emit a powerful beam in the front")
                .pointAt(Vec3.atCenterOf(laserPos).add(0, 0, -0.5));

        BlockPos stone = util.grid().at(2, 1, 0);
        scene.world().hideSection(util.select().position(stone), Direction.DOWN);
        scene.idle(90);




        scene.world().setBlock(stone, Blocks.STONE.defaultBlockState(), true);
        scene.world().showSection(util.select().position(stone), Direction.DOWN);


        scene.overlay().showText(50)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The beam will destroy everything in it's path...")
                .pointAt(Vec3.atCenterOf(stone));

        ElementLink<EntityElement> mob = scene.world().createEntity(w -> {
            Sheep entity = EntityType.SHEEP.create(w);
            Vec3 p = util.vector().topOf(util.grid().at(2, 0, 2)).add(0, 0, -0.5);
            entity.setPos(p.x, p.y, p.z);
            entity.xo = p.x;
            entity.yo = p.y;
            entity.zo = p.z;
            WalkAnimationState animation = entity.walkAnimation;
            animation.update(-animation.position(), 1);
            animation.setSpeed(1);
            entity.yRotO = 90;
            entity.setYRot(90);
            entity.yHeadRotO = 90;
            entity.yHeadRot = 90;
            entity.setSharedFlagOnFire(true);
            entity.animateHurt(90);
            entity.displayFireAnimation();
            return entity;
        });

        scene.idle(25);

        scene.world().modifyEntity(mob, e -> {
            if(e instanceof Sheep sheep) {
                sheep.animateHurt(90);
                sheep.displayFireAnimation();
            }
        });
        scene.world().modifyEntity(mob, Entity::discard);
        scene.idle(40);
        scene.world().setBlock(stone, Blocks.AIR.defaultBlockState(), true);

        scene.world().hideSection(util.select().fromTo(2, 1, 0, 2, 1, 2), Direction.UP);
        scene.idle(20);



        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("... with the exception of some special blocks")
                .pointAt(Vec3.atCenterOf(laserPos.north()));
        scene.idle(5);

        scene.world().setBlock(util.grid().at(2, 1, 2), ModBlocks.SPECTROMETER.getDefaultState().setValue(SpectrometerBlock.AXIS_ALONG_FIRST_COORDINATE, false).setValue(SpectrometerBlock.FACING, Direction.UP), false);
        scene.world().setBlock(util.grid().at(2, 1, 0), ModBlocks.MIRROR.getDefaultState().setValue(MirrorBlock.AXIS, Direction.Axis.Y), false);
        scene.world().modifyBlockEntity(util.grid().at(2, 1, 0), MirrorBlockEntity.class, mirror -> {
            mirror.angle = 45f + 90f;
        });

        scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(2, 1, 0), Direction.DOWN);


        scene.idle(30);
    }

    public static void mirror(SceneBuilder builder, SceneBuildingUtil util){
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("mirror", "Reflecting lasers with a mirror");
        scene.configureBasePlate(0, 0, 5);


        BlockPos mirror = util.grid().at(2, 1, 3);
        BlockPos laser = util.grid().at(2, 1, 0);
        BlockPos valvePos = util.grid().at(2, 2, 3);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().position(mirror), Direction.DOWN);


        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("Mirrors are blocks that reflect lasers")
                .pointAt(Vec3.atCenterOf(mirror));

        scene.idle(80);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("You can use a valve handle to adjust the mirror angle")
                .pointAt(Vec3.atCenterOf(valvePos));

        scene.idle(5);
        ElementLink<WorldSectionElement> valve = scene.world().showIndependentSection(util.select().position(valvePos), Direction.DOWN);
        scene.idle(30);
        scene.overlay().showControls(valvePos.getCenter(), Pointing.DOWN, 40).rightClick();
        scene.world().setKineticSpeed(util.select().fromTo(mirror, valvePos), 32);
        scene.world().rotateSection(valve, 0, 180, 0, 20);
        scene.idle(10);
        scene.world().showSection(util.select().position(laser), Direction.DOWN);
        scene.idle(10);
        scene.world().setKineticSpeed(util.select().fromTo(mirror, valvePos), 0);

        scene.world().modifyBlockEntity(laser, LaserBlockEntity.class, be -> be.laserBeam.enable());

        scene.idle(30);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("or a precision crank for fine tuning")
                .pointAt(Vec3.atCenterOf(valvePos));

        scene.world().replaceBlocks(util.select().position(valvePos), ModBlocks.PRECISION_CRANK.getDefaultState().setValue(PrecisionCrankBlock.FACING, Direction.UP), true);
        scene.idle(10);
        scene.overlay().showControls(valvePos.getCenter(), Pointing.DOWN, 60).rightClick();
        scene.world().setKineticSpeed(util.select().fromTo(mirror, valvePos), 1);
        scene.idle(60);
    }


    public static void prism(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("prism", "Decomposing lasers with a prism");
        scene.configureBasePlate(0, 0, 5);

        BlockPos prismPos = util.grid().at(2, 1, 2);
        BlockPos laserPos = util.grid().at(2, 1, 4);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().position(prismPos), Direction.DOWN);
        scene.idle(5);
        scene.world().showSection(util.select().position(laserPos), Direction.DOWN);

        scene.idle(20);
        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("A Prism can decompose a laser into it's RGB components")
                .pointAt(Vec3.atCenterOf(prismPos));

        scene.idle(40);

        scene.world().modifyBlockEntity(laserPos, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);

        scene.idle(30);
        scene.rotateCameraY(120);

        scene.idle(10);

        scene.addKeyframe();
        scene.overlay().showText(60)
                .placeNearTarget()
                .text("Red")
                .colored(PonderPalette.RED)
                .pointAt(Vec3.atCenterOf(prismPos.north(3)));

        scene.overlay().showText(60)
                .placeNearTarget()
                .text("Green")
                .colored(PonderPalette.GREEN)
                .pointAt(Vec3.atCenterOf(prismPos.north(4)).add(0, 0.4, 0));

        scene.overlay().showText(60)
                .placeNearTarget()
                .text("Blue")
                .colored(PonderPalette.BLUE)
                .pointAt(Vec3.atCenterOf(prismPos.north(5)).add(0, 1, 0));

        scene.idle(60);
    }


    public static void combiner(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("combiner", "Combining lasers with a combiner");
        scene.configureBasePlate(0, 0, 5);

        BlockPos combiner = util.grid().at(2, 1, 1);
        BlockPos laser1 = util.grid().at(0, 1, 2);
        BlockPos laser2 = util.grid().at(4, 1, 2);
        BlockPos laser3 = util.grid().at(2, 1, 4);
        BlockPos mirror1 = util.grid().at(0, 1, 4);
        BlockPos mirror2 = util.grid().at(4, 1, 4);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().position(combiner), Direction.DOWN);

        scene.idle(20);
        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Combiner can combine multiple lasers into one")
                .pointAt(Vec3.atCenterOf(combiner));

        scene.idle(30);
        scene.world().showSection(util.select().position(laser3), Direction.DOWN);
        scene.idle(10);
        scene.world().modifyBlockEntity(laser3, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);

        scene.idle(10);
        scene.world().showSection(util.select().position(laser1), Direction.DOWN);
        scene.world().showSection(util.select().position(mirror1), Direction.DOWN);
        scene.idle(10);
        scene.world().modifyBlockEntity(laser1, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);

        scene.idle(10);
        scene.world().showSection(util.select().position(laser2), Direction.DOWN);
        scene.world().showSection(util.select().position(mirror2), Direction.DOWN);
        scene.idle(10);
        scene.world().modifyBlockEntity(laser2, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);

        scene.idle(10);
        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The resulting laser has combined power and color of all the inputs")
                .pointAt(Vec3.atCenterOf(combiner));
        scene.idle(70);

        scene.rotateCameraY(120);

    }


    public static void attenuator(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("attenuator", "Attenuating lasers with the attenuator");
        scene.configureBasePlate(0, 0, 5);

        BlockPos attenuator = util.grid().at(2, 1, 2);
        BlockPos laser = util.grid().at(2, 1, 4);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().position(attenuator), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().position(laser), Direction.DOWN);


        scene.idle(20);
        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Attenuator can reduce the power level of a passing laser")
                .pointAt(Vec3.atCenterOf(attenuator));

        scene.idle(20);
        scene.world().modifyBlockEntity(laser, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);
        scene.idle(60);

        Vec3 blockSurface = util.vector().blockSurface(attenuator, Direction.WEST)
                .add(0, 0, 0);
        scene.overlay().showFilterSlotInput(blockSurface, Direction.WEST, 80);
        scene.overlay().showControls(blockSurface, Pointing.DOWN, 60).rightClick();
        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The attenuation percentage can be tuned on its input panel")
                .pointAt(blockSurface);

        scene.world().modifyBlockEntity(attenuator, AttenuatorBlockEntity.class, be ->be.apertureSize.setValue(50));
        scene.idle(70);
        scene.rotateCameraY(120);
    }


    public static void amplifier(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("amplifier", "Amplifying lasers with the amplifier");
        scene.configureBasePlate(0, 0, 5);

        BlockPos amplifier = util.grid().at(2, 1, 2);
        BlockPos laser = util.grid().at(2, 1, 4);

        scene.world().showSection(util.select().fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(10);
        scene.world().showSection(util.select().position(amplifier), Direction.DOWN);
        scene.idle(10);
        scene.world().showSection(util.select().position(laser), Direction.DOWN);


        scene.idle(20);
        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The Amplifier can increase the power level of a passing laser by a percentage")
                .pointAt(Vec3.atCenterOf(amplifier));

        scene.idle(20);
        scene.world().modifyBlockEntity(laser, LaserBlockEntity.class, LaserBlockEntity::ponderEnableLaser);
        scene.idle(60);


        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("It requires energy input from the side for any amplification")
                .pointAt(util.vector().blockSurface(amplifier, Direction.WEST));

        scene.idle(60);
        scene.rotateCameraY(120);
        scene.idle(10);


        Vec3 blockSurface = util.vector().blockSurface(amplifier, Direction.EAST)
                .add(0, 0, 0);
        scene.overlay().showFilterSlotInput(blockSurface, Direction.EAST, 80);
        scene.overlay().showControls(blockSurface, Pointing.DOWN, 60).rightClick();
        scene.idle(20);

        scene.overlay().showText(60)
                .placeNearTarget()
                .attachKeyFrame()
                .text("The amplification percentage can be tuned on its input panel")
                .pointAt(blockSurface);

        scene.world().modifyBlockEntity(amplifier, AmplifierBlockEntity.class, be ->be.amplification.setValue(100));
        scene.idle(70);
        scene.rotateCameraY(-120);
    }
}
