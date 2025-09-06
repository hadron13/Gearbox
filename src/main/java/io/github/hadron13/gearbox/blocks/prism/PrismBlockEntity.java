package io.github.hadron13.gearbox.blocks.prism;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

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

    @Override
    public void receiveLaser(Laser laser) {
        if(!lasers.containsKey(laser)){



            Laser[] scatter = new Laser[3];
            Vec3 position =  getBlockPos().getCenter();

            scatter[0] = ((laser.color & 0xFF0000) > 0)?new Laser(laser.color & 0xFF0000, position, laser.direction): null;
            scatter[1] = ((laser.color & 0x00FF00) > 0)?new Laser(laser.color & 0xFF00, position.add(0.001, 0, 0.001), laser.direction.add(0, 0.1, 0)):null;
            scatter[2] = ((laser.color & 0x0000FF) > 0)?new Laser(laser.color & 0xFF, position.add(-0.001, 0, -0.001), laser.direction.add(0, 0.2, 0)):null;
            lasers.put(laser, scatter);
            changed = true;
        }
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        if(!lasers.containsKey(laser))
            return;
        lasers.remove(laser);
    }
}
