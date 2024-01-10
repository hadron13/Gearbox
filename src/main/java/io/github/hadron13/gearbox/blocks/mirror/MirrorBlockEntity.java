package io.github.hadron13.gearbox.blocks.mirror;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.LaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.mirror.MirrorBlock.HORIZONTAL_FACING;

public class MirrorBlockEntity extends SmartBlockEntity implements LaserReceiver {

    LaserBeamBehavior beamBehavior;
    public int timeoutLeft = 0;
    public int timeoutRight= 0;
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
            sendData();
        }
        if(timeoutRight != 0){
            timeoutRight--;
        }else {
            beamBehavior.disableLaser(getLeft());
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
            timeoutLeft = 2;
        }
        else if(face == getRight()) {
            timeoutRight = 2;
        }

        if(Mth.equal(beam.power, power)|| beam.color != color) {
            beam.enabled = power > 0;
            beam.power = power;
            beam.color = color;
            beamBehavior.propagate(getMirroredFace(face), 50);
        }
        return true;
    }

    @Override
    public float getLoss() {
        return 0.1f;
    }
}
