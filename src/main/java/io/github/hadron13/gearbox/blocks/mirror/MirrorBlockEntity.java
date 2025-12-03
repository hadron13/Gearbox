package io.github.hadron13.gearbox.blocks.mirror;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.legacy.RefinedRadianceItem;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity.truncatePrecision;
import static net.minecraft.core.Direction.Axis.Z;


public class MirrorBlockEntity extends KineticBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Map<Laser, Laser> lasers = new HashMap<>();
    public float angle = 0;
    public static final int SPEED_DIVIDER = 20;


    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(200);
    }

    public MirrorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        angle += getSpeed()/SPEED_DIVIDER;
        if(getSpeed() == 0){

            if(Mth.abs(Mth.abs(angle) % 90f - 45f) < 0.25){
                angle = (float)Math.round(angle);
            }
        }
    }

    @Override
    public void receiveLaser(Laser laser) {
        Vec3 toHere = laser.getPosition().vectorTo(this.getBlockPos().getCenter());
        Vec3 laserEnd = laser.getDirection().scale(laser.length);
        if((angleFromVectors(getNormal(), laser.direction.normalize()) * Mth.RAD_TO_DEG > 50 &&
            angleFromVectors(getNormal().scale(-1), laser.direction.normalize()) * Mth.RAD_TO_DEG > 50) ||
           laserEnd.distanceTo(toHere) > .15){
            if(lasers.containsKey(laser)){
                endReceiveLaser(laser);
            }
            return;
        }
        if(!lasers.containsKey(laser)){
            lasers.put(laser, new Laser(laser.color, getBlockPos().getCenter(),  reflect(laser.direction, getNormal()), laser.power ) );
        }
        Laser reflected = lasers.get(laser);
        reflected.setDirection(reflect(laser.direction, getNormal()));
        reflected.setPower(laser.power);
        reflected.setColor(laser.color);
        reflected.tick(this.getLevel());
    }

    //I - 2.0 * dot(N, I) * N
    public static Vec3 reflect(Vec3 incident, Vec3 normal){
        return incident.subtract(normal.scale(2.0 * normal.dot(incident)));
    }

    public static double angleFromVectors(Vec3 a, Vec3 b){
        return Math.acos( a.dot(b) / (a.length() * b.length()) );
    }

    public Vec3 getNormal(){
        Direction.Axis axis = getBlockState().getValue(AXIS);
        return VecHelper.rotate(Vec3.atLowerCornerOf((axis==Z? Direction.EAST: Direction.NORTH).getNormal()), angle, axis);
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

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        GearboxLang.translate("gui.mirror.angle")
                .style(ChatFormatting.WHITE)
                .add(GearboxLang.text(": " + truncatePrecision(angle % 360, 2)))
                .forGoggles(tooltip);
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        return true;
    }
}
