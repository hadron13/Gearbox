package io.github.hadron13.gearbox.compat.kubejs;

import com.jozufozu.flywheel.util.Color;
import dev.latvian.mods.kubejs.util.ListJS;

public class LaserRecipeJS extends ProcessingRecipeJS{
    @Override
    public void create(ListJS args) {
        super.create(args);
        json.addProperty("power", 1.0f);
    }
    public LaserRecipeJS color(int r, int g, int b){
        json.addProperty("color", new Color(r, g, b).getRGB());
        save();
        return this;
    }
    public LaserRecipeJS power(float power){
        json.addProperty("power", power);
        save();
        return this;
    }
}
