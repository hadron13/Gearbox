package io.github.hadron13.gearbox.register;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.item.tau_cannon.TauCannonItem;
import io.github.hadron13.gearbox.item.ultimate_mechanism.UltimateMechanismItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class GearboxItems {
    private static final CreateRegistrate REGISTRATE = Gearbox.registrate();

    public static void register() {}
    public static final ItemEntry<Item> GELD_INGOT = ingredient("geld_ingot"),
        SULFUR_DUST = ingredient("sulfur_dust"),
        SALT_DUST = ingredient("salt_dust"),
        CAUSTIC_SODA = ingredient("caustic_soda");

    public static final ItemEntry<Item> PET_COKE = REGISTRATE.item("petroleum_coke", Item::new)
            .burnTime(3200)
            .register();

    public static final ItemEntry<Item> CORE_TUBE=
            REGISTRATE.item("core_tube", Item::new)
                    .lang("Core Drill Tube")
                    .model(AssetLookup.itemModel("core_tube"))
                    .register();

    public static final ItemEntry<TauCannonItem> TAU_CANNON =
            REGISTRATE.item("tau_cannon", TauCannonItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .register();

    public static final ItemEntry<UltimateMechanismItem> ULTIMATE_MECHANISM =
            REGISTRATE.item("ultimate_mechanism", UltimateMechanismItem::new)
                    .model(AssetLookup.itemModelWithPartials())
                    .properties((p)->p.rarity(Rarity.EPIC))
                    .register();


    private static ItemEntry<Item> ingredient(String name) {
        return REGISTRATE.item(name, Item::new)
                .register();
    }

}
