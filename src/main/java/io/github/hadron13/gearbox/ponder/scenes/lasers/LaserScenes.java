package io.github.hadron13.gearbox.ponder.scenes.lasers;

import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlock;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
import io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.data.ModDamageTypes;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
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
}
