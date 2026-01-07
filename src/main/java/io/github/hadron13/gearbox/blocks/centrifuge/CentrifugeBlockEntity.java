package io.github.hadron13.gearbox.blocks.centrifuge;

import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.TooltipHelper;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;


import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock.AXIS;
import static net.minecraft.ChatFormatting.GOLD;

public class CentrifugeBlockEntity extends KineticBlockEntity {

    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    protected IFluidHandler fluidCapability;
    public CentrifugingRecipe lastRecipe = null;
    int recipeTimer = 0;

    public CentrifugeBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }


    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                GearboxBlockEntities.CENTRIFUGE.get(),
                (be, context) -> {
                    if (context == null || context.getAxis() == be.getBlockState().getValue(AXIS)){
                        return be.fluidCapability;
                    }
                    return null;
                }
        );
    }

    @Override
    public void tick(){
        super.tick();
        if(Mth.abs(speed) < 32.0f || inputTank.isEmpty()) {
            recipeTimer = 0;
            return;
        }

        if(recipeTimer > 0){
            recipeTimer -= getProcessingSpeed();

            if(recipeTimer <= 0){
                if(lastRecipe == null)
                    return;
                CentrifugingRecipe.apply(this, lastRecipe, false);
            }
            return;
        }


        if (lastRecipe == null || !CentrifugingRecipe.match(this, lastRecipe)) {
            Optional<CentrifugingRecipe> recipe = GearboxRecipeTypes.CENTRIFUGING.find(this, level);
            if (!recipe.isPresent()) {
                recipeTimer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get();
                recipeTimer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }
        recipeTimer = lastRecipe.getProcessingDuration();

    }


    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition).inflate(1);
    }

    @Override
    public List<BlockPos> addPropagationLocations(IRotate block, BlockState state, List<BlockPos> neighbours) {
        if (!ICogWheel.isLargeCog(state))
            return super.addPropagationLocations(block, state, neighbours);

        BlockPos.betweenClosedStream(new BlockPos(-1, -1, -1), new BlockPos(1, 1, 1))
                .forEach(offset -> {
                    if (offset.distSqr(BlockPos.ZERO) == 2)
                        neighbours.add(worldPosition.offset(offset));
                });
        return neighbours;
    }

    public void addBehaviours(List<BlockEntityBehaviour> behaviours){
        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 1, 1000, true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 6, 1000, true);

        inputTank.forbidExtraction();
        outputTank.forbidInsertion();

        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = new CombinedTankWrapper(outputTank.getCapability(), inputTank.getCapability());
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean isEmpty = !super.addToGoggleTooltip(tooltip, isPlayerSneaking);


        if (Math.abs(getSpeed()) < 64.0f) {
            GearboxLang.translate("tooltip.speedRequirement")
                    .style(GOLD)
                    .forGoggles(tooltip);
            MutableComponent hint =
                    GearboxLang.translateDirect("gui.contraptions.not_fast_enough", I18n.get(getBlockState().getBlock()
                            .getDescriptionId()));
            List<Component> cutString = TooltipHelper.cutTextComponent(hint, FontHelper.Palette.GRAY_AND_WHITE);
            for (int i = 0; i < cutString.size(); i++)
                GearboxLang.builder()
                        .add(cutString.get(i)
                                .copy())
                        .forGoggles(tooltip);
            isEmpty = false;
        }

        containedFluidTooltip(tooltip, isPlayerSneaking, fluidCapability);

        return !isEmpty;
    }




}
