package io.github.hadron13.gearbox.blocks.amplifier;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.laser.*;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.amplifier.AmplifierBlock.HORIZONTAL_FACING;

public class AmplifierBlockEntity extends SmartBlockEntity implements ILaserReceiver, ILaserEmitter {

    public Laser laser, receivingLaser;

    public ScrollValueBehaviour amplification;


    public final InternalEnergyStorage energyStorage;

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(100);
    }

    public AmplifierBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);

        energyStorage = new InternalEnergyStorage(65536, 4096, 0);

        laser = new Laser(0x0, getBlockPos().getCenter(), new Vec3(getFacing().step()));
        laser.disable();
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.EnergyStorage.BLOCK,
                GearboxBlockEntities.AMPLIFIER.get(),
                (be, context) -> {
                    if (context == null || context == be.getBlockState().getValue(LaserBlock.HORIZONTAL_FACING).getCounterClockWise()){
                        return be.energyStorage;
                    }
                    return null;
                }
        );
    }

    @Override
    public void tick() {
        super.tick();
        laser.tick(level);
    }

    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

        amplification = new ScrollValueBehaviour(GearboxLang.translateDirect("gui.amplifier.amplification"), this,
                new AmplificationValueBoxTransform())
                .between(0, 100);

        behaviours.add(amplification);


    }

    private class AmplificationValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8.5f, 14.0f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return state.getValue(HORIZONTAL_FACING).getClockWise() == direction;
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
            this.laser.setPower(laser.getPower() * (1.0f + (amplification.value/100.0f)) );
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
