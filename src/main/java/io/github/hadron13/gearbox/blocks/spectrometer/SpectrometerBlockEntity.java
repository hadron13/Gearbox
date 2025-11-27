package io.github.hadron13.gearbox.blocks.spectrometer;

import com.simibubi.create.content.kinetics.gauge.GaugeBlock;
import com.simibubi.create.content.kinetics.gauge.GaugeBlockEntity;
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.laser.ILaserReader;
import io.github.hadron13.gearbox.blocks.laser.Laser;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock.AXIS_ALONG_FIRST_COORDINATE;
import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlock.FACING;

public class SpectrometerBlockEntity extends GaugeBlockEntity implements ILaserReader {
    public SpectrometerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public Laser passingLaser;
    public float distanceToLaser;

    @Override
    public void tick(){
        super.tick();
        if(passingLaser != null &&
                (!passingLaser.enabled || passingLaser.power < 0.01 ||
                  passingLaser.length < distanceToLaser ||
                  passingLaser.getPosition().add(passingLaser.getDirection().scale(distanceToLaser)).distanceTo(getPosition()) > 0.5)
        ){
            passingLaser = null;
        }
    }

    public Vec3 getPosition(){
        return getBlockPos().getCenter();
    }

    @Override
    public boolean receiveLaser(Laser laser) {
        Direction.Axis laserAxis = getRotationAxis(getBlockState());
        Vec3 forward = new Vec3(Direction.fromAxisAndDirection(laserAxis, Direction.AxisDirection.POSITIVE).step());
        Vec3 backward = new Vec3(Direction.fromAxisAndDirection(laserAxis, Direction.AxisDirection.NEGATIVE).step());

        if(!laser.hasValidAngle(forward, 30, 30) && !laser.hasValidAngle(backward, 30, 30))
            return false;

        if(passingLaser == null){
            passingLaser = laser;
        }

        float red   = ((passingLaser.color >> 16) & 0xFF) / 255.0f;
        float green = ((passingLaser.color >> 8)  & 0xFF) / 255.0f;
        float blue  = ((passingLaser.color)       & 0xFF) / 255.0f;

        dialTarget = Mth.clamp( Mth.lerp( Mth.clamp(green - (blue+red)/2, 0 , 1),blue - red, 0.5f) , 0, 1 );

        distanceToLaser = (float)laser.getPosition().distanceTo(getPosition());

        return true;
    }

    public static String truncatePrecision(float number, int decimals){
        return ("" + Math.floor(number * Math.pow(10, decimals))/Math.pow(10, decimals));
    }
    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        super.addToGoggleTooltip(tooltip, isPlayerSneaking);

        if(passingLaser == null){
            GearboxLang.translate("gui.spectrometer.nolaser")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
            return true;
        }

        float red   = ((passingLaser.color >> 16) & 0xFF) / 255.0f;
        float green = ((passingLaser.color >> 8)  & 0xFF) / 255.0f;
        float blue  = ((passingLaser.color)       & 0xFF) / 255.0f;

        GearboxLang.translate("gui.spectrometer.title")
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip);
        GearboxLang.text("\u2592 ").color(0xffffff)
                .add(GearboxLang.translate("gui.spectrometer.power").style(ChatFormatting.WHITE))
                .add(GearboxLang.text(" " + truncatePrecision(passingLaser.power, 2) ))
                .forGoggles(tooltip);
        GearboxLang.text("\u2588 ").color(0xbd5252)
                .add(GearboxLang.translate("gui.spectrometer.red").style(ChatFormatting.DARK_RED))
                .add(GearboxLang.text(" " + truncatePrecision(red, 2) ))
                .forGoggles(tooltip);
        GearboxLang.text("\u2588 ").color(0x2d9636)
                .add(GearboxLang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                .add(GearboxLang.text(" " + truncatePrecision(green, 2) ))
                .forGoggles(tooltip);
        GearboxLang.text("\u2588 ").color(0x3e3dbf)
                .add(GearboxLang.translate("gui.spectrometer.blue").style(ChatFormatting.BLUE))
                .add(GearboxLang.text(" " + truncatePrecision(blue, 2) ))
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
