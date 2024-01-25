package io.github.hadron13.gearbox.blocks.spectrometer;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Lang;
import io.github.hadron13.gearbox.blocks.laser.ILaserReader;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock.AXIS_ALONG_FIRST_COORDINATE;
import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock.FACING;

public class SpectrometerBlockEntity extends GaugeBlockEntity implements ILaserReader {
    public SpectrometerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }
    public Color currentColor = Color.BLACK;
    public float currentPower = 0;
    public int timeout = 0;

    @Override
    public void tick(){
        super.tick();
        if(timeout != 0) {
            timeout--;
        }else{
            currentColor = Color.BLACK;
            currentPower = 0;
        }
    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {

        if(face.getAxis() == getRotationAxis(getBlockState()) ){
            currentColor = color;
            currentPower = power;
            float r = color.getRed()/255f;
            float g = color.getGreen()/255f;
            float b = color.getBlue()/255f;

            dialTarget = Mth.clamp( Mth.lerp( Mth.clamp(g - (b+r)/2, 0 , 1),b - r, 0.5f) , 0, 1 );
            timeout = 3;
            return  true;
        }
        return false;
    }

    public static String truncatePrecision(float number, int decimals){
        return ("" + Math.floor(number * Math.pow(10, decimals))/Math.pow(10, decimals));
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if(currentPower == 0 || currentColor == Color.BLACK){
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
                .add(Lang.text(" " + truncatePrecision(currentPower, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0xbd5252)
                .add(Lang.translate("gui.spectrometer.red").style(ChatFormatting.DARK_RED))
                .add(Lang.text(" " + truncatePrecision(currentColor.getRed()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x2d9636)
                .add(Lang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                .add(Lang.text(" " + truncatePrecision(currentColor.getGreen()/255f, 2) ))
                .forGoggles(tooltip);
        Lang.text("\u2588 ").color(0x3e3dbf)
                .add(Lang.translate("gui.spectrometer.blue").style(ChatFormatting.BLUE))
                .add(Lang.text(" " + truncatePrecision(currentColor.getBlue()/255f, 2) ))
                .forGoggles(tooltip);

        return true;
    }
    public Direction.Axis getRotationAxis(BlockState state) {
        Direction.Axis pistonAxis = state.getValue(FACING)
                .getAxis();
        boolean alongFirst = state.getValue(AXIS_ALONG_FIRST_COORDINATE);

        if (pistonAxis == Direction.Axis.X)
            return alongFirst ? Direction.Axis.Y : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Y)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Z;
        if (pistonAxis == Direction.Axis.Z)
            return alongFirst ? Direction.Axis.X : Direction.Axis.Y;

        throw new IllegalStateException("Unknown axis??");
    }

}
