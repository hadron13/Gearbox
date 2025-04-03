package io.github.hadron13.gearbox.compat.kubejs;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import io.github.hadron13.gearbox.compat.kubejs.schemas.ProcessingRecipeSchema;
import io.github.hadron13.gearbox.register.ModRecipeTypes;

import java.util.HashMap;
import java.util.Map;

public class KubeJSGearboxPlugin extends KubeJSPlugin {

    private static final Map<ModRecipeTypes, RecipeSchema> recipeSchemas = new HashMap<>();

    static {
        recipeSchemas.put(ModRecipeTypes.PYROPROCESSING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(ModRecipeTypes.SAPPING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(ModRecipeTypes.COMPRESSING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(ModRecipeTypes.MECHANIZING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(ModRecipeTypes.TRANSMUTING, ProcessingRecipeSchema.LASER_RECIPE);
        recipeSchemas.put(ModRecipeTypes.IRRADIATING, ProcessingRecipeSchema.LASER_RECIPE);
        recipeSchemas.put(ModRecipeTypes.ELECTROLYZING, ProcessingRecipeSchema.PROCESSING_WITH_ENERGY);
        recipeSchemas.put(ModRecipeTypes.CENTRIFUGING, ProcessingRecipeSchema.PROCESSING_WITH_TIME);
        recipeSchemas.put(ModRecipeTypes.PUMPJACK, ProcessingRecipeSchema.PUMPJACK_RECIPE);
        recipeSchemas.put(ModRecipeTypes.LASER_DRILLING, ProcessingRecipeSchema.LASER_RECIPE);
        recipeSchemas.put(ModRecipeTypes.REACTING, ProcessingRecipeSchema.REACTING_RECIPE);
        recipeSchemas.put(ModRecipeTypes.DIPPING, ProcessingRecipeSchema.DIPPING_RECIPE);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        for (var createRecipeType : ModRecipeTypes.values()) {
            if (createRecipeType.getSerializer() instanceof ProcessingRecipeSerializer<?>) {
                var schema = recipeSchemas.getOrDefault(createRecipeType, ProcessingRecipeSchema.PROCESSING_DEFAULT);
                event.register(createRecipeType.getId(), schema);
            }
        }
    }
}