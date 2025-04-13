package io.github.hadron13.gearbox.blocks.large_laser;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.InternalEnergyStorage;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.laser.LaserBlock;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.theme.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.lwjgl.system.CallbackI;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.HORIZONTAL_FACING;
import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.LargeLaserPart.*;
import static io.github.hadron13.gearbox.blocks.large_laser.LargeLaserBlock.PART;

public class LargeLaserBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public LaserBeamBehavior beamBehavior;
    public AABB renderBoundingBox;
    public InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;
    public int redstoneSignal = 0;
    public LargeLaserBlockEntity back = this;
    boolean firstTick = true;
    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;

    public LargeLaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(5);
        energyStorage = new InternalEnergyStorage(65536, 4096, 4096);
        lazyEnergy = LazyOptional.of(() -> energyStorage);
    }

    public boolean isFront(){
        return getBlockState().getValue(PART) == FRONT || getBlockState().getValue(PART) == SINGLE;
    }
    public boolean isBack(){
        return getBlockState().getValue(PART) == BACK || getBlockState().getValue(PART) == SINGLE;
    }
    public Direction getFacing(){
        return getBlockState().getValue(HORIZONTAL_FACING);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition, worldPosition.offset(1, 1, 1));
        }
        return renderBoundingBox;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        beamBehavior = new LaserBeamBehavior(this);
        behaviours.add(beamBehavior);
        beamBehavior.addLaser(getFacing(), getBlockPos(), Color.RED, 20.0f);
        updatePower();
        renderBoundingBox = new AABB(worldPosition, worldPosition.relative(getFacing(), LaserBeamBehavior.MAX_LENGTH).offset(1, 1, 1));
    }
    @Override
    public void tick(){
        super.tick();
        if(level == null)
            return;

        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());

        if(beam == null)
            return;

        if(level.isClientSide) {
            float targetSpeed = beam.enabled? beam.power : 0;

            visualSpeed.updateChaseTarget(targetSpeed);
            visualSpeed.tickChaser();
            angle += visualSpeed.getValue() * 3 / 10f;
            angle %= 360;
            return;
        }
        if(!isFront())
            return;

        if(firstTick){
            neighbourChanged(getBlockState());
            firstTick = false;
        }
        InternalEnergyStorage backEnergy;

        backEnergy = back.energyStorage;
        //uncomment on release!!

        if(beam.enabled) {
            backEnergy.internalConsumeEnergy((int)(beam.power * 10f));
        }
        beam.enabled = backEnergy.getEnergyStored() > 400;
        sendData();
    }

    public void invalidate() {
        lazyEnergy.invalidate();
    }

    public void updatePower(){

        if(level == null || level.isClientSide)
            return;
        if(!isFront())
            return;
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(getFacing());
        beam.power = 20f;
        this.back = this;
        Direction back = getFacing().getOpposite();
        for(int offset = 0; offset <= 20; offset++){
            BlockState backBlock = level.getBlockState(getBlockPos().relative(back, offset + 1));

            if(!(backBlock.getBlock() instanceof LargeLaserBlock)) {
                break;
            }
            if(backBlock.getValue(HORIZONTAL_FACING) != getFacing()) {
                break;
            }
            if(beam.power <= 180f)
                beam.power += 20f;

            if(backBlock.getValue(PART) == BACK){
                BlockPos back_pos = getBlockPos().relative(back, offset + 1);
                BlockEntity be = level.getBlockEntity(back_pos);
                if(be instanceof LargeLaserBlockEntity back_laser){
                    this.back = back_laser;
                }
                break;
            }
        }
    }

    @Override
    public void lazyTick(){
        super.lazyTick();
        updatePower();
        sendData();

    }

    public void neighbourChanged(BlockState newState){
        if(level == null)
            return;


        beamBehavior.beams.clear();
        beamBehavior.addLaser(newState.getValue(HORIZONTAL_FACING), getBlockPos(), Color.BLACK, 20f);
        beamBehavior.wrenched = true;
        LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser(newState.getValue(HORIZONTAL_FACING));

        beam.enabled = (newState.getValue(PART) == FRONT || newState.getValue(PART) == SINGLE) && back.energyStorage.getEnergyStored() > 400;

        if(!beam.enabled){
            sendData();
            return;
        }
        redstoneSignal = level.getBestNeighborSignal(worldPosition);

        beam.color = Color.rainbowColor(redstoneSignal*(1536/15));
        if(redstoneSignal == 10){
            beam.color.setValue(0x0000FF);
        }else if (redstoneSignal == 5){
            beam.color.setValue(0x00FF00);
        }
        updatePower();

        sendData();
    }


    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if(isFront()) {
//            energyStorage.storedEnergyTooltip(tooltip);
            LaserBeamBehavior.LaserBeam beam = beamBehavior.getLaser();
            if(beam != null) {
                InternalEnergyStorage.energyConsumptionTooltip(tooltip, beam.enabled?(int)beamBehavior.getLaser().power * 10:0);
            }
        }
        return true;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("signal", redstoneSignal);
//        compound.putInt("storage", back.energyStorage.getEnergyStored());
        energyStorage.write(compound);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        redstoneSignal = compound.getInt("signal");
//        consumption = compound.getInt("consumption");
        energyStorage.read(compound);
        if (clientPacket)
//            visualSpeed.chase(10f, 1 / 32f, LerpedFloat.Chaser.EXP);
            visualSpeed.chase(beamBehavior.getLaser(getFacing()).power / 10, 1 / 64f, LerpedFloat.Chaser.EXP);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ENERGY && isBack() && side == getFacing().getOpposite())// && !level.isClientSide
            return lazyEnergy.cast();
        return LazyOptional.empty();
    }



}
