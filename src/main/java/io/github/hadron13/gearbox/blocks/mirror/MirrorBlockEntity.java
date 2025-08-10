package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MirrorBlockEntity extends KineticBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Map<Laser, Laser> lasers = new HashMap<>();
    public AABB renderBoundingBox;
    public float angle = 0;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
        }
        return renderBoundingBox;
    }
    public MirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);


    }



    @Override
    public void tick() {
        super.tick();
        //angle.tickChaser();
        angle += getSpeed()/8;

    }

    @Override
    public void receiveLaser(Laser laser) {
        

        if(Math.abs(getNormal().dot(laser.direction)) < 0.5f){
            if(lasers.containsKey(laser)){
                endReceiveLaser(laser);
            }
            return;
        }
        if(!lasers.containsKey(laser)){
            lasers.put(laser, new Laser(laser.color, getBlockPos().getCenter(),  getNormal()));
        }
        Laser reflected = lasers.get(laser);
        reflected.setDirection(reflect(laser.direction, getNormal()));
        reflected.tick(this.getLevel());
    }

    //I - 2.0 * dot(N, I) * N
    public static Vec3 reflect(Vec3 incident, Vec3 normal){
        return incident.subtract(normal.scale(2.0 * normal.dot(incident)));
    }
    public Vec3 getNormal(){
        return Vec3.atLowerCornerOf(Direction.NORTH.getNormal()).yRot(angle * Mth.DEG_TO_RAD);
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        if(lasers.containsKey(laser)) {
            lasers.get(laser).disable();
            lasers.remove(laser);
        }
    }

    @Override
    public List<Laser> getLasers() {
        return lasers.values().stream().toList();
    }
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("angle", angle);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        angle = compound.getFloat("angle");
    }



}
