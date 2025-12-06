package io.github.hadron13.gearbox.blocks.core_drill;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.ore_counter.CapabilityOreCounter;
import net.createmod.catnip.data.IntAttached;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
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
    public float animationProgress = 0f;

    public static final int IDLE = 0;
    public static final int PUSHING = 1;
    public static final int PULLING = 2;
    public static final int STORING = 3;

    public Item heldItem;

    protected LazyOptional<IItemHandlerModifiable> itemCapability;

    public int timer;
    public static final int OUTPUT_SLOTS = 3;
    public List<Integer> spoutputIndex;

    public static final int OUTPUT_ANIMATION_TIME = 10;
    public List<IntAttached<ItemStack>>  visualizedOutputItems;

    public CoreDrillBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    public void tick() {
        super.tick();

        if(drillState != 0 && getSpeed() > 0){

            animationProgress += 1.0f;
            if(animationProgress >= 20.0f){
                animationProgress = 0;
                drillState = 0;
                tubes++;
            }
        }

        this.containedChunk().getCapability(CapabilityOreCounter.COUNTER).ifPresent(oreCap -> {

            BlockPos breakingPos = getBlockPos().below();
            if( !oreCap.isNaturallyPlaced(breakingPos)){
//                Gearbox.LOGGER.debug("é jovem");
                return;
            }

            Block target = level.getBlockState(breakingPos).getBlock();
            oreCap.lazyCountBlocksOfType(target, true).ifPresent(count -> {
                if(count > 100){
//                    Gearbox.LOGGER.debug("é idoso");
                }else{
//                    Gearbox.LOGGER.debug("é jovem por skill issue");
                }
            });
        });
    }
}
