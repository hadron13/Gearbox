package io.github.hadron13.gearbox.blocks.sapper;


import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.sound.SoundScapes.AmbienceGroup;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;

public class SapperBlockEntity extends KineticBlockEntity implements IHaveHoveringInformation {

    
    public int runningTicks;
    public int processingTicks;

    // valid and not full
    public boolean running;

    // has a valid tree
    public boolean valid;
    private SappingRecipe lastRecipe;

    public SapperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public void tick() {
        super.tick();

        if(level.random.nextInt(60) == 1 && !running){


            running = true;


        }

        if (running && level != null) {

            if ((!level.isClientSide || isVirtual()) && runningTicks == 20) {
                if (processingTicks < 0) {
                    float recipeSpeed = 1;

                    processingTicks = Mth.clamp((Mth.log2((int) (512 / speed))) * Mth.ceil(recipeSpeed * 15) + 1, 1, 512);
                } else {
                    processingTicks--;
                    if (processingTicks == 0) {
                        runningTicks++;
                        processingTicks = -1;
                    }
                }
            }
            if (runningTicks != 20)
                runningTicks++;
        }


    }
    public void sap(){







    }

    public boolean hasValidTree(){
        BlockPos treePos = getBlockPos().relative(getBlockState().getValue(HORIZONTAL_FACING), 2);
        return true;

    }

    public float getRenderedHeadOffset(float partialTicks) {
        int localTick;
        float offset = 0;

        if (running) {
            if (runningTicks == 20) {
                offset = 1;
            }else{
                localTick = (runningTicks < 20)? runningTicks : 40 - runningTicks;

                float num = (localTick + partialTicks) / 20f;

                num = ((2 - Mth.cos((float) (num * Math.PI))) / 2);

                offset = num - .5f;
            }
        }
        return offset + 7 / 16f;
    }

    public float getRenderedHeadRotationSpeed(float partialTicks) {
        if (running) {
            return getSpeed() * ((runningTicks < 15 || runningTicks > 20)? 1 : 2 );
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
//        capability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
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

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//        if (isItemHandlerCap(cap))
//            return capability.cast();
//        return super.getCapability(cap, side);
//    }

//    private boolean canProcess(ItemStack stack) {
//        ItemStackHandler tester = new ItemStackHandler(1);
//        tester.setStackInSlot(0, stack);
//        RecipeWrapper inventoryIn = new RecipeWrapper(tester);
//
//        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
//            return true;
//        return ModRecipeTypes.SAPPING.find(inventoryIn, level)
//                .isPresent();
//    }



}