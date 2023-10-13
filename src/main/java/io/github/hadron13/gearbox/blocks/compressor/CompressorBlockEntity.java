package io.github.hadron13.gearbox.blocks.compressor;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.register.ModItems;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.HorizontalKineticBlock.HORIZONTAL_FACING;
import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel.NONE;
import static com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel.SMOULDERING;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class CompressorBlockEntity extends KineticBlockEntity implements IHaveHoveringInformation {

    private SmartFluidTankBehaviour tank;
    private SmartInventory output;

    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    private int outputTimer;
    private int timer;
    protected List<ItemStack> spoutputBuffer;
    private CompressingRecipe recipe;


    public CompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        output = new SmartInventory(1, this)
                .forbidInsertion()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new InvWrapper(output));
        timer = -1;
        recipe = null;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(tank);
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, output);
    }


    @Override
    public void tick() {
        super.tick();

        if(outputTimer > 0){
            outputTimer--;
            if(outputTimer == 0){
                BlockState state = getBlockState();
                BlockPos targetPos = getBlockPos().relative(state.getValue(HORIZONTAL_FACING)).below();
//
//                BlockEntity entity = level.getBlockEntity()
                for(int i = 0; i < output.getSlots(); i++) {
                    Block.popResource(level, targetPos, output.getStackInSlot(i));
                }
                if(!output.isEmpty())
                    outputTimer = OUTPUT_ANIMATION_TIME;
            }
        }

        if (getSpeed() >= 0)
            return;
        for (int i = 0; i < output.getSlots(); i++)
            if (output.getStackInSlot(i)
                    .getCount() == output.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
//                spawnParticles();
                return;
            }
            if (timer <= 0)
                process();
            return;
        }
        if (tank.getPrimaryHandler().isEmpty())
            return;

        if (recipe == null || !CompressingRecipe.match(this, recipe)) {
            Optional<CompressingRecipe> newRecipe = ModRecipeTypes.COMPRESSING.find(this, level);
            if (!newRecipe.isPresent()) {
                timer = 100;
                sendData();
            } else {
                recipe = newRecipe.get();
                timer = recipe.getProcessingDuration();
                sendData();
            }
            return;
        }

        timer = recipe.getProcessingDuration();
        sendData();
    }
    public void serverDebug(String msg){
        if(level != null && !level.isClientSide)
            Gearbox.LOGGER.debug(msg);
    }
    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }
    public BlazeBurnerBlock.HeatLevel getHeat(){
        return BasinBlockEntity.getHeatLevelOf(level.getBlockState(getBlockPos().below()));
    }
    private void process() {

        if (recipe == null || !CompressingRecipe.match(this, recipe)) {
            Optional<CompressingRecipe> newRecipe = ModRecipeTypes.COMPRESSING.find(this, level);
            if (!newRecipe.isPresent())
                return;
            recipe = newRecipe.get();
        }
        int usedAmmount = recipe.getFluidIngredients().get(0).getRequiredAmount();
        tank.getPrimaryHandler().drain(usedAmmount, EXECUTE);

        output.allowInsertion();
        recipe.rollResults()
                .forEach(stack -> ItemHandlerHelper.insertItemStacked(output, stack, false));
        output.forbidInsertion();
        if(outputTimer == 0)
            outputTimer = OUTPUT_ANIMATION_TIME;

        sendData();
        setChanged();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket){
//        compound.putFloat("t", recipeTimer);
//        compound.putString("recipe", currentRecipe.getId().toString());


        super.write(compound, clientPacket);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket){
//        recipeTimer = compound.getFloat("t");
//        currentRecipe =
        super.read(compound, clientPacket);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isFluidHandlerCap(cap)
                && (side == null || CompressorBlock.hasPipeTowards(level, worldPosition, getBlockState(), side)))
            return tank.getCapability().cast();

        if (isItemHandlerCap(cap) &&
                (side == null || !CompressorBlock.hasPipeTowards(level, worldPosition, getBlockState(), side)))
            return itemCapability.cast();

        return super.getCapability(cap, side);
    }
//
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());

        if(speed > 0)
            TooltipHelper.addHint(tooltip, "hint.compressor.reverse");

        return true;
    }




}
