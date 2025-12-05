package io.github.hadron13.gearbox.blocks.prism;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.blocks.mirror.MirrorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static io.github.hadron13.gearbox.blocks.prism.PrismBlock.HORIZONTAL_AXIS;

public class PrismBlockEntity extends SmartBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Map<Laser, Laser[]> lasers = new HashMap<>();

    public List<Laser> laserList;
    public boolean changed = true;


    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(100);
    }

    public PrismBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void tick() {
        super.tick();
        for(Laser l : getLasers()){
            l.tick(level);
        }
    }

    @Override
    public List<Laser> getLasers() {
        if(changed) {
            laserList = new ArrayList<>();
            for (Laser[] array : lasers.values()) {
                laserList.addAll(Arrays.stream(array).filter(Objects::nonNull).toList());
            }
        }
        return laserList;
    }

    public Vec3 getNormal(){
        return  Vec3.atLowerCornerOf(
                getBlockState().getValue(HORIZONTAL_AXIS) == Direction.Axis.Z?
                Direction.NORTH.getNormal():
                Direction.EAST.getNormal());

    }

    @Override
    public void receiveLaser(Laser laser) {

        if((MirrorBlockEntity.angleFromVectors(getNormal(), laser.direction.normalize()) * Mth.RAD_TO_DEG > 30 &&
            MirrorBlockEntity.angleFromVectors(getNormal().scale(-1), laser.direction.normalize()) * Mth.RAD_TO_DEG > 30)){

            if(lasers.containsKey(laser)){
                endReceiveLaser(laser);
            }
            return;
        }

        Vec3 position = getBlockPos().getCenter();
        float red   = (float)((laser.color & 0xFF0000) >> 16)/255.0f;
        float green = (float)((laser.color & 0x00FF00) >> 8 )/255.0f;
        float blue  = (float)((laser.color & 0x0000FF)      )/255.0f;
        float total = red+green+blue;

        if(!lasers.containsKey(laser)){
            Laser[] scatter = new Laser[3];

            scatter[0] = (red   > 0)?new Laser(0xFF0000, position, laser.direction,
                    red/total * laser.power): null;
            scatter[1] = (green > 0)?new Laser(0xFF00,   position.add(0.001, 0, 0.001), laser.direction.add(0, 0.1, 0),
                    green/total * laser.power):null;
            scatter[2] = (blue  > 0)?new Laser(0xFF,     position.add(-0.001, 0, -0.001), laser.direction.add(0, 0.2, 0),
                    blue/total * laser.power):null;

            lasers.put(laser, scatter);
            changed = true;
        }else{
            Laser[] scatter = lasers.get(laser);
            if(scatter.length > 0 && scatter[0] != null) {
                scatter[0].setDirection(laser.direction.normalize());
                scatter[0].setPower(red / total * laser.power);
            }
            if(scatter.length > 1 && scatter[1] != null) {
                scatter[1].setDirection(laser.direction.add(0, 0.1, 0).normalize());
                scatter[1].setPower(green / total * laser.power);
            }
            if(scatter.length > 2 && scatter[2] != null) {
                scatter[2].setDirection(laser.direction.add(0, 0.2, 0).normalize());
                scatter[2].setPower(blue / total * laser.power);
            }
        }
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        if(!lasers.containsKey(laser))
            return;
        for(Laser l : lasers.get(laser)){
            if(l != null)
                l.disable();
        }
        lasers.remove(laser);
    }
}
