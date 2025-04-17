package io.github.hadron13.gearbox.item.ultimate_mechanism;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;

public class UltimateMechanismRenderer extends CustomRenderedItemModelRenderer {

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext context, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        var stacker = TransformStack.of(ms);
        ms.translate(0, 1/16f, 0);

        renderer.render(model.getOriginalModel(), light);
        float worldTime = AnimationTickHolder.getRenderTime() / 10;

        stacker
                .scale(0.5f)
                .translate(-0.5f, -0.5f, -0.5f)
                .rotateCentered(new Quaternionf((float) Math.sin(worldTime), (float) Math.cos(worldTime), 0.0f, 1))
                .translate(0.5f, 0.5f, 0.5f);
        renderer.render(ModPartialModels.ULTIMATE_MECH_CORE.get(), light);


    }
}
