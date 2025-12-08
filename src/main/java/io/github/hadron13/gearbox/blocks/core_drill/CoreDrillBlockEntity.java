package io.github.hadron13.gearbox.blocks.core_drill;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.logistics.depot.DepotBlock;
import io.github.hadron13.gearbox.compat.adlods.AdlodDepositDetector;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.IntAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;

public class CoreDrillBlockEntity extends KineticBlockEntity {

    public int tubes = 0;
    public int drillState = 0;
    public LerpedFloat tubeOffset = LerpedFloat.linear();
    public LerpedFloat poleOffset = LerpedFloat.linear();

    public static final int IDLE = 0;
    public static final int PUSHING = 1;
    public static final int PULLING = 2;
    public static final int STORING = 3;

    public Block minedBlock;
    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    public int timer;
    public static final int OUTPUT_SLOTS = 3;
    public List<Integer> spoutputIndex;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    public List<IntAttached<ItemStack>>  visualizedOutputItems;

    public CoreDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        tubeOffset.chase(0f, 1 / 16f, LerpedFloat.Chaser.LINEAR);
        poleOffset.chase(0f, 1 / 16f, LerpedFloat.Chaser.LINEAR);
        minedBlock = null;
        if(level instanceof ServerLevel serverLevel) {
            minedBlock = AdlodDepositDetector.getDeposit(serverLevel, getBlockPos()).orElse(null);
        }

    }

    @Override
    public void tick() {
        super.tick();

        if(drillState != 0){

            poleOffset.tickChaser();
            tubeOffset.tickChaser();

            if(poleOffset.getValue() > 0.99){
                poleOffset.updateChaseTarget(0);
            }

            if(poleOffset.settled()){
                tubeOffset.setValue(0);
                drillState = 0;
                tubes++;


                if(level instanceof ServerLevel serverLevel) {
                    minedBlock = AdlodDepositDetector.getDeposit(serverLevel, getBlockPos()).orElse(null);
                    sendData();
                }
            }
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        if(minedBlock != null)
            compound.put("minedBlock", NbtUtils.writeBlockState(minedBlock.defaultBlockState()));
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        if(compound.contains("minedBlock"))
            minedBlock = NbtUtils.readBlockState(blockHolderGetter(), compound.getCompound("minedBlock")).getBlock();
    }
}
