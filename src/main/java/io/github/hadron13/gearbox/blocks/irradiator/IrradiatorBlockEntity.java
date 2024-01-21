package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity.truncatePrecision;

public class IrradiatorBlockEntity extends BasinOperatingBlockEntity implements ILaserReceiver {

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


    public IrradiatingRecipe currentRecipe;
    public BeltProcessingBehaviour beltBehavior;
    public PressingBehaviour.Mode mode;

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
        currentRecipe = null;
    }
    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        beltBehavior = new BeltProcessingBehaviour(this);
        behaviours.add(beltBehavior);
    }


    @Override
    public void tick(){
        timeouts.forEach((dir, timer) -> {
            if(timer != 0)
                timeouts.replace(dir, --timer);
            else{
                powers.remove(dir);
                colors.remove(dir);
                colorChanged = true;
            }
        });
        if(level.isClientSide){
            previousLensPos = lensPosition;
            lensPosition += (targetLensPosition - lensPosition) * .1f * Math.abs(getSpeed()/64f);
        }

        if(getBasin().isPresent()){
            mode = PressingBehaviour.Mode.BASIN;
        }else if(AllBlocks.BELT.has(level.getBlockState(getBlockPos().below(2))) ){
            mode = PressingBehaviour.Mode.BELT;
        }else{
            mode = PressingBehaviour.Mode.WORLD;
        }

        targetLensPosition = 4f/16f;
        if(colorChanged) {
            updateColors();
        }
    }

    public void updateColors(){
        totalPower = 0f;
        for(float power : powers.values())
            totalPower += power;

        if(totalPower <= 0.1f){
            mixedColor = Color.BLACK;
            targetLensPosition = 0f;
            sendData();
            return;
        }


        int r = 0, g = 0, b = 0;
        for (Color color : colors.values()) {
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        int ammount = colors.values().size();
        r /= ammount;
        g /= ammount;
        b /= ammount;
        if(!level.isClientSide)
            recipeColor = new Color(r, g, b);
        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        hsb[2] = Mth.clamp(hsb[2] * 3f, 0.5f, 1f);

        int rgb =  java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        mixedColor = new Color(rgb);
        colorChanged = false;
        sendData();
    }

    @Override
    protected boolean isRunning() {
        return false;
    }

    @Override
    protected void onBasinRemoved() {

    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putFloat("lens", targetLensPosition);
//        compound.putFloat("power", totalPower);
//        compound.putInt("color", mixedColor.getRGB());
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        targetLensPosition = compound.getFloat("lens");
//        totalPower = compound.getFloat("power");
//        mixedColor.setValue(compound.getInt("color"));
        super.read(compound, clientPacket);
    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return null;
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
        Lang.text("")
                .add(Lang.translate("gui.spectrometer.power").style(ChatFormatting.WHITE))
                .add(Lang.text(" " + truncatePrecision(totalPower, 2) ))
                .forGoggles(tooltip);
        Lang.text("")
                .add(Lang.translate("gui.spectrometer.red").style(ChatFormatting.DARK_RED))
                .add(Lang.text(" " + truncatePrecision(mixedColor.getRed()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("")
                .add(Lang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                .add(Lang.text(" " + truncatePrecision(mixedColor.getGreen()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("")
                .add(Lang.translate("gui.spectrometer.blue").style(ChatFormatting.DARK_BLUE))
                .add(Lang.text(" " + truncatePrecision(mixedColor.getBlue()/255f, 2) ))
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
