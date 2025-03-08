package io.github.hadron13.gearbox.compat.kubejs;

import com.jozufozu.flywheel.util.Color;
import dev.latvian.mods.kubejs.util.ListJS;

public class ReactingRecipeJS extends ProcessingRecipeJS{
    @Override
    public void create(ListJS args) {
        super.create(args);
    }
    public ReactingRecipeJS atmosphere(String name) {
        json.addProperty("atmosphere", name);
        save();
        return this;
    }
    public ReactingRecipeJS rpm_max(int max) {
        json.addProperty("rpm_max", max);
        save();
        return this;
    }
    public ReactingRecipeJS rpm_min(int min) {
        json.addProperty("rpm_min", min);
        save();
        return this;
    }
}
