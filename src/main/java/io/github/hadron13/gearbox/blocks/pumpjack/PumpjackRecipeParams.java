package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.function.Function;

public class PumpjackRecipeParams extends ProcessingRecipeParams {

    protected String biome;

    protected PumpjackRecipeParams(){
        super();
        biome = "";
    }

    public static MapCodec<PumpjackRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.either(FluidStack.CODEC, ProcessingOutput.CODEC).listOf().fieldOf("results")
                    .forGetter(PumpjackRecipeParams::results),
            Codec.STRING.optionalFieldOf("biome", "minecraft:void").forGetter(PumpjackRecipeParams::biome)
    ).apply(instance, (results, biome) -> {
        PumpjackRecipeParams params = new PumpjackRecipeParams();
        params.biome = biome;
        results.forEach(either -> either
                .ifRight(params.results::add)
                .ifLeft(params.fluidResults::add));
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, PumpjackRecipeParams> STREAM_CODEC = streamCodec(PumpjackRecipeParams::new);

    protected final String biome() {
        return biome;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.STRING_UTF8.encode(buffer, biome);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        biome = ByteBufCodecs.STRING_UTF8.decode(buffer);
    }
}
