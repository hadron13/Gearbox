package io.github.hadron13.gearbox.blocks.irradiator;


import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.createmod.catnip.theme.Color;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;


public class IrradiatingRecipe extends BasinRecipe implements LaserRecipe{
    public int requiredColor;
    public float requiredPower;

    public IrradiatingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.IRRADIATING, params);
    }

    public static boolean match(IrradiatorBlockEntity be, IrradiatingRecipe recipe){
        if(recipe == null)
            return false;

        if(be.receivingLaser == null)
            return false;

        if(Mth.abs(be.getSpeed()) < 1f)
            return false;

        if(!LaserRecipe.matchLaser(recipe, be.receivingLaser))
            return false;

        return LaserRecipe.matchLaser(recipe, be.receivingLaser);
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
}
