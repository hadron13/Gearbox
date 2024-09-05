package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.millstone.MillingRecipe;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;

public class CentrifugeBlockEntity extends KineticBlockEntity {

    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    public LazyOptional<IFluidHandler> fluidCapability;
    public CentrifugingRecipe lastRecipe = null;
    int recipeTimer = 0;

    public CentrifugeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick(){
        super.tick();
        if(Mth.abs(speed) < 32.0f || inputTank.isEmpty()) {
            recipeTimer = 0;
            return;
        }

        if(recipeTimer > 0){
            recipeTimer -= getProcessingSpeed();

            if(recipeTimer <= 0){
                if(lastRecipe == null)
                    return;
                CentrifugingRecipe.apply(this, lastRecipe, false);
            }
            return;
        }


        if (lastRecipe == null || !CentrifugingRecipe.match(this, lastRecipe)) {
            Optional<CentrifugingRecipe> recipe = ModRecipeTypes.CENTRIFUGING.find(this, level);
            if (!recipe.isPresent()) {
                recipeTimer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get();
                recipeTimer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }
        recipeTimer = lastRecipe.getProcessingDuration();

    }


    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!ICogWheel.isLargeCog(state))
            return super.addPropagationLocations(block, state, neighbours);

        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (offset.distSqr(BlockPos.ZERO) == 2)
                        neighbours.add(worldPosition.offset(offset));
                });
        return neighbours;
    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours){
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000, true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 3, 1000, true);

        inputTank.forbidExtraction();
        outputTank.forbidInsertion();

        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = LazyOptional.of(() -> {
            LazyOptional<? extends IFluidHandler> inputCap = inputTank.getCapability();
            LazyOptional<? extends IFluidHandler> outputCap = outputTank.getCapability();
            return new CombinedTankWrapper(outputCap.orElse(null), inputCap.orElse(null));
        });
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        IFluidHandler fluids = fluidCapability.orElse(new FluidTank(0));
        boolean isEmpty = true;

        LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        for (int i = 0; i < fluids.getTanks(); i++) {
            FluidStack fluidStack = fluids.getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            Lang.text("")
                    .add(Lang.fluidName(fluidStack)
                            .add(Lang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(Lang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
            isEmpty = false;
        }

        if (isEmpty)
            tooltip.remove(0);

        return true;
    }


    @Override
    public void invalidate() {
        super.invalidate();
        fluidCapability.invalidate();
    }
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == null || side.getAxis() == getBlockState().getValue(AXIS)) )
            return fluidCapability.cast();
        return super.getCapability(cap, side);
    }

}
