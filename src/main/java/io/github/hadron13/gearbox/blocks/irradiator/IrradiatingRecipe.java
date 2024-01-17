package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;


public class IrradiatingRecipe extends ProcessingRecipe<RecipeWrapper>{
    public Color requiredColor;
    public float requiredPower;

    public IrradiatingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.IRRADIATING, params);
    }

    @Override
    protected int getMaxInputCount() {
        return 3;
    }

    @Override
    protected int getMaxOutputCount() {
        return 3;
    }


    protected boolean canRequireHeat() {
        return true;
    }

    protected boolean canSpecifyDuration() {
        return true;
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
}
