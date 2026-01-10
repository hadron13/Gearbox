package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class EnergyRecipeParams extends ProcessingRecipeParams {

    public static MapCodec<EnergyRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(EnergyRecipeParams::new).forGetter(Function.identity()),
            Codec.INT.optionalFieldOf("energy", 100).forGetter(EnergyRecipeParams::energy)
    ).apply(instance, (params, energy) -> {
        params.energy = energy;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, EnergyRecipeParams> STREAM_CODEC = streamCodec(EnergyRecipeParams::new);

    protected int energy;

    protected final int energy() {return energy;}

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.INT.encode(buffer, energy);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        energy = ByteBufCodecs.INT.decode(buffer);
    }
}
