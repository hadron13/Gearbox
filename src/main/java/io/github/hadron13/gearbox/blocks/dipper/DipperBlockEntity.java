package io.github.hadron13.gearbox.blocks.dipper;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.List;

public class DipperBlockEntity extends KineticBlockEntity {


    public LazyOptional<IFluidHandler> fluidCapability;
    public SmartFluidTankBehaviour atmosphere;

    public LazyOptional<IItemHandlerModifiable> itemCapability;
    public Couple<SmartInventory> invs;

    public boolean running = false;
    public int remainingDips = 0;


    public DipperBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        invs = Couple.create(new SmartInventory(1, this).forbidExtraction(), new SmartInventory(1, this).forbidInsertion());
        itemCapability = LazyOptional.of(() -> new CombinedInvWrapper(invs.getFirst(), invs.getSecond()));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        atmosphere = SmartFluidTankBehaviour.single(this, 1000);
        behaviours.add(atmosphere);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0, -1.5, 0);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
