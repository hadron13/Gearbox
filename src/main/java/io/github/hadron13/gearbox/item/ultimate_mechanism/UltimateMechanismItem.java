package io.github.hadron13.gearbox.item.ultimate_mechanism;

import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import io.github.hadron13.gearbox.item.tau_cannon.TauCannonRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import java.util.function.Consumer;

public class UltimateMechanismItem extends Item {
    public UltimateMechanismItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(SimpleCustomRenderer.create(this, new UltimateMechanismRenderer()));
    }
}
