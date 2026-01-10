package io.github.hadron13.gearbox.blocks.irradiator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import io.github.hadron13.gearbox.blocks.electrolyzer.EnergyRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class LaserRecipeParams extends ProcessingRecipeParams {

    public static MapCodec<LaserRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(LaserRecipeParams::new).forGetter(Function.identity()),
            Codec.FLOAT.optionalFieldOf("power", 2f).forGetter(LaserRecipeParams::power),
            Codec.INT.fieldOf("color").forGetter(LaserRecipeParams::color)
    ).apply(instance, (params, power, color) -> {
        params.power = power;
        params.color = color;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, LaserRecipeParams> STREAM_CODEC = streamCodec(LaserRecipeParams::new);

    protected float power;
    protected int color;

    protected final float power() {return power;}
    protected final int color() {return color;}

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.FLOAT.encode(buffer, power);
        ByteBufCodecs.INT.encode(buffer, color);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        power = ByteBufCodecs.FLOAT.decode(buffer);
        color = ByteBufCodecs.INT.decode(buffer);
    }
}
