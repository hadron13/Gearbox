package io.github.hadron13.gearbox.item.tau_cannon;

import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;
import com.simibubi.create.content.equipment.zapper.ZapperBeamPacket;
import com.simibubi.create.content.equipment.zapper.ZapperRenderHandler;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class TauBeamPacket extends ZapperBeamPacket {

    public TauBeamPacket(Vec3 start, InteractionHand hand, boolean self, Vec3 target) {
        super(start, hand, self, target);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected ShootableGadgetRenderHandler getHandler() {
        return CreateClient.ZAPPER_RENDER_HANDLER;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void handleAdditional() {
//        CreateClient.ZAPPER_RENDER_HANDLER.addBeam(new ZapperRenderHandler.LaserBeam(location, target));
    }
}