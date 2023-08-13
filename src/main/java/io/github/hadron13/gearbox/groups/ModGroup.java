package io.github.hadron13.gearbox.groups;

import com.simibubi.create.AllBlocks;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModGroup extends CreativeModeTab {
    public static ModGroup MAIN;

    public ModGroup(String name) {
        super(Gearbox.MODID+":"+name);
        MAIN = this;
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(AllBlocks.GEARBOX.get());
    }
}