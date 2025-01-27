package io.github.hadron13.gearbox.compat.kubejs;

import dev.latvian.mods.kubejs.util.ListJS;

public class PumpjackRecipeJS extends ProcessingRecipeJS{
    @Override
    public void create(ListJS args) {
        super.create(args);
    }

    public void biome(String id){
        json.addProperty("biome", id);
    }

}
