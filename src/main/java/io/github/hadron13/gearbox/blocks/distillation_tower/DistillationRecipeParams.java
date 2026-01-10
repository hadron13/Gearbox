package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class DistillationRecipeParams extends ProcessingRecipeParams {
    public static MapCodec<DistillationRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(DistillationRecipeParams::new).forGetter(Function.identity()),
            Codec.STRING.fieldOf("mode").forGetter(DistillationRecipeParams::mode)
    ).apply(instance, (params, mode) -> {
        params.mode = mode;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, DistillationRecipeParams> STREAM_CODEC = streamCodec(DistillationRecipeParams::new);

    protected String mode;

    protected final String mode() {return mode;}

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.STRING_UTF8.encode(buffer, mode);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        mode = ByteBufCodecs.STRING_UTF8.decode(buffer);
    }
}
