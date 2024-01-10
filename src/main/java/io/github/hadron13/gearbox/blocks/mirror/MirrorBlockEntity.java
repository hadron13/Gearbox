package io.github.hadron13.gearbox.blocks.mirror;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.LaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
    public void wrenched(){
        LaserBeamBehavior.LaserBeam []beams = (LaserBeamBehavior.LaserBeam[])beamBehavior.beams.values().toArray();
        for(int i = 0; i < 2; i++){
            beams[i].facing = (i==1)?getLeft():getRight();
        }
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
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        beamBehavior.addLaser(facing, getBlockPos(), Color.BLACK, 0.0f);
        beamBehavior.addLaser(facing.getCounterClockWise(), getBlockPos(), Color.BLACK, 1.0f);

        beamBehavior.disableLaser(getLeft());
        beamBehavior.disableLaser(getRight());
//        beamBehavior.getLaser(facing).enabled = false;
//        beamBehavior.getLaser(facing.getCounterClockWise()).enabled = false;
    }

    @Override
    public void tick(){
       super.tick();

       if(timeoutLeft != 0) {
           timeoutLeft--;
       }else{
           beamBehavior.disableLaser(getRight());
       }

       if(timeoutRight != 0){
           timeoutRight--;
       }else {
           beamBehavior.disableLaser(getLeft());
       }

    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {

        if(face == getLeft() || face == getRight()) {
            LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getMirroredFace(face));
            if(face == getLeft())
                timeoutLeft = 3;
            else if(face == getRight())
                timeoutRight = 3;

            assert beam != null;
            beam.enabled = power > 0;
            beam.power = power;
            beam.color = color;
            return true;
        }

        return false;
    }
}
