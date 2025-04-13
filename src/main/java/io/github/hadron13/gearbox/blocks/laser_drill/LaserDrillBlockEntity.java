package io.github.hadron13.gearbox.blocks.laser_drill;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.theme.Color;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.github.hadron13.gearbox.blocks.spectrometer.SpectrometerBlockEntity.truncatePrecision;

public class LaserDrillBlockEntity extends SmartBlockEntity implements ILaserReceiver, IHaveGoggleInformation {

    public Map<Direction, Float> powers;
    public Map<Direction, Color> colors;
    public Map<Direction, Integer> timeouts;
    public boolean colorChanged = false;
    public Color mixedColor = Color.BLACK;
    public float totalPower = 0f;

    public int breakTimer = 0;
    public boolean bedrockContact = false;
    public int laserLength = 0;

    public SmartInventory output;
    public LazyOptional<IItemHandler> capability;

    LerpedFloat visualSpeed = LerpedFloat.linear();
    float angle;
    public int recipeTimer = 0;
    public LaserDrillingRecipe currentRecipe;


    public AABB renderBoundingBox;
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        if (renderBoundingBox == null) {
            renderBoundingBox = new AABB(worldPosition, worldPosition.offset(1, -384, 1));
        }
        return renderBoundingBox;
    }


    public LaserDrillBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
        setLazyTickRate(10);

        output = new SmartInventory(3, this)
                .forbidInsertion()
                .withMaxStackSize(64);


        capability = LazyOptional.of(() -> new InvWrapper(output));
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, output);
    }

    public boolean drill(boolean test){
        BlockPos pos = getBlockPos();
        int y = pos.getY();
        int i;
        boolean contacted = false;

        for(i = 1; i < y+65; i++) {
            BlockPos currentPosition = pos.below(i);
            BlockState block = level.getBlockState(currentPosition);
            if (block.isAir() || !block.getFluidState().isEmpty())
                continue;
            if (block.is(Tags.Blocks.GLASS) || block.is(Tags.Blocks.GLASS_PANES))
                continue;

            if (block.is(Blocks.BEDROCK)) {
                contacted = true;
                break;
            }
            if(test) break;

            boolean catchesFire = block.isFlammable(level, currentPosition, Direction.UP);
            float hardness = block.getDestroySpeed(level, currentPosition);
            boolean canBurn = hardness > -1 && hardness < totalPower;

            if (!canBurn && !catchesFire) {
                breakTimer = 0;
                break;
            }
            if (totalPower <= 0)
                break;
            breakTimer++;

            if ((canBurn && breakTimer >= (hardness * 10) / totalPower) || (catchesFire && breakTimer >= 20 / totalPower)) {
                level.destroyBlock(currentPosition, true);
                breakTimer = 0;
            }
            break;
        }
        laserLength = i;
        sendData();
        return contacted;
    }

    @Override
    public void lazyTick(){

        if(level.isClientSide)
            return;
        if(bedrockContact)
            bedrockContact = drill(true);
    }


    @Override
    public void tick(){
        super.tick();
        if(level == null)
            return;

        timeouts.forEach((dir, timer) -> {
            if(timer != 0)
                timeouts.replace(dir, --timer);
            else{
                powers.remove(dir);
                colors.remove(dir);
                colorChanged = true;
            }
        });

        if(level.isClientSide) {
            float targetSpeed = totalPower;

            visualSpeed.updateChaseTarget(targetSpeed);
            visualSpeed.tickChaser();
            angle += visualSpeed.getValue() * 3 / 10f;
            angle %= 360;
        }

        if(colorChanged)
            updateColors();

        if(level.isClientSide)
            return;

        if(totalPower < 20f)
            return;

        if(!bedrockContact)
            bedrockContact = drill(false);


        if(bedrockContact){
            if(currentRecipe == null){
                Optional<LaserDrillingRecipe> newRecipe = ModRecipeTypes.LASER_DRILLING.find(this, level);
                if(newRecipe.isPresent()) {
                    currentRecipe = newRecipe.get();
                    recipeTimer = currentRecipe.getProcessingDuration();
                }else{
                    return;
                }
            }
            assert currentRecipe != null;

            if(!LaserDrillingRecipe.match(this, currentRecipe)){
                currentRecipe = null;
                return;
            }
            recipeTimer -= totalPower;
            if(recipeTimer <= 0){

                output.allowInsertion();
                currentRecipe.rollResults().forEach(stack -> ItemHandlerHelper.insertItemStacked(output, stack, false));
                output.forbidInsertion();
                recipeTimer = currentRecipe.getProcessingDuration();
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
            GearboxLang.translate("gui.spectrometer.nolaser")
                    .style(ChatFormatting.DARK_GRAY)
                    .forGoggles(tooltip);
        }else {

            GearboxLang.translate("gui.spectrometer.title")
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip);


            GearboxLang.text("\u2592 ").color(0xffffff)
                    .add(GearboxLang.translate("gui.spectrometer.power").style(ChatFormatting.WHITE))
                    .add(GearboxLang.text(" " + truncatePrecision(totalPower, 2)))
                    .forGoggles(tooltip);
            if (totalPower < 20.0f) {
                GearboxLang.translate("gui.laserdrill.minimum_power")
                        .style(ChatFormatting.GRAY)
                        .forGoggles(tooltip);
                return true;
            }
            GearboxLang.text("\u2588 ").color(0xbd5252)
                    .add(GearboxLang.translate("gui.spectrometer.red").style(ChatFormatting.DARK_RED))
                    .add(GearboxLang.text(" " + truncatePrecision(mixedColor.getRed() / 255f, 2)))
                    .forGoggles(tooltip);
            GearboxLang.text("\u2588 ").color(0x2d9636)
                    .add(GearboxLang.translate("gui.spectrometer.green").style(ChatFormatting.DARK_GREEN))
                    .add(GearboxLang.text(" " + truncatePrecision(mixedColor.getGreen() / 255f, 2)))
                    .forGoggles(tooltip);
            GearboxLang.text("\u2588 ").color(0x3e3dbf)
                    .add(GearboxLang.translate("gui.spectrometer.blue").style(ChatFormatting.BLUE))
                    .add(GearboxLang.text(" " + truncatePrecision(mixedColor.getBlue() / 255f, 2)))
                    .forGoggles(tooltip);
        }

        for (int i = 0; i < output.getSlots(); i++) {
            ItemStack stackInSlot = output.getStackInSlot(i);
            if (stackInSlot.isEmpty())
                continue;
            GearboxLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(GearboxLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
        }

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
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (isItemHandlerCap(cap) && (side == null || side == Direction.UP))
            return capability.cast();

        return super.getCapability(cap, side);
    }


        @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putInt("length",laserLength);
        tag.put("OutputItems", output.serializeNBT());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        laserLength = compound.getInt("length");
        output.deserializeNBT(compound.getCompound("OutputItems"));
        if (clientPacket)
            visualSpeed.chase(Math.log(totalPower+5), 1 / 64f, LerpedFloat.Chaser.EXP);
    }
}
