package io.github.hadron13.gearbox.compat.kubejs;

import com.jozufozu.flywheel.util.Color;
import dev.latvian.mods.kubejs.util.ListJS;

public class EnergyRecipeJS extends ProcessingRecipeJS{

    @Override
    public void create(ListJS args) {
        super.create(args);
        json.addProperty("energy", 1.0);
    }

    public EnergyRecipeJS energy(int power){
        json.addProperty("energy", power);
        save();
        return this;
    }
}
