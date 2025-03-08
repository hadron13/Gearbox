package io.github.hadron13.gearbox.blocks.chemical_reactor;

import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.hadron13.gearbox.blocks.laser.InternalEnergyStorage;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillingRecipe;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.hadron13.gearbox.blocks.laser.LaserBlock.HORIZONTAL_FACING;

public class ReactorBlockEntity extends MechanicalMixerBlockEntity {

    public static final Object reactingRecipeKey = new Object();
    public SmartFluidTankBehaviour atmosphere_tank;

    public ReactorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);

        atmosphere_tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1500, true);
        atmosphere_tank.forbidExtraction();
        behaviours.add(atmosphere_tank);
    }

    @Override
    public void tick(){
        super.tick();
        if(level != null && level.isClientSide)
            return;
        atmosphere_tank.allowExtraction();
        atmosphere_tank.getPrimaryHandler().drain(5, IFluidHandler.FluidAction.EXECUTE);
        atmosphere_tank.forbidExtraction();
    }

    @Override
    public float getRenderedHeadRotationSpeed(float partialTicks) {
        return running? getSpeed()/2 : getSpeed()/4;
    }

    @Override
    public float getRenderedHeadOffset(float partialTicks) {
        return 17/16f;
    }

    @Override
    public void renderParticles() {

        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent() || level == null)
            return;

        if(level.random.nextInt(4) != 1)
            return;

        for (SmartInventory inv : basin.get()
                .getInvs()) {
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getItem(slot);
                if (stackInSlot.isEmpty())
                    continue;
                ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
                spillParticle(data);
            }
        }

        for (SmartFluidTankBehaviour behaviour : basin.get()
                .getTanks()) {
            if (behaviour == null)
                continue;
            for (SmartFluidTankBehaviour.TankSegment tankSegment : behaviour.getTanks()) {
                if (tankSegment.isEmpty(0))
                    continue;
                spillParticle(FluidFX.getFluidParticle(tankSegment.getRenderedFluid()));
            }
        }
    }
    @Override
    protected List<Recipe<?>> getMatchingRecipes() {
        if (getBasin().map(BasinBlockEntity::isEmpty)
                .orElse(true))
            return new ArrayList<>();

        List<ReactingRecipe> list= level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.REACTING.getType());

        return list.stream()
                .filter(this::matchBasinRecipe)
                .sorted((r1, r2) -> r2.getIngredients()
                        .size()
                        - r1.getIngredients()
                        .size())
                .collect(Collectors.toList());
    }
    @Override
    protected <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
        if (recipe == null)
            return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent())
            return false;

        return ReactingRecipe.match(this, (ReactingRecipe) recipe);
    }

    @Override
    public Optional<BasinBlockEntity> getBasin() {
        return super.getBasin();
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger(){
        return Optional.empty();
    }
    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof ReactingRecipe;
    }

    @Override
    public boolean isSpeedRequirementFulfilled() {
        return true;
    }

    @Override
    public Object getRecipeCacheKey(){
        return reactingRecipeKey;
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) { LangBuilder mb = Lang.translate("generic.unit.millibuckets");
        boolean empty_tooltip = true;
        FluidStack atmosphere = atmosphere_tank.getPrimaryHandler().getFluidInTank(0);
        if (!atmosphere.isEmpty()) {
            empty_tooltip = false;
            Lang.text("")
                    .add(Lang.translate("hint.reactor.atmosphere"))
                    .add(Lang.text(" "))
                    .add(Lang.fluidName(atmosphere)
                            .add(Lang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(Lang.number(atmosphere.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
        }
        return !empty_tooltip || super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if(side.getClockWise().getAxis() == getBlockState().getValue(HORIZONTAL_FACING).getAxis() &&
                cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if(getBasin().isPresent()) return getBasin().get().getCapability(cap);
        }
        if(side == getBlockState().getValue(HORIZONTAL_FACING) &&
                cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return atmosphere_tank.getCapability().cast();
        }

        return LazyOptional.empty();
    }

}
