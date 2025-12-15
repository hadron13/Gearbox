package io.github.hadron13.gearbox.blocks.steel_tank;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.animation.LerpedFloat;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static net.createmod.catnip.platform.ForgeCatnipServices.FLUID_RENDERER;

public class SteelFluidTankRenderer extends SafeBlockEntityRenderer<SteelTankBlockEntity> {

    public SteelFluidTankRenderer(BlockEntityRendererProvider.Context context) {}
    @Override
    protected void renderSafe(SteelTankBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        if (!te.isController())
            return;
        if (!te.hasWindows()) {
            return;
        }
        LerpedFloat fluidLevel = te.getFluidLevel();
        if (fluidLevel == null)
            return;

        float capHeight = 1 / 4f;
        float tankHullWidth = 1 / 16f + 1 / 128f;
        float minPuddleHeight = 1 / 16f;
        float totalHeight = te.getHeight() - 2 * capHeight - minPuddleHeight;

        float level = fluidLevel.getValue(partialTicks);
        if (level < 1 / (512f * totalHeight))
            return;
        float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);

        FluidTank tank = te.getTankInventory();
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack.isEmpty())
            return;
        boolean top = fluidStack.getFluid()
                .getFluidType()
                .isLighterThanAir();

        float xMin = tankHullWidth;
        float xMax = xMin + te.getWidth() - 2 * tankHullWidth;
        float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
        float yMax = yMin + clampedLevel;

        if (top) {
            yMin += totalHeight - clampedLevel;
            yMax += totalHeight - clampedLevel;
        }

        float zMin = tankHullWidth;
        float zMax = zMin + te.getWidth() - 2 * tankHullWidth;

        ms.pushPose();
        ms.translate(0, clampedLevel - totalHeight, 0);
        FLUID_RENDERER.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false,true);

        ms.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(SteelTankBlockEntity te) {
        return te.isController();
    }
}
