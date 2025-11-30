package io.github.hadron13.gearbox.blocks.laser_drill;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.hadron13.gearbox.blocks.irradiator.LaserRecipe;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.createmod.catnip.theme.Color;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class LaserDrillingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe {

    public int requiredColor;
    public float requiredPower;

    public LaserDrillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params){
        super(ModRecipeTypes.LASER_DRILLING, params);
    }


    public static boolean match(LaserDrillBlockEntity be, LaserDrillingRecipe recipe){
        if(recipe == null)
            return false;

        if(!LaserRecipe.matchLaser(recipe, null))
            return false;

        return true;
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
        requiredColor = GsonHelper.getAsInt(json, "color", 0);
        requiredPower = GsonHelper.getAsFloat(json, "power", 1f);
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        requiredColor = buffer.readInt();
        requiredPower = buffer.readFloat();
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("color", requiredColor);
        json.addProperty("power", requiredPower);
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(requiredColor);
        buffer.writeFloat(requiredPower);
    }



    @Override
    public int getColor() {
        return requiredColor;
    }

    @Override
    public float getPower() {
        return requiredPower;
    }

    @Override
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
    }
}
