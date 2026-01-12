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
        registry.register(ProcessingRecipeSchema.PYROPROCESSING_FACTORY);
        registry.register(ProcessingRecipeSchema.SAPPING_FACTORY);
        registry.register(ProcessingRecipeSchema.COMPRESSING_FACTORY);
        registry.register(ProcessingRecipeSchema.MECHANIZING_FACTORY);
        registry.register(ProcessingRecipeSchema.ELECTROLYZING_FACTORY);
        registry.register(ProcessingRecipeSchema.IRRADIATING_FACTORY);
        registry.register(ProcessingRecipeSchema.TRANSMUTING_FACTORY);
        registry.register(ProcessingRecipeSchema.PUMPJACK_FACTORY);
        registry.register(ProcessingRecipeSchema.DISTIL_FACTORY);
    }
    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(GearboxRecipeTypes.PYROPROCESSING.id, ProcessingRecipeSchema.PYROPROCESSING_SCHEMA);
        registry.register(GearboxRecipeTypes.SAPPING.id, ProcessingRecipeSchema.SAPPING_SCHEMA);
        registry.register(GearboxRecipeTypes.COMPRESSING.id, ProcessingRecipeSchema.COMPRESSING_SCHEMA);
        registry.register(GearboxRecipeTypes.MECHANIZING.id, ProcessingRecipeSchema.MECHANIZING_SCHEMA);
        registry.register(GearboxRecipeTypes.ELECTROLYZING.id, ProcessingRecipeSchema.ELECTROLYZING_SCHEMA);
        registry.register(GearboxRecipeTypes.IRRADIATING.id, ProcessingRecipeSchema.IRRADIATING_SCHEMA);
        registry.register(GearboxRecipeTypes.TRANSMUTING.id, ProcessingRecipeSchema.TRANSMUTING_SCHEMA);
        registry.register(GearboxRecipeTypes.PUMPJACK.id, ProcessingRecipeSchema.PUMPJACK_SCHEMA);
        registry.register(GearboxRecipeTypes.DISTILLING.id, ProcessingRecipeSchema.DISTILLATION_SCHEMA);
    }
}