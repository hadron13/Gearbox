package io.github.hadron13.gearbox.item.tau_cannon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.potatoCannon.PotatoCannonItemRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModPartialModels;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class TauCannonRenderer extends CustomRenderedItemModelRenderer {

    public static LerpedFloat speed = LerpedFloat.linear();
    public static LerpedFloat recoil = LerpedFloat.linear();

    @Override
    protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext context, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
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
        ms.mulPose(Axis.ZP.rotationDegrees(angle));
        ms.translate(0, -offset, 0);
        renderer.render(ModPartialModels.TAU_CANNON_COIL.get(), light);
        ms.popPose();


    }
}
