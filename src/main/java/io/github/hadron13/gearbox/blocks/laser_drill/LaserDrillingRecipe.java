package io.github.hadron13.gearbox.blocks.laser_drill;

import com.google.gson.JsonObject;
import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.latvian.mods.rhino.ast.Block;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.irradiator.IrradiatorBlockEntity;
import io.github.hadron13.gearbox.blocks.irradiator.LaserRecipe;
import io.github.hadron13.gearbox.blocks.irradiator.TransmutingRecipe;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class LaserDrillingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe {

    public Color requiredColor;
    public float requiredPower;

    public LaserDrillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params){
        super(ModRecipeTypes.LASER_DRILLING, params);
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

    public static boolean match(LaserDrillBlockEntity be, LaserDrillingRecipe recipe){
        if(recipe == null)
            return false;

        if(be.totalPower < recipe.requiredPower)
            return false;
        if(!matchColor(recipe.requiredColor, be.mixedColor))
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
    public Color getColor() {
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
