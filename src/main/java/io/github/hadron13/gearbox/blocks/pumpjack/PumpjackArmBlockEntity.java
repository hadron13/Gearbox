package io.github.hadron13.gearbox.blocks.pumpjack;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.hadron13.gearbox.blocks.laser.LaserBeamBehavior;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.pumpjack.PumpjackArmBlock.HORIZONTAL_FACING;

public class PumpjackArmBlockEntity extends SmartBlockEntity  {

    public PumpjackCrankBlockEntity crank;

    public PumpjackArmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(5);
    }


    public AABB renderBoundingBox = new AABB(worldPosition.offset(-2, -2, -2), worldPosition.offset(2, 1, 2));

    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return renderBoundingBox;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if(level == null)
            return;
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        BlockEntity blockEntity = level.getBlockEntity(getBlockPos().below(2).relative(facing, 2));
        if(blockEntity instanceof PumpjackCrankBlockEntity){
            crank = (PumpjackCrankBlockEntity) blockEntity;
        }else{
            crank = null;
        }
    }


}
