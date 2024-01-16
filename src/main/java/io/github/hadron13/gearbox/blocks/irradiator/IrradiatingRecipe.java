package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class IrradiatingRecipe implements Recipe<RecipeWrapper> {

    public ResourceLocation id;
    public IrradiatingRecipeSerializer serializer;
    public RecipeType<?> type;

    public boolean requiresColor;
    public Color color;
    public float power;
    public ItemStack ingredient, result;

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {

        return false;
    }

    @Override
    public ItemStack assemble(RecipeWrapper pContainer) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
        @Override
    public String getGroup() {
        return "processing";
    }
    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
