package io.github.hadron13.gearbox.compat.kubejs;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import io.github.hadron13.gearbox.compat.kubejs.helpers.FluidIngredientHelper;

public class IrradiatingRecipeJS extends ProcessingRecipeJS{
    @Override
    public void create(ListJS args) {
        super.create(args);
        json.addProperty("power", 1.0f);
    }
    public IrradiatingRecipeJS color(int r, int g, int b){
        json.addProperty("color", new Color(r, g, b).getRGB());
        save();
        return this;
    }
    public IrradiatingRecipeJS power(float power){
        json.addProperty("power", power);
        save();
        return this;
    }
}
