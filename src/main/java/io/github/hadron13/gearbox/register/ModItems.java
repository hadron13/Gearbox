package io.github.hadron13.gearbox.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.groups.ModGroup;
import io.github.hadron13.gearbox.item.tau_cannon.TauCannonItem;
import net.minecraft.world.item.Item;

public class ModItems {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate()
            .creativeModeTab(() -> ModGroup.MAIN);

    public static void register() {}
    public static final ItemEntry<Item> GELD_INGOT = ingredient("geld_ingot"),
                                        DIAMOND_TUBE = ingredient("diamond_tube");

    public static final ItemEntry<TauCannonItem> TAU_CANNON =
            REGISTRATE.item("tau_cannon", TauCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

}
