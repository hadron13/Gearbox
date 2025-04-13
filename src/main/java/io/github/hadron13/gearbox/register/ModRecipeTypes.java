package io.github.hadron13.gearbox.register;

import com.google.common.collect.ImmutableSet;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.gearbox.blocks.chemical_reactor.ReactingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.dipper.DippingRecipe;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.laser_drill.LaserDrillingRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
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
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum ModRecipeTypes implements IRecipeTypeInfo {
    PYROPROCESSING(PyroprocessingRecipe::new),
    SAPPING(SappingRecipe::new),
    COMPRESSING(CompressingRecipe::new),
    MECHANIZING(MechanizingRecipe::new),
    IRRADIATING(IrradiatingRecipe::new),
    TRANSMUTING(TransmutingRecipe::new),
    ELECTROLYZING(ElectrolyzingRecipe::new),
    CENTRIFUGING(CentrifugingRecipe::new),
    LASER_DRILLING(LaserDrillingRecipe::new),
    PUMPJACK(PumpjackRecipe::new),
    DIPPING(DippingRecipe::new),
    REACTING(ReactingRecipe::new);

    private final ResourceLocation id;
    private final RegistryObject<RecipeSerializer<?>> serializerObject;
    @Nullable
    private final RegistryObject<RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;

    public static final Predicate<? super Recipe<?>> CAN_BE_AUTOMATED = r -> !r.getId()
            .getPath()
            .endsWith("_manual_only");


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


    public Optional<CompressingRecipe> find(CompressorBlockEntity blockEntity, Level world) {

        if(world.isClientSide())
            return Optional.empty();
        List<CompressingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPRESSING.getType());


        Stream<CompressingRecipe> matchingRecipes =
                allRecipes.stream().filter(compressingRecipe -> CompressingRecipe.match(blockEntity, compressingRecipe) );

        return matchingRecipes.findAny();
    }

    public Optional<TransmutingRecipe> find(IrradiatorBlockEntity blockEntity, Level world, ItemStack ingredient){
        if(world.isClientSide())
            return Optional.empty();
        List<TransmutingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.TRANSMUTING.getType());

        Stream<TransmutingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> TransmutingRecipe.match(blockEntity, recipe, ingredient) );

        return matchingRecipes.findAny();
    }
    public Optional<CentrifugingRecipe> find(CentrifugeBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<CentrifugingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.CENTRIFUGING.getType());

        Stream<CentrifugingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> CentrifugingRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }
    public Optional<LaserDrillingRecipe> find(LaserDrillBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<LaserDrillingRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.LASER_DRILLING.getType());

        Stream<LaserDrillingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> LaserDrillingRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }

    public Optional<PumpjackRecipe> find(PumpjackWellBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();
        List<PumpjackRecipe> allRecipes = world.getRecipeManager().getAllRecipesFor(ModRecipeTypes.PUMPJACK.getType());

        Stream<PumpjackRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> PumpjackRecipe.match(blockEntity, recipe) );

        return matchingRecipes.findAny();
    }



    public static final Set<ResourceLocation> RECIPE_DENY_SET =
            ImmutableSet.of(new ResourceLocation("occultism", "spirit_trade"), new ResourceLocation("occultism", "ritual"));

    public static boolean shouldIgnoreInAutomation(Recipe<?> recipe) {
        RecipeSerializer<?> serializer = recipe.getSerializer();
        if (serializer != null && AllTags.AllRecipeSerializerTags.AUTOMATION_IGNORE.matches(serializer))
            return true;
        return !CAN_BE_AUTOMATED.test(recipe);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Gearbox.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Gearbox.MODID);
    }

}