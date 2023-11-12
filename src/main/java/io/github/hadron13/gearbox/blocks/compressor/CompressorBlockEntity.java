package io.github.hadron13.gearbox.blocks.compressor;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlock;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.IntAttached;
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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.*;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.ArrayList;
import java.util.Collections;
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

    private int timer;
    public List<Integer> spoutputIndex;
    public CompressingRecipe recipe;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    public List<IntAttached<ItemStack>>  visualizedOutputItems;


    public CompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        output = new SmartInventory(3, this)
                .forbidInsertion()
                .withMaxStackSize(64);

        itemCapability = LazyOptional.of(() -> new InvWrapper(output));
        spoutputIndex = new ArrayList<>();
        visualizedOutputItems = Collections.synchronizedList(new ArrayList<>());
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
        if(level==null)
            return;

        if(level.isClientSide){
            visualizedOutputItems.forEach(IntAttached::decrement);
            visualizedOutputItems.removeIf(IntAttached::isOrBelowZero);
        }


        Direction dir = getBlockState().getValue(HORIZONTAL_FACING);
        if(!level.isClientSide && !spoutputIndex.isEmpty() && BasinBlock.canOutputTo(level, getBlockPos(), dir)) {
            serverDebug("can insert");
            for (int i = 0; i < spoutputIndex.size(); i++) {
                ItemStack item = output.getItem(spoutputIndex.get(i));
                if (item.isEmpty()) {
                    spoutputIndex.remove(i);
                    continue;
                }

                BlockEntity be = level.getBlockEntity(worldPosition.below()
                        .relative(dir));

                FilteringBehaviour filter = null;
                InvManipulationBehaviour inserter = null;
                if (be != null) {
                    filter = BlockEntityBehaviour.get(level, be.getBlockPos(), FilteringBehaviour.TYPE);
                    inserter = BlockEntityBehaviour.get(level, be.getBlockPos(), InvManipulationBehaviour.TYPE);
                }

                IItemHandler targetInv = be == null ? null
                        : be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, dir.getOpposite())
                        .orElse(inserter == null ? null : inserter.getInventory());

                if (targetInv == null)
                    continue;
                if (!ItemHandlerHelper.insertItemStacked(targetInv, item, true)
                        .isEmpty())
                    continue;
                if (filter != null && !filter.test(item))
                    continue;
                ItemHandlerHelper.insertItemStacked(targetInv,
                        output.extractItem(spoutputIndex.get(i), 64, false), false);
                visualizedOutputItems.add(IntAttached.withZero(item));
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

        if (!CompressingRecipe.match(this, recipe)) {
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

        if (!CompressingRecipe.match(this, recipe)) {
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
        for(int i = 0; i < output.getSlots(); i++) {
            if(!spoutputIndex.contains(i))
                spoutputIndex.add(i);
        }
        sendData();
        setChanged();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket){
        super.write(compound, clientPacket);
        compound.put("OutputItems", output.serializeNBT());

//        compound.putIntArray("Overflow",spoutputIndex);

        if (!clientPacket)
            return;

        compound.put("VisualizedItems", NBTHelper.writeCompoundList(visualizedOutputItems, ia -> ia.getValue()
                .serializeNBT()));
        visualizedOutputItems.clear();

    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket){
        super.read(compound, clientPacket);

        output.deserializeNBT(compound.getCompound("OutputItems"));

//        NBTHelper.readCompoundList(compound.getList("Overflow", Tag.TAG_INT), );

        if (!clientPacket)
            return;

        NBTHelper.iterateCompoundList(compound.getList("VisualizedItems", Tag.TAG_COMPOUND),
                c -> visualizedOutputItems.add(IntAttached.with(OUTPUT_ANIMATION_TIME, ItemStack.of(c))));
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
        boolean isEmpty = true;
        isEmpty = containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
        if(speed > 0) {
            TooltipHelper.addHint(tooltip, "hint.compressor.reverse");
            isEmpty = false;
        }

        for (int i = 0; i < output.getSlots(); i++) {
            ItemStack stackInSlot = output.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            Lang.text("")
                    .add(Components.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(Lang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        return isEmpty;
    }




}