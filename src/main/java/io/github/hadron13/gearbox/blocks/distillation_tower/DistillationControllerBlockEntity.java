package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlock;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlockEntity;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxIcons;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class DistillationControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public ScrollOptionBehaviour<DistilMode> distilMode;
    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    public DistillingRecipe currentRecipe;
    public BlockPos tankController;
    public Map<Integer, BlockPos> outputs = new HashMap<>();
    public int timer;

    public boolean contentsChanged;

    protected LazyOptional<IFluidHandler> fluidCapability;

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(10);
    }


    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        distilMode = new ScrollOptionBehaviour<>(DistilMode.class,
                GearboxLang.translate("gui.distil_mode").component(), this, new DistilModeBoxTransform());
        behaviours.add(distilMode);


        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 4000, true)
                .whenFluidUpdates(this::sendData);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 1, 4000, true)
                .whenFluidUpdates(this::sendData)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }

    public SteelTankBlockEntity getTankControllerBE(){
        if(tankController == null)
            return null;
        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            return tank.getControllerBE();
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();

        if(level.isClientSide)
            return;

        SteelTankBlockEntity tankController = getTankControllerBE();

        if(tankController == null)
            return;
        if(tankController.heat < 2)
            return;
        if(inputTank.isEmpty())
            return;


        if(!DistillingRecipe.match(this, currentRecipe)){
            currentRecipe = null;
        }
        if(currentRecipe == null)
            return;


        if(timer <= 0){
            //apply time!

            FluidIngredient fluidIngredient = currentRecipe.getFluidIngredients().get(0);
            int amountRequired = fluidIngredient.getRequiredAmount();

            for (int tank = 0; tank < inputTank.getPrimaryHandler().getTanks(); tank++) {
                FluidStack fluidStack = inputTank.getPrimaryHandler().getFluidInTank(tank);
                if (!fluidIngredient.test(fluidStack))
                    continue;
                int drainedAmount = Math.min(amountRequired, fluidStack.getAmount());
                fluidStack.shrink(drainedAmount);
            }

            int output = -1;
            for(FluidStack result: currentRecipe.getFluidResults()){
                output++;

                BlockPos pos = outputs.get(output);
                if(pos == null)
                    continue;
                BlockEntity be = level.getBlockEntity(pos);
                if(!(be instanceof DistillationOutputBlockEntity))
                    continue;

                DistillationOutputBlockEntity outBE = (DistillationOutputBlockEntity) be;


                outBE.tankInventory.allowInsertion();
                outBE.tankInventory.getPrimaryHandler().fill(result, IFluidHandler.FluidAction.EXECUTE);
                outBE.tankInventory.forbidInsertion();
            }

            if(DistillingRecipe.match(this, currentRecipe)){
                timer = currentRecipe.getProcessingDuration();
            }else{
                currentRecipe = null;
            }

        }else{
            timer -= (int)tankController.heat;
        }


    }

    @Override
    public void lazyTick() {
        super.lazyTick();

        if(tankController == null){
            for(Direction d : Iterate.directions){
                BlockEntity neighbour = getLevel().getBlockEntity(worldPosition.relative(d));
                if(neighbour instanceof SteelTankBlockEntity tank){
                    tankController = tank.getController();
                    break;
                }
            }
        }else{
            if(!getLevel().getBlockState(tankController).is(GearboxBlocks.STEEL_FLUID_TANK.get()))
                tankController = null;
        }
        if(tankController == null)
            return;

        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            tank.distillationController = worldPosition;
            tank.setDistillationMode(true);
        }


        if(currentRecipe == null) {
            Optional<DistillingRecipe> recipeOptional = GearboxRecipeTypes.DISTILLING.find(this, level);

            if (recipeOptional.isEmpty())
                return;

            currentRecipe = recipeOptional.get();
            timer = currentRecipe.getProcessingDuration();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if(tankController == null)
            return;

        BlockEntity be = level.getBlockEntity(tankController);
        if(be instanceof SteelTankBlockEntity tank){
            tank.distillationController = null;
            tank.setDistillationMode(false);
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        inputTank.write(tag, clientPacket);
        outputTank.write(tag, clientPacket);
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        inputTank.read(tag, clientPacket);
        outputTank.read(tag, clientPacket);
        super.read(tag, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        containedFluidTooltip(tooltip, isPlayerSneaking, getCapability(ForgeCapabilities.FLUID_HANDLER));

        GearboxLang.text("").forGoggles(tooltip);
        GearboxLang.translate("gui.distil_mode").text(":").forGoggles(tooltip);
        GearboxLang.translate("gui.distil_mode." + distilMode.get().toString().toLowerCase())
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        return true;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side.getAxis() == DistillationControllerBlock.getAxis(getBlockState()) )){
            return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    private class DistilModeBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8f, 8f, 16f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return DistillationControllerBlock.shouldRenderHeadOnFaceStatic(state, direction);
        }
    }



    public static enum DistilMode implements INamedIconOptions {
        DISTIL_FLASH(GearboxIcons.DISTIL_FLASH),
        DISTIL_ATMOSPHERIC(GearboxIcons.DISTIL_ATMOSPHERIC),
        DISTIL_VACUUM(GearboxIcons.DISTIL_VACUUM),
        ;

        private String translationKey;
        private GearboxIcons icon;

        private DistilMode(GearboxIcons icon) {
            this.icon = icon;
            translationKey = "gearbox.gui.distil_mode." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }
}
