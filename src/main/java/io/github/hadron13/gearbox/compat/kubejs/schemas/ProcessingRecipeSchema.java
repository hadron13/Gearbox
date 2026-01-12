package io.github.hadron13.gearbox.compat.kubejs.schemas;

import com.mojang.datafixers.util.Either;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import dev.latvian.mods.kubejs.create.recipe.CreateRecipeComponents;
import dev.latvian.mods.kubejs.create.recipe.ProcessingOutputRecipeComponent;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public interface ProcessingRecipeSchema {

    RecipeKey<List<Either<SizedFluidIngredient, SizedIngredient>>> INGREDIENTS = CreateRecipeComponents.SIZED_FLUID_INGREDIENT.instance().or(SizedIngredientComponent.FLAT.instance()).asList().inputKey("ingredients");
    RecipeKey<List<Either<FluidStack, ProcessingOutput>>> RESULTS = FluidStackComponent.FLUID_STACK.instance().or(ProcessingOutputRecipeComponent.TYPE.instance()).asList().outputKey("results");
    RecipeKey<Long> PROCESSING_TIME = NumberComponent.LONG.otherKey("processing_time").optional(100L);
    RecipeKey<Long> PROCESSING_TIME_REQUIRED = NumberComponent.LONG.otherKey("processing_time").optional(100L).alwaysWrite();
    RecipeKey<HeatCondition> HEAT_REQUIREMENT = CreateRecipeComponents.HEAT_CONDITION.otherKey("heat_requirement").defaultOptional();

    RecipeKey<Integer> ENERGY = NumberComponent.INT.inputKey("energy").optional(50).alwaysWrite();

    RecipeKey<Integer> COLOR = NumberComponent.INT.inputKey("color").alwaysWrite();
    RecipeKey<Float> POWER = NumberComponent.FLOAT.inputKey("power").optional(1f).alwaysWrite();

    RecipeKey<String> BIOME = StringComponent.ID.inputKey("biome").alwaysWrite();
    RecipeKey<String> DISTIL_MODE = StringComponent.STRING.inputKey("mode").alwaysWrite();

    public static final KubeRecipeFactory PYROPROCESSING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.PYROPROCESSING.id, TimedProcessingRecipeKube.class, TimedProcessingRecipeKube::new);
    public static final KubeRecipeFactory SAPPING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.SAPPING.id, ProcessingRecipeKube.class, ProcessingRecipeKube::new);
    public static final KubeRecipeFactory COMPRESSING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.COMPRESSING.id, TimedProcessingRecipeKube.class, TimedProcessingRecipeKube::new);
    public static final KubeRecipeFactory MECHANIZING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.MECHANIZING.id, ProcessingRecipeKube.class, ProcessingRecipeKube::new);
    public static final KubeRecipeFactory ELECTROLYZING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.ELECTROLYZING.id, EnergyProcessingRecipeKube.class, EnergyProcessingRecipeKube::new);
    public static final KubeRecipeFactory IRRADIATING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.IRRADIATING.id, LaserProcessingRecipeKube.class, LaserProcessingRecipeKube::new);
    public static final KubeRecipeFactory TRANSMUTING_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.TRANSMUTING.id, LaserProcessingRecipeKube.class, LaserProcessingRecipeKube::new);
    public static final KubeRecipeFactory PUMPJACK_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.TRANSMUTING.id, ProcessingRecipeKube.class, ProcessingRecipeKube::new);
    public static final KubeRecipeFactory DISTIL_FACTORY = new KubeRecipeFactory(GearboxRecipeTypes.TRANSMUTING.id, TimedProcessingRecipeKube.class, TimedProcessingRecipeKube::new);


    RecipeSchema PYROPROCESSING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED).factory(PYROPROCESSING_FACTORY);
    RecipeSchema SAPPING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS).factory(SAPPING_FACTORY);
    RecipeSchema COMPRESSING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, PROCESSING_TIME_REQUIRED, HEAT_REQUIREMENT).factory(COMPRESSING_FACTORY);
    RecipeSchema MECHANIZING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS).factory(MECHANIZING_FACTORY);
    RecipeSchema ELECTROLYZING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, HEAT_REQUIREMENT, PROCESSING_TIME, ENERGY).factory(ELECTROLYZING_FACTORY);
    RecipeSchema IRRADIATING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, COLOR, POWER, PROCESSING_TIME_REQUIRED).factory(IRRADIATING_FACTORY);
    RecipeSchema TRANSMUTING_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, COLOR, POWER, PROCESSING_TIME_REQUIRED).factory(TRANSMUTING_FACTORY);
    RecipeSchema PUMPJACK_SCHEMA = new RecipeSchema(RESULTS, BIOME).factory(PUMPJACK_FACTORY);
    RecipeSchema DISTILLATION_SCHEMA = new RecipeSchema(RESULTS, INGREDIENTS, DISTIL_MODE, PROCESSING_TIME_REQUIRED).factory(DISTIL_FACTORY);


    public class ProcessingRecipeKube extends KubeRecipe {
        public KubeRecipe heated() {
            this.setValue(HEAT_REQUIREMENT, HeatCondition.HEATED);
            save();
            return this;
        }
        public KubeRecipe superheated() {
            this.setValue(HEAT_REQUIREMENT, HeatCondition.SUPERHEATED);
            save();
            return this;
        }
        public KubeRecipe processingTime(long time) {
            this.setValue(PROCESSING_TIME, time);
            save();
            return this;
        }
    }

    public class TimedProcessingRecipeKube extends ProcessingRecipeKube{
        @Override
        public KubeRecipe processingTime(long time) {
            this.setValue(PROCESSING_TIME_REQUIRED, time);
            save();
            return this;
        }
    }
    public class LaserProcessingRecipeKube extends TimedProcessingRecipeKube{
        public KubeRecipe power(float power) {
            this.setValue(POWER, power);
            save();
            return this;
        }
    }

    public class EnergyProcessingRecipeKube extends ProcessingRecipeKube{
        public KubeRecipe energy(int energy){
            this.setValue(ENERGY, energy);
            save();
            return this;
        }
    }
}
