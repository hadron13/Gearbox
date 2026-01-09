package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.fluids.FluidStack;

public class PumpjackRecipe extends ProcessingRecipe<RecipeInput, PumpjackRecipeParams> {

    public ResourceKey<Biome> biome;
    public float density;

    public PumpjackRecipe(PumpjackRecipeParams params) {
        super(GearboxRecipeTypes.PUMPJACK, params);
        biome = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(params.biome));
    }

    public static boolean match(PumpjackWellBlockEntity be, PumpjackRecipe recipe){
        return be.getLevel().getBiome(be.getBlockPos()).is(recipe.biome);

    }

    public FluidStack getFluidResult(){
        return getFluidResults().get(0);
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }


    public static class Serializer<R extends PumpjackRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<PumpjackRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, PumpjackRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, PumpjackRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }
}
