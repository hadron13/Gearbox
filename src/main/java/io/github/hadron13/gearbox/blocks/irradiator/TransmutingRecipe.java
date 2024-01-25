package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;


public class TransmutingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe{
    public Color requiredColor;
    public float requiredPower;

    public TransmutingRecipe(ProcessingRecipeParams params){
        super(ModRecipeTypes.TRANSMUTING, params);
    }

    public static boolean matchColor(Color required, Color provided){
        if(required == Color.BLACK)
            return true;

        if(Mth.abs(required.getRed() - provided.getRed()) > 5)
            return false;
        if(Mth.abs(required.getGreen() - provided.getGreen()) > 5)
            return false;
        if(Mth.abs(required.getBlue() - provided.getBlue()) > 5)
            return false;
        return true;
    }

    public static boolean match(IrradiatorBlockEntity be, TransmutingRecipe recipe, ItemStack ingredient){
        if(recipe == null)
            return false;

        if(be.totalPower < recipe.requiredPower)
            return false;

        if(Mth.abs(be.getSpeed()) < 1f)
            return false;

        if(!matchColor(recipe.requiredColor, be.recipeColor))
            return false;
        ItemStack recipeStack = recipe.getIngredients().get(0).getItems()[0];
        return recipeStack.sameItem(ingredient) && ingredient.getCount() >= recipeStack.getCount();
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 3;
    }


    public void readAdditional(JsonObject json) {
        requiredColor = new Color(GsonHelper.getAsInt(json, "color", 0));
        requiredPower = GsonHelper.getAsFloat(json, "power", 1f);
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        requiredColor = new Color(buffer.readInt());
        requiredPower = buffer.readFloat();
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("color", requiredColor.getRGB());
        json.addProperty("power", requiredPower);
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(requiredColor.getRGB());
        buffer.writeFloat(requiredPower);
    }
    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }

    @Override
    public Color getColor() {
        return requiredColor;
    }

    @Override
    public float getPower() {
        return requiredPower;
    }
}
