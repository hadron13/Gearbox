package io.github.hadron13.gearbox.blocks.laser_drill;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.simibubi.create.content.kinetics.press.PressingBehaviour.Mode.BASIN;
import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity.truncatePrecision;

public class LaserDrillBlockEntity extends SmartBlockEntity implements ILaserReceiver, IHaveGoggleInformation {

    public Map<Direction, Float> powers;
    public Map<Direction, Color> colors;
    public Map<Direction, Integer> timeouts;
    public boolean colorChanged = false;
    public Color mixedColor = Color.BLACK;
    public float totalPower = 0f;

    public Block blockUnder;
    public int recipeTimer = 0;
    public LaserDrillingRecipe currentRecipe;



    public LaserDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
    }

    @Override
    public void lazyTick(){
        blockUnder = level.getBlockState(getBlockPos().below(2)).getBlock();
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
        if(colorChanged)
            updateColors();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

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

        float ceil = Math.max(r, Math.max(g, b));
        if(ceil == 0){
            r = 0;
            g = 0;
            b = 0;
        }else {
            r = (int)(((float)r/ceil) * 255.0f);
            g = (int)(((float)g/ceil) * 255.0f);
            b = (int)(((float)b/ceil) * 255.0f);
        }

        mixedColor = new Color(r,g,b);
        colorChanged = false;

        sendData();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
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
                .add(Lang.text(" " + truncatePrecision(mixedColor.getRed()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x2d9636)
                .add(Lang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                .add(Lang.text(" " + truncatePrecision(mixedColor.getGreen()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x3e3dbf)
                .add(Lang.translate("gui.spectrometer.blue").style(ChatFormatting.BLUE))
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
