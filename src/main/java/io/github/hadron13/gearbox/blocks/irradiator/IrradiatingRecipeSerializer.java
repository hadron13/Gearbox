package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class IrradiatingRecipeSerializer <IrradiatingRecipe extends Recipe<?>> extends ForgeRegistryEntry<RecipeSerializer<?>>
	implements RecipeSerializer<IrradiatingRecipe>{

    @Override
    public IrradiatingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {




        return null;
    }

    @Nullable
    @Override
    public IrradiatingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        return null;
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, IrradiatingRecipe pRecipe) {

    }
}
