package io.github.hadron13.gearbox.blocks.pumpjack;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class PumpjackRecipe extends ProcessingRecipe<RecipeWrapper> {

    public ResourceKey<Biome> biome;
    public float density;

    public PumpjackRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.PUMPJACK, params);
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

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }


    public void readAdditional(JsonObject json) {
        String biome_key = GsonHelper.getAsString(json, "biome");
        if(biome_key == null){
            Gearbox.LOGGER.warn("invalid biome in recipe " + this.getId().getPath());
            return;
        }
        biome = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(biome_key));
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        String biome_key = buffer.readUtf();
        if(biome_key == null){
            Gearbox.LOGGER.warn("invalid biome in recipe " + this.getId().getPath());
            return;
        }
        biome = ResourceKey.create(ForgeRegistries.BIOMES.getRegistryKey(), new ResourceLocation(biome_key));
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("biome", biome.registry().getPath());
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeUtf(biome.registry().getPath());
    }
}
