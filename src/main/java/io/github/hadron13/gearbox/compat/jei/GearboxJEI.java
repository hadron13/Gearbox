package io.github.hadron13.gearbox.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillingRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import io.github.hadron13.gearbox.compat.jei.category.*;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import io.github.hadron13.gearbox.register.ModBlocks;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.config.ConfigBase;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class GearboxJEI implements IModPlugin {

    private static final ResourceLocation ID = Gearbox.asResource("jei_plugin");

    public IIngredientManager ingredientManager;
    private final List<CreateRecipeCategory<?>> modCategories = new ArrayList<>();


    private void loadCategories() {

        this.modCategories.clear();
        CreateRecipeCategory<?>
                pyroprocessing = builder(PyroprocessingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.PYROPROCESSING)
                .catalyst(ModBlocks.KILN::get)
                .itemIcon(ModBlocks.KILN.get())
                .emptyBackground(177, 75)
                .build("pyroprocessing", PyroprocessingCategory::new);

        CreateRecipeCategory<?>
                sapping = builder(SappingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.SAPPING)
                .catalyst(ModBlocks.SAPPER::get)
                .itemIcon(ModBlocks.SAPPER.get())
                .emptyBackground(177, 103)
                .build("sapping", SappingCategory::new);

        CreateRecipeCategory<?>
                compressing = builder(CompressingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.COMPRESSING)
                .catalyst(ModBlocks.COMPRESSOR::get)
                .catalyst(AllBlocks.BLAZE_BURNER::get)
                .itemIcon(ModBlocks.COMPRESSOR.get())
                .emptyBackground(177,75)
                .build("compressing", CompressingCategory::new);

        CreateRecipeCategory<?>
                mechanizing = builder(MechanizingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.MECHANIZING)
                .catalyst(ModBlocks.BRASS_PRESS::get)
                .itemIcon(ModBlocks.BRASS_PRESS.get())
                .emptyBackground(177, 75)
                .build("mechanizing", MechanizingCategory::new);

        CreateRecipeCategory<?>
                transmuting = builder(TransmutingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.TRANSMUTING)
                .catalyst(ModBlocks.IRRADIATOR::get)
                .catalyst(AllBlocks.DEPOT::get)
                .catalyst(AllBlocks.BELT::get)
                .doubleItemIcon(ModBlocks.IRRADIATOR.get(), AllBlocks.DEPOT.get())
                .emptyBackground(177, 75)
                .build("transmuting", TransmutingCategory::new);

        CreateRecipeCategory<?>
                irradiating = builder(IrradiatingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.IRRADIATING)
                .catalyst(ModBlocks.IRRADIATOR::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(ModBlocks.IRRADIATOR.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 75)
                .build("irradiating", IrradiatingCategory::new);

        CreateRecipeCategory<?>
                electrolyzing = builder(BasinRecipe.class)
                .addTypedRecipes(ModRecipeTypes.ELECTROLYZING)
                .catalyst(ModBlocks.ELECTROLYZER::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(ModBlocks.ELECTROLYZER.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 103)
                .build("electrolyzing", ElectrolyzingCategory::new);


        CreateRecipeCategory<?>
                centrifuging = builder(CentrifugingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.CENTRIFUGING)
                .catalyst(ModBlocks.CENTRIFUGE::get)
                .itemIcon(ModBlocks.CENTRIFUGE.get())
                .emptyBackground(177, 93)
                .build("centrifuging", CentrifugingCategory::new);

        CreateRecipeCategory<?>
                pumpjack = builder(PumpjackRecipe.class)
                .addTypedRecipes(ModRecipeTypes.PUMPJACK)
                .catalyst(ModBlocks.PUMPJACK_WELL::get)
                .catalyst(ModBlocks.PUMPJACK_ARM::get)
                .catalyst(ModBlocks.PUMPJACK_CRANK::get)
                .itemIcon(ModBlocks.PUMPJACK_WELL.get())
                .emptyBackground(177, 65)
                .build("pumpjack", PumpjackCategory::new);

        CreateRecipeCategory<?>
                laser_drilling = builder(LaserDrillingRecipe.class)
                .addTypedRecipes(ModRecipeTypes.LASER_DRILLING)
                .catalyst(ModBlocks.LASER_DRILL::get)
                .itemIcon(ModBlocks.LASER_DRILL.get())
                .emptyBackground(177, 75)
                .build("laser_drilling", LaserDrillingCategory::new);

        CreateRecipeCategory<?>
                reacting= builder(BasinRecipe.class)
                .addTypedRecipes(ModRecipeTypes.REACTING)
                .catalyst(ModBlocks.REACTOR::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(ModBlocks.REACTOR.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 103)
                .build("reacting", ReactingCategory::new);
    }
    private <T extends Recipe<?>> CategoryBuilder<T> builder(Class<? extends T> recipeClass) {
        return new CategoryBuilder<>(recipeClass);
    }

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories();
        registration.addRecipeCategories(modCategories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ingredientManager = registration.getIngredientManager();

        modCategories.forEach(c -> c.registerRecipes(registration));

    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        modCategories.forEach(c -> c.registerCatalysts(registration));
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BlueprintTransferHandler(), RecipeTypes.CRAFTING);
    }



    private class CategoryBuilder<T extends Recipe<?>> {
        private final Class<? extends T> recipeClass;
        private Predicate<CRecipes> predicate = cRecipes -> true;

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<T>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CategoryBuilder<T> enableIf(Predicate<CRecipes> predicate) {
            this.predicate = predicate;
            return this;
        }

        public CategoryBuilder<T> enableWhen(Function<CRecipes, ConfigBase.ConfigBool> configValue) {
            predicate = c -> configValue.apply(c).get();
            return this;
        }

        public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<T>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public CategoryBuilder<T> addRecipes(Supplier<Collection<? extends T>> collection) {
            return addRecipeListConsumer(recipes -> recipes.addAll(collection.get()));
        }

        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add((T) recipe);
                }
            }));
        }

        public CategoryBuilder<T> addAllRecipesIf(Predicate<Recipe<?>> pred, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> consumeAllRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(converter.apply(recipe));
                }
            }));
        }

        public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipes::add, recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipes(Supplier<RecipeType<? extends T>> recipeType, Function<Recipe<?>, T> converter) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> recipes.add(converter.apply(recipe)), recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipesIf(Supplier<RecipeType<? extends T>> recipeType, Predicate<Recipe<?>> pred) {
            return addRecipeListConsumer(recipes -> CreateJEI.<T>consumeTypedRecipes(recipe -> {
                if (pred.test(recipe)) {
                    recipes.add(recipe);
                }
            }, recipeType.get()));
        }

        public CategoryBuilder<T> addTypedRecipesExcluding(Supplier<RecipeType<? extends T>> recipeType,
                                                           Supplier<RecipeType<? extends T>> excluded) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(excluded.get());
                CreateJEI.<T>consumeTypedRecipes(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                            return;
                        }
                    }
                    recipes.add(recipe);
                }, recipeType.get());
            });
        }

        public CategoryBuilder<T> removeRecipes(Supplier<RecipeType<? extends T>> recipeType) {
            return addRecipeListConsumer(recipes -> {
                List<Recipe<?>> excludedRecipes = CreateJEI.getTypedRecipes(recipeType.get());
                recipes.removeIf(recipe -> {
                    for (Recipe<?> excludedRecipe : excludedRecipes) {
                        if (CreateJEI.doInputsMatch(recipe, excludedRecipe)) {
                            return true;
                        }
                    }
                    return false;
                });
            });
        }

        public CategoryBuilder<T> catalystStack(Supplier<ItemStack> supplier) {
            catalysts.add(supplier);
            return this;
        }

        public CategoryBuilder<T> catalyst(Supplier<ItemLike> supplier) {
            return catalystStack(() -> new ItemStack(supplier.get()
                    .asItem()));
        }

        public CategoryBuilder<T> icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        public CategoryBuilder<T> itemIcon(ItemLike item) {
            icon(new ItemIcon(() -> new ItemStack(item)));
            return this;
        }

        public CategoryBuilder<T> doubleItemIcon(ItemLike item1, ItemLike item2) {
            icon(new DoubleItemIcon(() -> new ItemStack(item1), () -> new ItemStack(item2)));
            return this;
        }

        public CategoryBuilder<T> background(IDrawable background) {
            this.background = background;
            return this;
        }

        public CategoryBuilder<T> emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        public CreateRecipeCategory<T> build(String name, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<T>> recipesSupplier;
            if (predicate.test(AllConfigs.server().recipes)) {
                recipesSupplier = () -> {
                    List<T> recipes = new ArrayList<>();
                    for (Consumer<List<T>> consumer : recipeListConsumers)
                        consumer.accept(recipes);
                    return recipes;
                };
            } else {
                recipesSupplier = () -> Collections.emptyList();
            }

            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                    new mezz.jei.api.recipe.RecipeType<>(Gearbox.asResource(name), recipeClass),
                    Lang.translateDirect("recipe." + name), background, icon, recipesSupplier, catalysts);
            CreateRecipeCategory<T> category = factory.create(info);
            modCategories.add(category);
            return category;
        }
    }

    public static void consumeAllRecipes(Consumer<Recipe<?>> consumer) {
        Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }






}
