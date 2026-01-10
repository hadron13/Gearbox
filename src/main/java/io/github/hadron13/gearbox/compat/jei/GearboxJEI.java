package io.github.hadron13.gearbox.compat.jei;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import io.github.hadron13.gearbox.compat.jei.category.*;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import net.createmod.catnip.config.ConfigBase;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static mezz.jei.api.recipe.RecipeType.createRecipeHolderType;

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
                .addTypedRecipes(GearboxRecipeTypes.PYROPROCESSING)
                .catalyst(GearboxBlocks.KILN::get)
                .itemIcon(GearboxBlocks.KILN.get())
                .emptyBackground(177, 75)
                .build("pyroprocessing", PyroprocessingCategory::new);

        CreateRecipeCategory<?>
                sapping = builder(SappingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.SAPPING)
                .catalyst(GearboxBlocks.SAPPER::get)
                .itemIcon(GearboxBlocks.SAPPER.get())
                .emptyBackground(177, 103)
                .build("sapping", SappingCategory::new);

        CreateRecipeCategory<?>
                compressing = builder(CompressingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.COMPRESSING)
                .catalyst(GearboxBlocks.COMPRESSOR::get)
                .catalyst(AllBlocks.BLAZE_BURNER::get)
                .itemIcon(GearboxBlocks.COMPRESSOR.get())
                .emptyBackground(177,75)
                .build("compressing", CompressingCategory::new);

        CreateRecipeCategory<?>
                mechanizing = builder(MechanizingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.MECHANIZING)
                .catalyst(GearboxBlocks.BRASS_PRESS::get)
                .itemIcon(GearboxBlocks.BRASS_PRESS.get())
                .emptyBackground(177, 75)
                .build("mechanizing", MechanizingCategory::new);

        CreateRecipeCategory<?>
                transmuting = builder(TransmutingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.TRANSMUTING)
                .catalyst(GearboxBlocks.IRRADIATOR::get)
                .catalyst(AllBlocks.DEPOT::get)
                .catalyst(AllBlocks.BELT::get)
                .doubleItemIcon(GearboxBlocks.IRRADIATOR.get(), AllBlocks.DEPOT.get())
                .emptyBackground(177, 75)
                .build("transmuting", TransmutingCategory::new);

        CreateRecipeCategory<?>
                irradiating = builder(IrradiatingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.IRRADIATING)
                .catalyst(GearboxBlocks.IRRADIATOR::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(GearboxBlocks.IRRADIATOR.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 75)
                .build("irradiating", IrradiatingCategory::new);

        CreateRecipeCategory<?>
                electrolyzing = builder(ElectrolyzingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.ELECTROLYZING)
                .catalyst(GearboxBlocks.ELECTROLYZER::get)
                .catalyst(AllBlocks.BASIN::get)
                .doubleItemIcon(GearboxBlocks.ELECTROLYZER.get(), AllBlocks.BASIN.get())
                .emptyBackground(177, 103)
                .build("electrolyzing", ElectrolyzingCategory::new);


        CreateRecipeCategory<?>
                centrifuging = builder(CentrifugingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.CENTRIFUGING)
                .catalyst(GearboxBlocks.CENTRIFUGE::get)
                .itemIcon(GearboxBlocks.CENTRIFUGE.get())
                .emptyBackground(177, 93)
                .build("centrifuging", CentrifugingCategory::new);

        CreateRecipeCategory<?>
                pumpjack = builder(PumpjackRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.PUMPJACK)
                .catalyst(GearboxBlocks.PUMPJACK_WELL::get)
                .catalyst(GearboxBlocks.PUMPJACK_ARM::get)
                .catalyst(GearboxBlocks.PUMPJACK_CRANK::get)
                .itemIcon(GearboxBlocks.PUMPJACK_WELL.get())
                .emptyBackground(177, 65)
                .build("pumpjack", PumpjackCategory::new);

//        CreateRecipeCategory<?>
//                laser_drilling = builder(LaserDrillingRecipe.class)
//                .addTypedRecipes(GearboxRecipeTypes.LASER_DRILLING)
//                .catalyst(GearboxBlocks.LASER_DRILL::get)
//                .itemIcon(GearboxBlocks.LASER_DRILL.get())
//                .emptyBackground(177, 75)
//                .build("laser_drilling", LaserDrillingCategory::new);

        CreateRecipeCategory<?>
                distilling = builder(DistillingRecipe.class)
                .addTypedRecipes(GearboxRecipeTypes.DISTILLING)
                .catalyst(GearboxBlocks.DISTILLATION_CONTROLLER::get)
                .catalyst(GearboxBlocks.DISTILLATION_OUTPUT::get)
                .catalyst(GearboxBlocks.STEEL_FLUID_TANK::get)
                .itemIcon(GearboxBlocks.DISTILLATION_CONTROLLER.get())
                .emptyBackground(177, 165)
                .build("distilling", DistillingCategory::new);

//        CreateRecipeCategory<?>
//                reacting= builder(BasinRecipe.class)
//                .addTypedRecipes(GearboxRecipeTypes.REACTING)
//                .catalyst(GearboxBlocks.REACTOR::get)
//                .catalyst(AllBlocks.BASIN::get)
//                .doubleItemIcon(GearboxBlocks.REACTOR.get(), AllBlocks.BASIN.get())
//                .emptyBackground(177, 103)
//                .build("reacting", ReactingCategory::new);
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

        private IDrawable background;
        private IDrawable icon;

        private final List<Consumer<List<RecipeHolder<T>>>> recipeListConsumers = new ArrayList<>();
        private final List<Supplier<? extends ItemStack>> catalysts = new ArrayList<>();

        public CategoryBuilder(Class<? extends T> recipeClass) {
            this.recipeClass = recipeClass;
        }

        public CategoryBuilder<T> addRecipeListConsumer(Consumer<List<RecipeHolder<T>>> consumer) {
            recipeListConsumers.add(consumer);
            return this;
        }

        public CategoryBuilder<T> addTypedRecipes(IRecipeTypeInfo recipeTypeEntry) {
            return addTypedRecipes(recipeTypeEntry::getType);
        }

        public <I extends RecipeInput, R extends Recipe<I>> CategoryBuilder<T> addTypedRecipes(Supplier<net.minecraft.world.item.crafting.RecipeType<R>> recipeType) {
            return addRecipeListConsumer(recipes -> GearboxJEI.<T>consumeTypedRecipes(recipe -> {
                if (recipeClass.isInstance(recipe.value()))
                    //noinspection unchecked - checked by if statement above
                    recipes.add((RecipeHolder<T>) recipe);
            }, recipeType.get()));
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

        public CreateRecipeCategory<T> build(String id, CreateRecipeCategory.Factory<T> factory) {
            Supplier<List<RecipeHolder<T>>> recipesSupplier;
            recipesSupplier = () -> {
                List<RecipeHolder<T>> recipes = new ArrayList<>();
                for (Consumer<List<RecipeHolder<T>>> consumer : recipeListConsumers) {consumer.accept(recipes);}
                return recipes;
            };
            CreateRecipeCategory.Info<T> info = new CreateRecipeCategory.Info<>(
                    createRecipeHolderType(Gearbox.asResource(id)),
                    Component.translatable( "gearbox.recipe." + id),
                    background,
                    icon,
                    recipesSupplier,
                    catalysts
            );

            CreateRecipeCategory<T> category = factory.create(info);
            modCategories.add(category);
            return category;
        }

    }

    public static void consumeAllRecipes(Consumer<? super RecipeHolder<?>> consumer) {
        Minecraft.getInstance()
                .getConnection()
                .getRecipeManager()
                .getRecipes()
                .forEach(consumer);
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Recipe<?>> void consumeTypedRecipes(Consumer<RecipeHolder<?>> consumer, RecipeType<?> type) {
        List<? extends RecipeHolder<?>> map = Minecraft.getInstance()
                .getConnection()
                .getRecipeManager().getAllRecipesFor((RecipeType) type);
        if (!map.isEmpty())
            map.forEach(consumer);
    }




}
