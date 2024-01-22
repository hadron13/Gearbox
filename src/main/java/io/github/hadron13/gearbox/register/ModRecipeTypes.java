package io.github.hadron13.gearbox.register;

import com.google.common.collect.ImmutableSet;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ModRecipeTypes implements IRecipeTypeInfo {
    EXCHANGING(ExchangingRecipe::new),
    PYROPROCESSING(PyroprocessingRecipe::new),
    SAPPING(SappingRecipe::new),
    COMPRESSING(CompressingRecipe::new),
    MECHANIZING(MechanizingRecipe::new),
    IRRADIATING(IrradiatingRecipe::new);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    ModRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier, boolean registerType) {
        String name = Lang.asId(name());
        id = Gearbox.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        if (registerType) {
            typeObject = Registers.TYPE_REGISTER.register(name, typeSupplier);
            type = typeObject;
        } else {
            typeObject = null;
            type = typeSupplier;
        }
    }

    ModRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = Gearbox.asResource(name);
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> simpleType(id));
        type = typeObject;
    }
    ModRecipeTypes(ProcessingRecipeBuilder.ProcessingRecipeFactory<?> processingFactory) {
        this(() -> new ProcessingRecipeSerializer<>(processingFactory));
    }



    public static <T extends Recipe<?>> RecipeType<T> simpleType(ResourceLocation id) {
        String stringId = id.toString();
        return new RecipeType<T>() {
            @Override
            public String toString() {
                return stringId;
            }
        };
    }

    public static void register(IEventBus modEventBus) {
        ShapedRecipe.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
        return (T) serializerObject.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }

    public <C extends Container, T extends Recipe<C>> Optional<T> find(C inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public Optional<ExchangingRecipe> find(Container inv, Level world,boolean waterlogged, float speed) {
        if(world.isClientSide())
            return Optional.empty();
        List<ExchangingRecipe> exchangingRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.EXCHANGING.getType());
        Stream<ExchangingRecipe> exchangingRecipesFiltered = exchangingRecipes.stream().filter(exchangingRecipe -> exchangingRecipe.matches((RecipeWrapper) inv,world));
        return exchangingRecipesFiltered.findAny();
    }

    public Optional<CompressingRecipe> find(CompressorBlockEntity blockEntity, Level world) {

        if(world.isClientSide())
            return Optional.empty();
        List<CompressingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPRESSING.getType());


        Stream<CompressingRecipe> matchingRecipes =
                allRecipes.stream().filter(compressingRecipe -> CompressingRecipe.match(blockEntity, compressingRecipe) );

        return matchingRecipes.findAny();
    }

    public Optional<IrradiatingRecipe> find(IrradiatorBlockEntity blockEntity, Level world, ItemStack ingredient){
        if(world.isClientSide())
            return Optional.empty();
        List<IrradiatingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.IRRADIATING.getType());

        Stream<IrradiatingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> IrradiatingRecipe.match(blockEntity, recipe, ingredient) );

        return matchingRecipes.findAny();
    }


    public List<SappingRecipe> getAll(Level world){
        if(world.isClientSide())
            return null;
        return world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.SAPPING.getType());
    }

    public static final Set<ResourceLocation> RECIPE_DENY_SET =
            ImmutableSet.of(new ResourceLocation("occultism", "spirit_trade"), new ResourceLocation("occultism", "ritual"));

    public static boolean shouldIgnoreInAutomation(Recipe<?> recipe) {
        RecipeSerializer<?> serializer = recipe.getSerializer();
        if (serializer != null && RECIPE_DENY_SET.contains(RegisteredObjects.getKeyOrThrow(serializer)))
            return true;
        return recipe.getId()
                .getPath()
                .endsWith("_manual_only");
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Gearbox.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Gearbox.MODID);
    }

}