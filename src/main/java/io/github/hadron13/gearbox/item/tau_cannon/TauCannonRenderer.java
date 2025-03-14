package io.github.hadron13.gearbox.item.tau_cannon;

import com.jozufozu.flywheel.core.PartialModel;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonItem;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;

public class TauCannonRenderer extends CustomRenderedItemModelRenderer {

    public static LerpedFloat speed = LerpedFloat.linear();
    public static LerpedFloat recoil = LerpedFloat.linear();

    protected static final PartialModel COIL = new PartialModel(Gearbox.asResource("item/tau_cannon/coil"));
    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemTransforms.TransformType transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance()
                .getItemRenderer();

        TauCannonAnimationHolder.tick();
        float recoil = TauCannonAnimationHolder.recoil.getValue();
        if(recoil > 0) {
            ms.translate(recoil, 0, 0);
        }

        renderer.render(model.getOriginalModel(), light);

        LocalPlayer player = Minecraft.getInstance().player;
        boolean mainHand = player.getMainHandItem() == stack;
        boolean offHand = player.getOffhandItem() == stack;
        boolean leftHanded = player.getMainArm() == HumanoidArm.LEFT;

        float offset = 1f / 16f;
        float worldTime = AnimationTickHolder.getRenderTime() / 10;
        float angle = worldTime * -25;
        float speed = CreateClient.POTATO_CANNON_RENDER_HANDLER.getAnimation(mainHand ^ leftHanded,
                AnimationTickHolder.getPartialTicks());



        if (mainHand || offHand)
            angle += 360 * Mth.clamp(speed * 5, 0, 1);
        angle %= 360;

        ms.pushPose();
        ms.translate(0, offset, 0);
        ms.mulPose(Vector3f.ZP.rotationDegrees(angle));
        ms.translate(0, -offset, 0);
        renderer.render(COIL.get(), light);
        ms.popPose();


    }
}
