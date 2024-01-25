package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import static com.simibubi.create.content.kinetics.press.PressingBehaviour.Mode.BASIN;


public class IrradiatingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe{
    public Color requiredColor;
    public float requiredPower;

    public IrradiatingRecipe(ProcessingRecipeParams params){
        super(ModRecipeTypes.IRRADIATING, params);
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

    public static boolean match(IrradiatorBlockEntity be, IrradiatingRecipe recipe, ItemStack ingredient){
        if(be.totalPower < recipe.requiredPower)
            return false;

        if(!matchColor(recipe.requiredColor, be.recipeColor))
            return false;

        return recipe.getIngredients().get(0).getItems()[0].sameItem(ingredient);
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
