package io.github.hadron13.gearbox.blocks.mirror;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.mirror.MirrorBlock.HORIZONTAL_FACING;

public class MirrorBlockEntity extends SmartBlockEntity implements ILaserReceiver {

    LaserBeamBehavior beamBehavior;
    public int timeoutLeft = 0;
    public int timeoutRight= 0;

    public AABB renderBoundingBox;
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

    public void wrenched(BlockState newState){
        Direction newLeft = newState.getValue(HORIZONTAL_FACING);
        Direction newRight = newLeft.getCounterClockWise();
        beamBehavior.beams.clear();
        beamBehavior.addLaser(newLeft, getBlockPos(), Color.BLACK, 0.0f);
        beamBehavior.addLaser(newRight, getBlockPos(), Color.BLACK, 0.0f);
        beamBehavior.disableLaser(newLeft);
        beamBehavior.disableLaser(newRight);
        timeoutLeft = 0;
        timeoutRight = 0;
        if(level.isClientSide)
            beamBehavior.wrenched = true;
    }
    public Direction getLeft(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }
    public Direction getRight(){
        return getLeft().getCounterClockWise();
    }
    public Direction getMirroredFace(Direction face){
        if(face == getLeft()){
            return getRight();
        }else if(face == getRight()){
            return getLeft();
        }else{
            return null;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getLeft(), getBlockPos(), Color.BLACK, 0.0f);
        beamBehavior.addLaser(getRight(), getBlockPos(), Color.BLACK, 0.0f);
        beamBehavior.disableLaser(getLeft());
        beamBehavior.disableLaser(getRight());


        renderBoundingBox = new AABB(worldPosition, worldPosition
                                                    .relative(getLeft(), LaserBeamBehavior.MAX_LENGTH)
                                                    .relative(getRight(), LaserBeamBehavior.MAX_LENGTH)
                                                    .offset(1, 1, 1));
    }

    @Override
    public void tick(){
        super.tick();
        if(level == null)
            return;
        if(level.isClientSide)
            return;

        if(timeoutLeft != 0) {
            timeoutLeft--;
        }else{
            beamBehavior.disableLaser(getRight());
            beamBehavior.setPower(getRight(), 0);
            beamBehavior.propagate(getRight(), 50);
            sendData();
        }
        if(timeoutRight != 0){
            timeoutRight--;
        }else {
            beamBehavior.disableLaser(getLeft());
            beamBehavior.setPower(getLeft(), 0);
            beamBehavior.propagate(getLeft(), 50);
            sendData();
        }

    }

    public void propagate(Direction face, Color color, float power, int index){
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getMirroredFace(face));
        if(beam == null)
            return;
        if(face == getLeft()) {
            timeoutLeft = 2;
        }
        else if(face == getRight()) {
            timeoutRight = 2;
        }
        beam.enabled = power > 0;
        beam.power = power;
        beam.color = color;
        beamBehavior.propagate(getMirroredFace(face), index);
    }
    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getMirroredFace(face));
        if(beam == null)
            return false;
        if(face == getLeft()) {
            timeoutLeft = 1;
        }
        else if(face == getRight()) {
            timeoutRight = 1;
        }

        //if(Mth.equal(beam.power, power) || beam.color.getRGB() != color.getRGB()) {
        beam.enabled = power > 0;
        beam.power = power;
        beam.color = color;
        beamBehavior.propagate(getMirroredFace(face), 50);
        //}
        return true;
    }

    @Override
    public float getLoss() {
        return 0.03f;
    }
}
