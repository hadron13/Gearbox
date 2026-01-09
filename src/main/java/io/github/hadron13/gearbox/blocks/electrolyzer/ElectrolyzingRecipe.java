package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class ElectrolyzingRecipe extends BasinRecipe {
    public int requiredEnergy = 0;
    public ElectrolyzingRecipe(ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.ELECTROLYZING, params);
//        requiredEnergy = params;
    }
//    public static class Serializer<R extends ElectrolyzingRecipe> implements RecipeSerializer<R> {
//        private final MapCodec<R> codec;
//        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;
//
//        public Serializer(ProcessingRecipe.Factory<EnergyRecipeParams, R> factory) {
//            this.codec = ProcessingRecipe.codec(factory, EnergyRecipeParams.CODEC);
//            this.streamCodec = ProcessingRecipe.streamCodec(factory, EnergyRecipeParams.STREAM_CODEC);
//        }
//
//        @Override
//        public MapCodec<R> codec() {
//            return codec;
//        }
//
//        @Override
//        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
//            return streamCodec;
//        }
//    }
}
