package io.github.hadron13.gearbox.blocks.black_hole;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BBHelper;
import com.simibubi.create.foundation.utility.Pair;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.EndGatewayBlock;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class BlackHoleBlockEntity extends SmartBlockEntity implements ILaserReceiver {

    public static List<BlackHoleBlockEntity> blackHoles = new ArrayList<>();

    public LaserBeamBehavior beamBehavior;
    public List<Entity> entitiesInside;

    public BlackHoleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        blackHoles.add(this);
    }

    @Override
    public void tick(){

        if(level == null || level.isClientSide)
            return;
//        if (level instanceof ServerLevel && !pEntity.isPassenger() && !pEntity.isVehicle() && pEntity.canChangeDimensions() && Shapes.joinIsNotEmpty(Shapes.create(pEntity.getBoundingBox().move((double)(-pPos.getX()), (double)(-pPos.getY()), (double)(-pPos.getZ()))), pState.getShape(pLevel, pPos), BooleanOp.AND)) {
//            ResourceKey<Level> resourcekey = pLevel.dimension() == Level.END ? Level.OVERWORLD : Level.END;
//            ServerLevel serverlevel = ((ServerLevel)pLevel).getServer().getLevel(resourcekey);
//            if (serverlevel == null) {
//                return;
//            }
//
//            pEntity.changeDimension(serverlevel);
//        }

        if(blackHoles.size() < 2)
            return;

        AABB teleportCollision = AABB.ofSize(Vec3.atCenterOf(getBlockPos()), 1.0f, 1.0f, 1.0f);
        AABB attractionCollision = AABB.ofSize(Vec3.atCenterOf(getBlockPos()), 20.0f, 20.0f, 20.0f);

        entitiesInside = level.getEntities(null, teleportCollision);
        BlackHoleBlockEntity match;
        BlockPos teleportPos;
        do{
            teleportPos = blackHoles.get(level.random.nextInt(blackHoles.size())).getBlockPos();
        }while (teleportPos == getBlockPos());


        for(Entity entity : entitiesInside){
            if(entity instanceof ItemEntity){
                continue;
            }

            if(!entity.isOnPortalCooldown()) {
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, .8f, 0));
                if(entity instanceof Player player){

                }

//                level.explode(null, teleportPos.getX() + 1, teleportPos.getY() + 3, teleportPos.getZ(), 3.0f, Explosion.BlockInteraction.NONE);
                entity.teleportToWithTicket(teleportPos.getX() + 1 , teleportPos.getY() + 3, teleportPos.getZ() + 1);
//                entity.push
                entity.setPortalCooldown();
            }
        }


    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
    }


    public void remove() {
        blackHoles.remove(this);
    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {
        return true;
    }
}
