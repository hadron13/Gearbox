package io.github.hadron13.gearbox.item.ultimate_mechanism;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.item.ItemStack;

public class UltimateMechanismRenderer extends CustomRenderedItemModelRenderer {

    protected static final PartialModel CORE = new PartialModel(Gearbox.asResource("item/ultimate_mechanism/core"));
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {

        ms.translate(0, 1/16f, 0);

        renderer.render(model.getOriginalModel(), light);
        float worldTime = AnimationTickHolder.getRenderTime() / 10;

        TransformStack.cast(ms)
                .translate(-0.5f, -0.5f, -0.5f)
                .rotateCentered(Quaternion.fromXYZ((float) Math.sin(worldTime), (float) Math.cos(worldTime), 0))
                .translate(0.5f, 0.5f, 0.5f);
        renderer.render(CORE.get(), light);


    }
}
