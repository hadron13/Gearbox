package io.github.hadron13.gearbox.compat.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeFactoryRegistry;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.compat.kubejs.schemas.ProcessingRecipeSchema;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;

import java.util.HashMap;
import java.util.Map;

public class KubeJSGearboxPlugin implements KubeJSPlugin {

    private static final Map<GearboxRecipeTypes, RecipeSchema> recipeSchemas = new HashMap<>();

    @Override
    public void registerRecipeFactories(RecipeFactoryRegistry registry) {
        registry.register(ProcessingRecipeSchema.TimedProcessingRecipeKube.PYROPROCESSING_FACTORY);
        registry.register(ProcessingRecipeSchema.TimedProcessingRecipeKube.COMPRESSING_FACTORY);
    }
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(GearboxRecipeTypes.PYROPROCESSING.id, ProcessingRecipeSchema.PYROPROCESSING_SCHEMA);
        registry.register(GearboxRecipeTypes.COMPRESSING.id, ProcessingRecipeSchema.COMPRESSING_SCHEMA);
    }
}