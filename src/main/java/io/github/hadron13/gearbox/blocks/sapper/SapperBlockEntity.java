package io.github.hadron13.gearbox.blocks.sapper;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyBlock;
import com.simibubi.create.content.fluids.hosePulley.HosePulleyFluidHandler;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;

import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.LeavesBlock.DISTANCE;

public class SapperBlockEntity extends KineticBlockEntity implements IHaveHoveringInformation {

    public static final int NUM_LEAVES = 5;
    public static final int EXTENSION_TIME = 10;

    //registers a few leaves to keep track of
    public BlockPos[] leafPos = new BlockPos[NUM_LEAVES];
    private Couple<Block> treeBlocks;
    SmartFluidTankBehaviour tank;
    private int extendedTicks;
    private int sapTimer;
    private boolean valid;
    private boolean cached;
    public enum SapperState{
        RETRACTED,
        EXTENDING,
        EXTENDED,
        RETRACTING
    }
    SapperState sapperState;
    private SappingRecipe lastRecipe;

    public SapperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        extendedTicks = 0;
        sapTimer = -1;
        valid = true;
        cached = false;
        sapperState = SapperState.RETRACTED;

    }

    @Override
    public void tick() {
        super.tick();

        if (level == null)
            return;
        float speed = Math.abs(getSpeed());

        switch (sapperState) {
            case RETRACTED -> {
//                checkValidity();
                if (valid && speed > 0 && !isTankFull()) {
                    sapperState = SapperState.EXTENDING;
                    sapTimer = 40*32;
                }
            }
            case EXTENDING -> {
                extendedTicks++;
                if (extendedTicks == EXTENSION_TIME)
                    sapperState = SapperState.EXTENDED;
            }
            case EXTENDED -> {
                sapTimer -= speed;
                if (sapTimer == 0) {
//                    checkValidity();

                    if (valid && speed > 0 && !isTankFull()) {
                        SmartFluidTank localTank = tank.getPrimaryHandler();
                        localTank.fill(new FluidStack(AllFluids.CHOCOLATE.get(), 150), IFluidHandler.FluidAction.EXECUTE);
                        sapTimer = 40*32;

                        sendData();
                    } else {
                        sapperState = SapperState.RETRACTING;
                    }
                }
            }
            case RETRACTING -> {
                extendedTicks--;
                if (extendedTicks == 0)
                    sapperState = SapperState.RETRACTED;
            }
        }

    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
    }

    public boolean isTankFull(){
        return tank.getPrimaryHandler().getFluidAmount() == 1000;
    }

    public void checkValidity(){
        if (level == null || !level.isLoaded(worldPosition) || level.isClientSide)
            return;
        BlockPos trunkPos = getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING).getOpposite(), 2);

        if(!level.getBlockState(trunkPos).is(BlockTags.LOGS)) {
            cached = false;
            valid = false;
            return;
        }
        int vertical = Math.min(level.getMaxBuildHeight() - worldPosition.getY(), 40);

        Iterable<BlockPos> leafArea = BlockPos.betweenClosed(   trunkPos.offset(-2,1,-2),
                                                                trunkPos.offset( 2, vertical, 2));
        if(!cached){
            valid = false;
            if(checkLeaves(leafArea)){
                cached = true;
                valid = true;
            }
            return;
        }

        for (BlockPos pos : leafPos) {
            if (level.getBlockState(pos).getValue(DISTANCE) == 0 )
                continue;
            valid = checkLeaves(leafArea);
            return;
        }
    }

    public boolean checkLeaves(Iterable<BlockPos> leafArea){
        int leafCount = 0;
        for(BlockPos pos : leafArea){
            BlockState state = level.getBlockState(pos);
            if(state.getBlock() == Blocks.AIR)
                continue;

            if( state.getValue(DISTANCE) != 0 ){
                leafPos[leafCount] = pos;
                leafCount++;
                if(leafCount == NUM_LEAVES)
                    break;
            }
        }
        return leafCount == NUM_LEAVES;
    }

    public boolean isValidTrunk(BlockPos pos){
        return true;
    }

    public boolean isValidLeaf(BlockPos pos){
        return true;
    }

    public float getRenderedHeadOffset(float partialTicks) {

        final float startPos = 12 / 16f;
        final float finalPos = startPos + 6 / 16f;

        switch (sapperState){
            case RETRACTED  -> {return startPos;}
            case EXTENDED   -> {return finalPos;}
            case RETRACTING -> {partialTicks *= -1;}
        }

        float offset = (extendedTicks + partialTicks)/10f;

        float t =  1 - Mth.cos((float)( offset * Math.PI ));
        t /= 2;

        return Mth.lerp(t, startPos, finalPos) ;
    }

    public float getRenderedHeadRotationSpeed(float partialTicks) {
        if (sapperState == SapperState.EXTENDED) {
            return getSpeed() * ((extendedTicks < 7 || extendedTicks > EXTENSION_TIME)? 1 : 2 );
        }
        return speed / 2;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();
    }

    @Override
    public void invalidate() {
        super.invalidate();

    }

    @Override
    public void destroy() {
        super.destroy();
    }

    public static FluidStack getFluid(Block leaf, Block trunk){
        return new FluidStack(AllFluids.CHOCOLATE.get(), 150);
    }

    public void spawnParticles() {
        if(level.random.nextInt(8) != 1)
            return;

        Vec3 center = VecHelper.getCenterOf(worldPosition);
        level.addParticle(ParticleTypes.LARGE_SMOKE, center.x, center.y + .4f, center.z, 0, 1 /32f, 0);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {

//        compound.putInt("Timer", timer);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
//        timer = compound.getInt("Timer");
        super.read(compound, clientPacket);
    }
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isFluidHandlerCap(cap)
                && (side == null || SapperBlock.hasPipeTowards(level, worldPosition, getBlockState(), side)))
            return tank.getCapability().cast();
        return super.getCapability(cap, side);
    }


}