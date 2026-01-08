package io.github.hadron13.gearbox.blocks.pumpjack;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class PumpjackRecipeParams extends ProcessingRecipeParams {

    public static MapCodec<PumpjackRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(PumpjackRecipeParams::new).forGetter(Function.identity()),
            Codec.STRING.optionalFieldOf("biome", "minecraft:plains").forGetter(PumpjackRecipeParams::biome)
    ).apply(instance, (params, biome) -> {
        params.biome = biome;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, PumpjackRecipeParams> STREAM_CODEC = streamCodec(PumpjackRecipeParams::new);

    protected String biome;

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
