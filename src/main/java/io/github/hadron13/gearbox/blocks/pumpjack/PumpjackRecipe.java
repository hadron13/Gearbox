package io.github.hadron13.gearbox.blocks.pumpjack;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class PumpjackRecipe extends StandardProcessingRecipe<RecipeInput> {

    public ResourceKey<Biome> biome;
    public float density;

    public PumpjackRecipe(ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.PUMPJACK, params);
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



    public void readAdditional(JsonObject json) {
        String biome_key = GsonHelper.getAsString(json, "biome");
        if(biome_key == null){
            Gearbox.LOGGER.warn("invalid biome in recipe " + this.toString());
            return;
        }
        biome = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(biome_key));
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        String biome_key = buffer.readUtf();
        if(biome_key == null){
            Gearbox.LOGGER.warn("invalid biome in recipe " + this.toString());
            return;
        }
        biome = ResourceKey.create(Registries.BIOME, ResourceLocation.parse(biome_key));
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("biome", biome.location().toString());
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeUtf(biome.location().toString());
    }

    @Override
    public boolean matches(RecipeInput recipeInput, Level level) {
        return false;
    }
}
