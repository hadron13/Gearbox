package io.github.hadron13.gearbox.blocks.core_drill;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.compat.adlods.AdlodDepositDetector;
import io.github.hadron13.gearbox.register.GearboxItems;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlock.HORIZONTAL_FACING;

public class CoreDrillBlockEntity extends KineticBlockEntity {

    public int tubes = 0;
    public int drillState = 0;
    public int switchDelay = 0;
    public boolean updateOffsets = false;
    public LerpedFloat payloadOffset = LerpedFloat.linear();
    public LerpedFloat poleOffset = LerpedFloat.linear();

    public static final int IDLE = 0;
    public static final int PUSHING = 4;
    public static final int PULLING = 2;
    public static final int STORING = 3;

    public Block minedBlock;
    public ItemStackHandler outputInv;
    protected LazyOptional<IItemHandlerModifiable> itemCapability;


    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).inflate(30, 1, 30).setMinY(-60);
    }

    public CoreDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        payloadOffset.chase(0f, 1 / 16f, LerpedFloat.Chaser.LINEAR);
        poleOffset.chase(0f, 1 / 16f, LerpedFloat.Chaser.LINEAR);
        outputInv = new ItemStackHandler(1);
        itemCapability = LazyOptional.of(()->outputInv);

        minedBlock = null;
        setLazyTickRate(20);
    }

    @Override
    public void tick() {
        super.tick();


        if(switchDelay > 0){
            switchDelay--;
            return;
        }

        if(!level.isClientSide) {
            switch (drillState) {
                case IDLE -> {
                    if (Mth.abs(getSpeed()) > 32f && minedBlock != null && enoughTubes() && !isFull()) {
                        switchState(PULLING);
                    }
                }
                case PUSHING -> {
                    if (poleOffset.getChaseTarget() == 20 / 16f && poleOffset.settled()) {
                        poleOffset.updateChaseTarget(0);
                    }

                    if (poleOffset.settled()) {
                        tubes++;

                        if (enoughTubes() && level instanceof ServerLevel serverLevel) {
                            minedBlock = AdlodDepositDetector.getDeposit(serverLevel, getBlockPos()).orElse(null);
                        }

                        switchState(IDLE);
                        return;
                    }
                }
                case PULLING -> {
                    if (poleOffset.getChaseTarget() == 20 / 16f && poleOffset.settled()) {
                        poleOffset.updateChaseTarget(-1/16f);
                        payloadOffset.updateChaseTarget(0);
                    }

                    if (poleOffset.settled()) {
                        switchState(STORING);
                    }
                }
                case STORING -> {
                    if (payloadOffset.settled()) {
                        if (minedBlock != null)
                            outputInv.insertItem(0, new ItemStack(minedBlock.asItem(), 1), false);

                        if (Mth.abs(getSpeed()) > 32f && minedBlock != null && enoughTubes() && !isFull()) {
                            switchState(PULLING);
                        }else{
                            switchState(IDLE);
                        }
                    }
                }
            }
        }else{
            switch (drillState) {
                case PUSHING -> {
                    if (poleOffset.getChaseTarget() == 20 / 16f && poleOffset.settled()) {
                        poleOffset.updateChaseTarget(0);
                    }
                }
                case PULLING -> {
                    if (poleOffset.getChaseTarget() == 20 / 16f && poleOffset.settled()) {
                        poleOffset.updateChaseTarget(-1/16f);
                        payloadOffset.updateChaseTarget(0);
                    }
                }
            }
        }
        if(getSpeed() == 0)
            return;
        float chaseSpeed = Mth.log2((int)Mth.abs(getSpeed()))/128f;
        if(drillState == PUSHING)
            chaseSpeed = Mth.abs(getSpeed())/512f;

        poleOffset.updateChaseSpeed(chaseSpeed);
        payloadOffset.updateChaseSpeed(chaseSpeed/(20/16f));

        poleOffset.tickChaser();
        payloadOffset.tickChaser();

        if(poleOffset.getValue() > 20/16f){
            poleOffset.setValue(20/16f);
        }
    }

    public void switchState(int state){
        if(level.isClientSide)
            return;

        this.drillState = state;

        switch(this.drillState){
            case IDLE -> {
                poleOffset.updateChaseTarget(0.0f);
            }
            case PUSHING -> {
                payloadOffset.setValue(0);
                payloadOffset.updateChaseTarget(1.0f);
                poleOffset.updateChaseTarget(20/16f);
            }
            case PULLING -> {
                payloadOffset.setValue(1.0f);
                payloadOffset.updateChaseTarget(1.0f);
                poleOffset.updateChaseTarget(20 / 16f);
            }
            case STORING -> {
                payloadOffset.setValue(0.0f);
                payloadOffset.updateChaseTarget(1.0f);
            }
        }
        switchDelay = 2;
        updateOffsets = true;
        sendData();
    }

    public boolean enoughTubes(){
        return tubes >= ((worldPosition.getY() + 64)/1.25f)/Mth.cos(22.5f * Mth.DEG_TO_RAD);
    }

    public boolean isFull(){
        return outputInv.getStackInSlot(0).getCount() >= outputInv.getSlotLimit(0);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        itemCapability.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), new ItemStack(GearboxItems.CORE_TUBE, tubes + (drillState==PUSHING?1:0)));
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER && (side == null || side == getBlockState().getValue(HORIZONTAL_FACING).getOpposite())){
            return itemCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public boolean isSpeedRequirementFulfilled() {
        return Mth.abs(getSpeed()) >= 32f;
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(level instanceof ServerLevel serverLevel) {
            minedBlock = AdlodDepositDetector.getDeposit(serverLevel, getBlockPos()).orElse(null);
            sendData();
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        GearboxLang.text("").forGoggles(tooltip);
        GearboxLang.translate("gui.core_drill.deposit_info").style(ChatFormatting.GOLD).forGoggles(tooltip);
        if(minedBlock == null){
            GearboxLang.translate("gui.core_drill.no_deposit")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);
        }else{
            GearboxLang.translate("gui.core_drill.deposit_found")
                    .style(ChatFormatting.GRAY)
                    .add(minedBlock.getName().setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)))
                    .forGoggles(tooltip);
//            GearboxLang.(minedBlock.getName().get)
//                    .style(ChatFormatting.WHITE).forGoggles(tooltip);

        }
        if(!enoughTubes()){
            GearboxLang.text("").forGoggles(tooltip);

            GearboxLang.addHint(tooltip, "hint.core_drill.tubes");
        }

        return true;
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("tubes", tubes);
        compound.putInt("state", drillState);
        if(!clientPacket || updateOffsets){
            compound.put("pole_offset", poleOffset.writeNBT());
            compound.put("payload_offset", payloadOffset.writeNBT());
            updateOffsets = false;
        }
        if(minedBlock != null)
            compound.put("minedBlock", NbtUtils.writeBlockState(minedBlock.defaultBlockState()));
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        tubes = compound.getInt("tubes");
        drillState = compound.getInt("state");

        //stupid clientPacket parameter will not update value unless false.
        //I guess it makes sense everywhere else but this is 100% server driven
        if(compound.contains("pole_offset")){
            poleOffset.readNBT(compound.getCompound("pole_offset"), false);
            payloadOffset.readNBT(compound.getCompound("payload_offset"), false);
        }
        if(compound.contains("minedBlock"))
            minedBlock = NbtUtils.readBlockState(blockHolderGetter(), compound.getCompound("minedBlock")).getBlock();
    }
}
