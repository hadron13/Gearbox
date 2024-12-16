package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.datafixers.TypeRewriteRule;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlock;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;
import java.util.Optional;

import static io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlock.HORIZONTAL_FACING;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;

public class PumpjackWellBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public SmartFluidTankBehaviour tank;
    public AABB renderBoundingBox;
    public PumpjackRecipe currentRecipe;
    public float timer;

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 2, 2));
        }
        return renderBoundingBox;
    }

    public PumpjackWellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        tank = SmartFluidTankBehaviour.single(this, 2000);
        tank.forbidInsertion();
        behaviours.add(tank);
    }


    public void updateRecipe(){
        if(currentRecipe == null || !PumpjackRecipe.match(this, currentRecipe)){
            Optional<PumpjackRecipe> match = ModRecipeTypes.PUMPJACK.find(this, getLevel());
            if(match.isEmpty())
                return;
            currentRecipe = match.get();
            timer = 10f;
        }
    }
    public boolean validPiping(){
        if(level == null)
            return true;
        BlockPos position = getBlockPos().below();
        BlockState block = level.getBlockState(position);
        while(block.getBlock() != Blocks.BEDROCK){
            if(block.getBlock() != AllBlocks.FLUID_PIPE.get() &&
               block.getBlock() != AllBlocks.ENCASED_FLUID_PIPE.get()) {
                return false;
            }

            position = position.below();
            block = level.getBlockState(position);
        }
        return true;

    }

    public void pump(float speed){
        if(currentRecipe == null)
            return;
        if(isTankFull())
            return;
        if(!validPiping())
            return;

        timer -= speed;

        if(timer > 0)
            return;
        tank.allowInsertion();
        tank.getPrimaryHandler().fill(currentRecipe.getFluidResult(), EXECUTE);
        tank.forbidInsertion();
        timer = 10f;
    }
    public boolean isTankFull(){
        return tank.getPrimaryHandler().getFluidAmount() == 2000;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isFluidHandlerCap(cap)
                && (side == null || getBlockState().getValue(HORIZONTAL_FACING) == side))
            return tank.getCapability().cast();

        return super.getCapability(cap, side);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return containedFluidTooltip(tooltip, isPlayerSneaking, tank.getCapability().cast());
    }




}
