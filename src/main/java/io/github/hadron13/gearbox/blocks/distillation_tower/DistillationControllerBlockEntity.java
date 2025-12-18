package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.millstone.MillstoneBlockEntity;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlock;
import com.simibubi.create.content.kinetics.steamEngine.SteamEngineBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.INamedIconOptions;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollOptionBehaviour;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.CreateLang;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.register.GearboxIcons;
import net.createmod.catnip.lang.Lang;
import net.createmod.catnip.lang.LangBuilder;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class DistillationControllerBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    public ScrollOptionBehaviour<DistilMode> distilMode;
    public SmartFluidTankBehaviour inputTank;
    public SmartFluidTankBehaviour outputTank;
    public DistillingRecipe currentRecipe;

    public boolean contentsChanged;

    protected LazyOptional<IFluidHandler> fluidCapability;

    public DistillationControllerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }



    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        distilMode = new ScrollOptionBehaviour<>(DistilMode.class,
                GearboxLang.translate("gui.distil_mode").component(), this, new DistilModeBoxTransform());
        behaviours.add(distilMode);


        inputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this, 2, 4000, true)
                .whenFluidUpdates(() -> contentsChanged = true);
        outputTank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.OUTPUT, this, 8, 4000, true)
                .forbidInsertion();
        behaviours.add(inputTank);
        behaviours.add(outputTank);

        fluidCapability = inputTank.getCapability().cast();
    }


    @Override
    public void tick() {
        super.tick();



    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        CreateLang.translate("gui.gauge.info_header").forGoggles(tooltip);

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        for (int i = 0; i < inputTank.getPrimaryHandler().getTanks(); i++) {
            FluidStack fluidStack = inputTank.getPrimaryHandler().getFluidInTank(i);
            if (fluidStack.isEmpty())
                continue;
            CreateLang.text("")
                    .add(CreateLang.fluidName(fluidStack)
                            .add(CreateLang.text(" "))
                            .style(ChatFormatting.GRAY)
                            .add(CreateLang.number(fluidStack.getAmount())
                                    .add(mb)
                                    .style(ChatFormatting.BLUE)))
                    .forGoggles(tooltip, 1);
        }

        GearboxLang.text("").forGoggles(tooltip);
        GearboxLang.translate("gui.distil_mode").text(":").forGoggles(tooltip);
        GearboxLang.translate("gui.distil_mode." + distilMode.get().toString().toLowerCase())
                .style(ChatFormatting.GRAY)
                .forGoggles(tooltip, 1);

        return true;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side.getAxis() == DistillationControllerBlock.getAxis(getBlockState()) )){
            return fluidCapability.cast();
        }
        return super.getCapability(cap, side);
    }

    private class DistilModeBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8f, 8f, 16f);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return DistillationControllerBlock.shouldRenderHeadOnFaceStatic(state, direction);
        }
    }



    public static enum DistilMode implements INamedIconOptions {
        DISTIL_FLASH(GearboxIcons.DISTIL_FLASH),
        DISTIL_ATMOSPHERIC(GearboxIcons.DISTIL_ATMOSPHERIC),
        DISTIL_VACUUM(GearboxIcons.DISTIL_VACUUM),
        ;

        private String translationKey;
        private GearboxIcons icon;

        private DistilMode(GearboxIcons icon) {
            this.icon = icon;
            translationKey = "gearbox.gui.distil_mode." + Lang.asId(name());
        }

        @Override
        public AllIcons getIcon() {
            return icon;
        }

        @Override
        public String getTranslationKey() {
            return translationKey;
        }
    }
}
