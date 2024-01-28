package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import com.mojang.math.Vector3f;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.belt.BeltHelper;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.stream.Collectors;

import static com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour.ProcessingResult.HOLD;
import static com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour.ProcessingResult.PASS;
import static com.simibubi.create.content.kinetics.press.PressingBehaviour.Mode.BASIN;
import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity.truncatePrecision;

public class IrradiatorBlockEntity extends BasinOperatingBlockEntity implements ILaserReceiver {

    public static final Object irradiatingRecipesKey = new Object();
    public Map<Direction, Float> powers;
    public Map<Direction, Color> colors;
    public Map<Direction, Integer> timeouts;
    public boolean colorChanged = false;
    public Color mixedColor = Color.BLACK;
    public Color recipeColor = Color.BLACK;
    public float totalPower = 0f;
    public int recipeTimer = 0;

    public float targetLensPosition = 0;
    public float previousLensPos = 0;
    public float lensPosition = 0;

    public BeltProcessingBehaviour beltBehavior;
    public PressingBehaviour.Mode mode = PressingBehaviour.Mode.WORLD;

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
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
    public Color getRecipeColor(){
        if(currentRecipe instanceof LaserRecipe laserRecipe)
            return laserRecipe.getColor();
        return Color.BLACK;
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

        Optional<TransmutingRecipe> recipe = ModRecipeTypes.TRANSMUTING.find(this, level, transported.stack);
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
            targetLensPosition = 0f;
            sendData();
            return PASS;
        }

        if(recipeTimer > 0)
            return HOLD;

        Optional<TransmutingRecipe> recipe = ModRecipeTypes.TRANSMUTING.find(this, level, transported.stack);
        if(recipe.isEmpty())
            return PASS;

        List<ItemStack> results = RecipeApplier.applyRecipeOn(
                 ItemHandlerHelper.copyStackWithSize(transported.stack, 1), recipe.get());

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
        timeouts.forEach((dir, timer) -> {
            if(timer != 0)
                timeouts.replace(dir, --timer);
            else{
                powers.remove(dir);
                colors.remove(dir);
                colorChanged = true;
            }
        });
        if(colorChanged)
            updateColors();

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

        if(level.isClientSide){
            previousLensPos = lensPosition;
            lensPosition += (targetLensPosition - lensPosition) * .1f * Math.abs(getSpeed()/64f);
            return;
        }
        if(currentRecipe == null)
            return;

        if(recipeTimer > 0){
            recipeTimer -= (int)(totalPower/getRecipePower());
            if(recipeTimer <= 0){
                if(mode == BASIN) {
                    applyBasinRecipe();
                }
                recipeTimer = 0;
                targetLensPosition = 0f;
                sendData();
            }
        }

    }

    public void updateColors(){
        totalPower = 0f;
        for(float power : powers.values())
            totalPower += power;

        if(totalPower <= 0.1f){
            mixedColor = Color.BLACK;
            sendData();
            return;
        }

        int r = 0, g = 0, b = 0;
        for (Color color : colors.values()) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }

//        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
//        hsb[2] = Mth.clamp(hsb[2] * 3f, 0.5f, 1f);

//        int rgb =  java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        mixedColor = new Color(r,g,b);
        colorChanged = false;
        recipeColor = mixedColor;

        if(currentRecipe != null && mode == BASIN){
            if(!IrradiatingRecipe.match(this, getBasinRecipe()) ) {
                recipeTimer = 0;
                currentRecipe = null;
            }
        }
        sendData();
    }

    @Override
    protected boolean isRunning() {
        return recipeTimer > 0;
    }
    public void startProcessingBasin() {
        recipeTimer = getBasinRecipe().getProcessingDuration();
        targetLensPosition = 4/16f;
        super.startProcessingBasin();
    }
    @Override
    protected void onBasinRemoved() {
        currentRecipe = null;
        recipeTimer = 0;
        basinChecker.scheduleUpdate();
        sendData();
    }

    protected <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
        if(!(recipe instanceof IrradiatingRecipe))
            return false;
        return super.matchBasinRecipe(recipe) && IrradiatingRecipe.match(this, (IrradiatingRecipe)recipe);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return recipe.getType() == ModRecipeTypes.IRRADIATING.getType();
    }

    @Override
    protected Object getRecipeCacheKey() {
        return irradiatingRecipesKey;
    }
    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("lens", targetLensPosition);
//        compound.putInt("recipeColor", recipeColor.getRGB());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        targetLensPosition = compound.getFloat("lens");
//        recipeColor.setValue(compound.getInt("recipeColor"));
        super.read(compound, clientPacket);
    }



    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        super.addToGoggleTooltip(tooltip, isPlayerSneaking);
        if(totalPower == 0 || mixedColor== Color.BLACK){
            Lang.translate("gui.spectrometer.nolaser")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
            return true;
        }


        Lang.translate("gui.spectrometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        Lang.text("\u2592 ").color(0xffffff)
                .add(Lang.translate("gui.spectrometer.power").style(ChatFormatting.WHITE))
                .add(Lang.text(" " + truncatePrecision(totalPower, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0xbd5252)
                .add(Lang.translate("gui.spectrometer.red").style(ChatFormatting.DARK_RED))
                .add(Lang.text(" " + truncatePrecision(recipeColor.getRed()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x2d9636)
                .add(Lang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                .add(Lang.text(" " + truncatePrecision(recipeColor.getGreen()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x3e3dbf)
                .add(Lang.translate("gui.spectrometer.blue").style(ChatFormatting.BLUE))
                .add(Lang.text(" " + truncatePrecision(recipeColor.getBlue()/255f, 2) ))
                .forGoggles(tooltip);



        return true;
    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {

        if(face.getAxis() != Direction.Axis.Y){

            if(color == Color.BLACK || power <= 0.1f){
                powers.remove(face);
                colors.remove(face);
                timeouts.remove(face);
                return true;
            }
            if(colors.get(face) != color){
                colors.put(face, color);
                colorChanged = true;
            }
            powers.put(face, power);
            timeouts.put(face, 3);
        }

        return false;
    }
}
