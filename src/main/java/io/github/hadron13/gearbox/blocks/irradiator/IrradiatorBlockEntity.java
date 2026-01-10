package io.github.hadron13.gearbox.blocks.irradiator;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

import static com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour.ProcessingResult.HOLD;
import static com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour.ProcessingResult.PASS;
import static com.simibubi.create.content.kinetics.press.PressingBehaviour.Mode.BASIN;
import static io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlock.HORIZONTAL_FACING;

public class IrradiatorBlockEntity extends BasinOperatingBlockEntity implements ILaserReceiver {

    public static final Object irradiatingRecipesKey = new Object();

    public Laser receivingLaser = null;
    public int recipeTimer = 0;
    
    public static final float LENS_POSITION_END = 4/16f;
    public static final float LENS_POSITION_START = 0f;

    public float targetLensPosition = 0;
    public float previousLensPos = 0;
    public float lensPosition = 0;

    public BeltProcessingBehaviour beltBehavior;
    public PressingBehaviour.Mode mode = PressingBehaviour.Mode.WORLD;

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public TransmutingRecipe getBeltRecipe(){
        if(currentRecipe instanceof TransmutingRecipe)
            return (TransmutingRecipe) currentRecipe;
        return null;
    }
    public IrradiatingRecipe getBasinRecipe(){
        if(currentRecipe instanceof IrradiatingRecipe)
            return (IrradiatingRecipe) currentRecipe;
        return null;
    }
    public int getRecipeColor(){
        if(currentRecipe instanceof LaserRecipe laserRecipe)
            return laserRecipe.getColor();
        return 0;
    }
    public float getRecipePower(){
        if(currentRecipe instanceof LaserRecipe laserRecipe)
            return laserRecipe.getPower();
        return 0f;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        beltBehavior = new BeltProcessingBehaviour(this)
                            .whenItemEnters(this::onItemReceived)
                            .whileItemHeld(this::whenItemHeld);
        behaviours.add(beltBehavior);


    }


    protected BeltProcessingBehaviour.ProcessingResult onItemReceived(TransportedItemStack transported,
                                                                      TransportedItemStackHandlerBehaviour handler) {
        if(handler.blockEntity.isVirtual())
            return PASS;

        if(getSpeed() == 0)
            return PASS;

        Optional<TransmutingRecipe> recipe = GearboxRecipeTypes.TRANSMUTING.find(this, level, transported.stack);
        if(recipe.isEmpty())
            return PASS;
        currentRecipe = recipe.get();
        recipeTimer = getBeltRecipe().getProcessingDuration();
        targetLensPosition = 4f/16f;
        sendData();
        return HOLD;
    }

    protected BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack transported,
                                                                    TransportedItemStackHandlerBehaviour handler) {
        if(getSpeed() == 0)
            return PASS;

        if(!TransmutingRecipe.match(this, getBeltRecipe(), transported.stack)) {
            recipeTimer = 0;
            currentRecipe = null;
            targetLensPosition = LENS_POSITION_START;
            sendData();
            return PASS;
        }

        if(recipeTimer > 0)
            return HOLD;

        Optional<TransmutingRecipe> recipe = GearboxRecipeTypes.TRANSMUTING.find(this, level, transported.stack);
        if(recipe.isEmpty())
            return PASS;

        List<ItemStack> results = RecipeApplier.applyRecipeOn(level, new ItemStack(transported.stack.getItem(), 1), recipe.get(), true);
//        List<ItemStack> results = RecipeApplier.applyRecipeOn(getLevel(),
//                 ItemHandlerHelper.copyStackWithSize(transported.stack, 1), recipe.get());

        List<TransportedItemStack> collect = results.stream()
                .map(stack -> {
                    TransportedItemStack copy = transported.copy();
                    boolean centered = BeltHelper.isItemUpright(stack);
                    copy.stack = stack;
                    copy.locked = true;
                    copy.angle = centered ? 180 : level.random.nextInt(360);
                    return copy;
                })
                .collect(Collectors.toList());

        if (transported.stack.getCount() == 1) {
            if (collect.isEmpty())
                handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.removeItem());
            else
                handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(collect));

        } else {
            TransportedItemStack left = transported.copy();
            left.stack.shrink(1);

            if (collect.isEmpty())
                handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(left));
            else
                handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(collect, left));
            if( AllBlocks.DEPOT.has(level.getBlockState(getBlockPos().below(2))) ) {
                if (TransmutingRecipe.match(this, recipe.get(), left.stack)) {
                    recipeTimer = recipe.get().getProcessingDuration();
                    targetLensPosition = 4 / 16f;
                    sendData();
                }
            }
        }

        return HOLD;
    }


    @Override
    public void tick(){
        super.tick();

//        updateBasin();


        if(mode == BASIN && getBasin().isEmpty()){
            targetLensPosition = 0;
            recipeTimer = 0;
            currentRecipe = null;
            sendData();
        }

        if(getBasin().isPresent()){
            mode = BASIN;
        }else if(AllBlocks.BELT.has(level.getBlockState(getBlockPos().below(2))) ||
                 AllBlocks.DEPOT.has(level.getBlockState(getBlockPos().below(2))) ){
            mode = PressingBehaviour.Mode.BELT;
        }else{
            mode = PressingBehaviour.Mode.WORLD;
        }

//        if(level.isClientSide){
        previousLensPos = lensPosition;
        lensPosition += (targetLensPosition - lensPosition) * .1f * Math.abs(getSpeed()/64f);
//            return;
//        }
        if(currentRecipe == null)
            return;

        if(recipeTimer > 0 && lensPosition > 2/16f){
            recipeTimer -= (int)(receivingLaser.getPower()/getRecipePower()) * 3;
            if(recipeTimer <= 0){
                if(mode == BASIN) {
                    applyBasinRecipe();
                }
                recipeTimer = 0;
                targetLensPosition = LENS_POSITION_START;
                sendData();
            }
        }
    }


    @Override
    protected boolean isRunning() {
        return recipeTimer > 0;
    }
    public void startProcessingBasin() {
        recipeTimer = getBasinRecipe().getProcessingDuration();
        targetLensPosition = LENS_POSITION_END;
        super.startProcessingBasin();
    }
    @Override
    protected void onBasinRemoved() {
        currentRecipe = null;
        recipeTimer = 0;
        basinChecker.scheduleUpdate();
        sendData();
    }

    @Override
    protected boolean matchStaticFilters(RecipeHolder<? extends Recipe<?>> recipe) {
        return recipe.value() instanceof IrradiatingRecipe;
    }


    @Override
    protected void applyBasinRecipe() {
        if (currentRecipe == null)
            return;

        Optional<BasinBlockEntity> optionalBasin = getBasin();
        if (!optionalBasin.isPresent())
            return;
        BasinBlockEntity basin = optionalBasin.get();
        boolean wasEmpty = basin.canContinueProcessing();
        if (!IrradiatingRecipe.apply(basin, currentRecipe))
            return;
        getProcessedRecipeTrigger().ifPresent(this::award);
        basin.inputTank.sendDataImmediately();

        // Continue mixing
        if (wasEmpty && matchBasinRecipe(currentRecipe)) {
            continueWithPreviousRecipe();
            sendData();
        }

        basin.notifyChangeOfContents();
    }

    @Override
    protected <I extends RecipeInput> boolean matchBasinRecipe(Recipe<I> recipe) {
        if(!(recipe instanceof IrradiatingRecipe irradiatingRecipe))
            return false;
        Optional<BasinBlockEntity> basin = getBasin();
        if (!basin.isPresent())
            return false;
        return IrradiatingRecipe.match(this, irradiatingRecipe) && IrradiatingRecipe.matchBasin(getBasin().get(), recipe);
    }


    @Override
    protected Object getRecipeCacheKey() {
        return irradiatingRecipesKey;
    }

    @Override
    protected void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putFloat("lens", targetLensPosition);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        targetLensPosition = compound.getFloat("lens");
        super.read(compound, registries, clientPacket);
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        Laser.spectrometryTooltip(tooltip, isPlayerSneaking, receivingLaser);

        return true;
    }

    @Override
    public void receiveLaser(Laser laser) {
        if(receivingLaser == null && laser.hasValidAngle(new Vec3(getBlockState().getValue(HORIZONTAL_FACING).getOpposite().step()) , 15f, 15f)){
            receivingLaser = laser;
        }
    }

    @Override
    public void endReceiveLaser(Laser laser) {
        receivingLaser = null;
    }
}
