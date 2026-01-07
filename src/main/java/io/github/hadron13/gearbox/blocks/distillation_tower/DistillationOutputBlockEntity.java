package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlock.*;
import static net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class DistillationOutputBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tankInventory;
    public boolean duplicate = false;

    public DistillationOutputBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tankInventory = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 4000, true)
                .whenFluidUpdates(this::sendData)
                .forbidInsertion();
        behaviours.add(tankInventory);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(level.isClientSide)
            return;


        if(getBlockState().getValue(POWERED)){
            tankInventory.getPrimaryHandler().drain(500, EXECUTE);
            sendData();
        }

        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            DistillationControllerBlockEntity controller = tank.getDistillationControllerBE();
            if(controller != null ){

                boolean wasDuplicate = duplicate;
                duplicate = controller.addOutput(getOutputNumber(), worldPosition);
                if(wasDuplicate != duplicate)
                    sendData();
            }
        }
    }

    @Override
    public void remove() {
        super.remove();

        if(level.isClientSide)
            return;
        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            DistillationControllerBlockEntity controller = tank.getDistillationControllerBE();
            if(controller != null){
                controller.removeOutput(getOutputNumber());
            }
        }
    }

    public int getOutputNumber(){
        int output = -1;
        BlockEntity be = level.getBlockEntity(worldPosition.relative(getBlockState().getValue(TANK_FACE)));
        if(be instanceof SteelTankBlockEntity tank){
            output = tank.getOutputNumber();
        }
        return output;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {


        if(duplicate){
            GearboxLang.translate("gui.distil_duplicate")
                    .style(ChatFormatting.DARK_RED)
                    .forGoggles(tooltip);
        }

        int output = getOutputNumber();
        if(output != -1) {
            GearboxLang.translate("gui.distil_layer")
                    .text("#" + output)
                    .forGoggles(tooltip);
            GearboxLang.text("").forGoggles(tooltip);
        }


        containedFluidTooltip(tooltip, isPlayerSneaking, tankInventory.getCapability());


        if(getBlockState().getValue(POWERED)) {
            GearboxLang.text("").forGoggles(tooltip);
            GearboxLang.addHint(tooltip, "hint.distil.discard");
        }
        return true;
    }

    @Override
    protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        tag.putBoolean("dup", duplicate);
        tankInventory.write(tag, registries, clientPacket);
        super.write(tag, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
        duplicate = tag.getBoolean("dup");
        tankInventory.read(tag, registries, clientPacket);
        super.read(tag, registries, clientPacket);
    }




    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                GearboxBlockEntities.DISTILLATION_OUTPUT.get(),
                (be, context) -> {
                    if (context == null || context == be.getBlockState().getValue(FACING)){
                        return be.tankInventory.getCapability();
                    }
                    return null;
                }
        );
    }
}
