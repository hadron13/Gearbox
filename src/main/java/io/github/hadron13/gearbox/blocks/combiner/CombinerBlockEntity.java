package io.github.hadron13.gearbox.blocks.combiner;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.github.hadron13.gearbox.blocks.combiner.CombinerBlock.HORIZONTAL_FACING;

public class CombinerBlockEntity extends SmartBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Laser laserBeam;
    public Set<Laser> incomingLasers;

    public CombinerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        incomingLasers = new HashSet<>();
        laserBeam = new Laser(0xFFFFFF, getBlockPos().getCenter(), new Vec3(getBlockState().getValue(HORIZONTAL_FACING).step()));
        laserBeam.disable();
    }

    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(100);
    }

    @Override
    public void tick() {
        super.tick();
        laserBeam.tick(getLevel());
    }

    @Override
    public List<Laser> getLasers() {
        return List.of(laserBeam);
    }

//    public static double angleFromVectors(double x1, double y1, double x2, double y2) {
//        double cosTheta = Math.max(-1.0, Math.min(1.0, x1 * x2 + y1 * y2));
//        return Math.acos(cosTheta) * Mth.RAD_TO_DEG;
//    }
    public boolean validAngle(Vec3 direction){
        Vec3 front = new Vec3(getBlockState().getValue(HORIZONTAL_FACING).step());

        double cosTheta = Mth.clamp( direction.x * front.x + direction.z * front.z, -1.0, 1.0);
        double horizontal_angle = Math.acos(cosTheta) * Mth.RAD_TO_DEG;

        double vertical_angle = Math.acos(direction.y) * Mth.RAD_TO_DEG - 90;

        return horizontal_angle < 60 && Math.abs(vertical_angle) < 45;
    }

    public void updateColor(){
        int total_red = 0, total_green = 0, total_blue = 0;
        for(Laser l : incomingLasers){
            int red   = (l.color >> 16) & 0xFF;
            int green = (l.color >> 8)  & 0xFF;
            int blue  = (l.color)       & 0xFF;

            total_red   += (int) (red   * l.power);
            total_green += (int) (green * l.power);
            total_blue  += (int) (blue  * l.power);
        }
        float largest_component = Math.max(Math.max(total_red, total_green), total_blue);
        if(largest_component == 0){
            return;
        }
        total_red   = (int)((total_red  /largest_component) * 255.0);
        total_green = (int)((total_green/largest_component) * 255.0);
        total_blue  = (int)((total_blue /largest_component) * 255.0);

        laserBeam.color = (total_red << 16) + (total_green << 8) + (total_blue);
        laserBeam.enable();
    }

    @Override
    public void receiveLaser(Laser laser) {
        if(incomingLasers.contains(laser))
            return;
        if(validAngle(laser.direction)) {
            incomingLasers.add(laser);
            updateColor();
        }
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        incomingLasers.remove(laser);
        if(incomingLasers.isEmpty()){
            laserBeam.disable();
        }else{
            updateColor();
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
}
