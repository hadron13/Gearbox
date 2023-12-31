package io.github.hadron13.gearbox.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

public class KubeJSGearboxPlugin extends KubeJSPlugin {
    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register("gearbox:pyroprocessing", ProcessingRecipeJS::new);
        event.register("gearbox:sapping", ProcessingRecipeJS::new);
        event.register("gearbox:compressing", ProcessingRecipeJS::new);
        event.register("gearbox:mechanizing", ProcessingRecipeJS::new);
    }
}