package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeFinder;
import io.github.hadron13.gearbox.blocks.laser.InternalEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ElectrolyzerBlockEntity extends MechanicalMixerBlockEntity {

    public static final Object electrolyzingRecipeKey = new Object();

    public final InternalEnergyStorage energyStorage;
    public LazyOptional<IEnergyStorage> lazyEnergy;
    public ElectrolyzerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        energyStorage = new InternalEnergyStorage(100, 10, 10);
        lazyEnergy = LazyOptional.of(() -> energyStorage);
    }

    @Override
    public float getSpeed(){

//        if(energyStorage.getEnergyStored() < 1)
//            return 0;
        return 32f;
    }

    @Override
    public float getRenderedHeadRotationSpeed(float partialTicks) {
        return running? getSpeed()/2 : getSpeed()/4;
    }

    @Override
    public void renderParticles() {

        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent() || level == null)
            return;

        spillParticle(ParticleTypes.BUBBLE.getType());
        spillParticle(ParticleTypes.BUBBLE.getType());

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

        List<Recipe<?>> list = RecipeFinder.get(getRecipeCacheKey(), level, this::matchStaticFilters);
        return list.stream()
                .filter(this::matchBasinRecipe)
                .sorted((r1, r2) -> r2.getIngredients()
                        .size()
                        - r1.getIngredients()
                        .size())
                .collect(Collectors.toList());
    }

    @Override
    protected Optional<CreateAdvancement> getProcessedRecipeTrigger(){
        return Optional.empty();
    }
    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> r) {
        return r instanceof ElectrolyzingRecipe;
    }

    @Override
    public Object getRecipeCacheKey(){
        return electrolyzingRecipeKey;
    }

}
