package io.github.hadron13.gearbox.blocks.attenuator;

import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.laser.ILaserEmitter;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.attenuator.AttenuatorBlock.HORIZONTAL_FACING;

public class AttenuatorBlockEntity extends SmartBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Laser laser, receivingLaser;
    public ScrollValueBehaviour apertureSize;


    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(100);
    }


    public AttenuatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        laser = new Laser(0x0, getBlockPos().getCenter(), new Vec3(getFacing().step()));
        laser.disable();
    }

    @Override
    public void tick() {
        super.tick();
        if(apertureSize.value == 0){
            laser.disable();
        }
        laser.tick(level);
    }

    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

        apertureSize = new ScrollValueBehaviour(GearboxLang.translateDirect("gui.attenuator.aperture_size"), this, new ApertureValueBoxTransform())
                        .between(0, 100);

        behaviours.add(apertureSize);


    }

    private class ApertureValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8f, 13.5f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            if (direction == Direction.DOWN)
                return false;
            return state.getValue(HORIZONTAL_FACING).getAxis() != direction.getAxis();
        }

    }

    @Override
    public List<Laser> getLasers() {
        return List.of(laser);
    }

    @Override
    public void receiveLaser(Laser laser) {
        if(!laser.hasValidAngle(new Vec3(getFacing().step()), 45, 45 )){
            return;
        }

        if(receivingLaser == null){
            receivingLaser = laser;
        }
        if(laser == receivingLaser){
            this.laser.setColor(laser.getColor());
            this.laser.setPower(laser.getPower() * (apertureSize.value/100.0f));
            this.laser.enable();
        }
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        if(laser == receivingLaser) {
            receivingLaser = null;
            this.laser.disable();
        }
    }
}
