package io.github.hadron13.gearbox.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.brass_press.MechanizingRecipe;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressingRecipe;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillingRecipe;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatingRecipe;
import io.github.hadron13.gearbox.blocks.kiln.PyroprocessingRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipe;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackRecipeParams;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SappingRecipe;
import net.createmod.catnip.lang.Lang;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public enum GearboxRecipeTypes implements IRecipeTypeInfo, StringRepresentable {
    PYROPROCESSING(PyroprocessingRecipe::new),
    SAPPING(SappingRecipe::new),
    COMPRESSING(CompressingRecipe::new),
    MECHANIZING(MechanizingRecipe::new),
    IRRADIATING(() -> new IrradiatingRecipe.Serializer<>(IrradiatingRecipe::new)),
    TRANSMUTING(() -> new TransmutingRecipe.Serializer<>(TransmutingRecipe::new)),
    ELECTROLYZING(() -> new ElectrolyzingRecipe.Serializer<>(ElectrolyzingRecipe::new)),
    CENTRIFUGING(CentrifugingRecipe::new),
    PUMPJACK(PumpjackRecipe::new),
//    LASER_DRILLING(LaserDrillingRecipe::new),
//    DIPPING(DippingRecipe::new),
//    REACTING(ReactingRecipe::new),
    DISTILLING(() -> new DistillingRecipe.Serializer<>(DistillingRecipe::new));

    public final ResourceLocation id;
    public final Supplier<RecipeSerializer<?>> serializerSupplier;
    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<?>> serializerObject;
    @Nullable
    private final DeferredHolder<RecipeType<?>, RecipeType<?>> typeObject;
    private final Supplier<RecipeType<?>> type;


    GearboxRecipeTypes(StandardProcessingRecipe.Factory<?> processingFactory) {
        this(() -> new StandardProcessingRecipe.Serializer<>(processingFactory));
    }

    GearboxRecipeTypes(ProcessingRecipe.Factory<PumpjackRecipeParams, PumpjackRecipe> pumpjackFactory) {
        this(() -> new PumpjackRecipe.Serializer<>(pumpjackFactory));
    }


    GearboxRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier) {
        String name = Lang.asId(name());
        id = Gearbox.asResource(name);
        this.serializerSupplier = serializerSupplier;
        serializerObject = Registers.SERIALIZER_REGISTER.register(name, serializerSupplier);
        typeObject = Registers.TYPE_REGISTER.register(name, () -> RecipeType.simple(id));
        type = typeObject;
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
        ShapedRecipePattern.setCraftingSize(9, 9);
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
    public <I extends RecipeInput, R extends Recipe<I>> RecipeType<R> getType() {
        return (RecipeType<R>) type.get();
    }


    public <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world) {
        return world.getRecipeManager()
                .getRecipeFor(getType(), inv, world);
    }

    public Optional<CompressingRecipe> find(CompressorBlockEntity blockEntity, Level world) {
        if(world.isClientSide())
            return Optional.empty();
        List<RecipeHolder<CompressingRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.COMPRESSING.getType());

        Stream<CompressingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> CompressingRecipe.match(blockEntity, recipe.value()) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }



    public Optional<TransmutingRecipe> find(IrradiatorBlockEntity blockEntity, Level world, ItemStack ingredient) {
        if(world.isClientSide())
            return Optional.empty();
        List<RecipeHolder<TransmutingRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.TRANSMUTING.getType());

        Stream<TransmutingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> TransmutingRecipe.match(blockEntity, recipe.value(), ingredient) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }

    public Optional<IrradiatingRecipe> find(IrradiatorBlockEntity blockEntity, Level world) {
        if(world.isClientSide())
            return Optional.empty();
        List<RecipeHolder<IrradiatingRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.TRANSMUTING.getType());

        Stream<IrradiatingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> IrradiatingRecipe.match(blockEntity, recipe.value()) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }


    public Optional<CentrifugingRecipe> find(CentrifugeBlockEntity blockEntity, Level world) {
        if(world.isClientSide())
            return Optional.empty();
        List<RecipeHolder<CentrifugingRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.CENTRIFUGING.getType());

        Stream<CentrifugingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> CentrifugingRecipe.match(blockEntity, recipe.value()) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }

    public Optional<PumpjackRecipe> find(PumpjackWellBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();


        List<RecipeHolder<PumpjackRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.PUMPJACK.getType());

        Stream<PumpjackRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> PumpjackRecipe.match(blockEntity, recipe.value()) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }

    public Optional<DistillingRecipe> find(DistillationControllerBlockEntity blockEntity, Level world){
        if(world.isClientSide())
            return Optional.empty();

        List<RecipeHolder<DistillingRecipe>> allRecipes = world.getRecipeManager().getAllRecipesFor(GearboxRecipeTypes.DISTILLING.getType());

        Stream<DistillingRecipe> matchingRecipes =
                allRecipes.stream().filter(recipe -> DistillingRecipe.match(blockEntity, recipe.value()) ).map(RecipeHolder::value);

        return matchingRecipes.findAny();
    }



    @Override
    public String getSerializedName() {
        return id.toString();
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, Gearbox.MODID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Gearbox.MODID);
    }

}