package io.github.hadron13.gearbox.blocks.electrolyzer;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class ElectrolyzingRecipe extends BasinRecipe {
    public int requiredEnergy = 0;
    public int baseProcessingTime = 0;
    public ElectrolyzingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.ELECTROLYZING, params);
    }

    public void readAdditional(JsonObject json) {
        requiredEnergy = GsonHelper.getAsInt(json, "energy", 1000);
        baseProcessingTime = GsonHelper.getAsInt(json, "processingTime", 500);
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        requiredEnergy = buffer.readInt();
        baseProcessingTime = buffer.readInt();
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("energy", requiredEnergy);
        json.addProperty("processingTime", baseProcessingTime);
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(requiredEnergy);
        buffer.writeInt(baseProcessingTime);
    }
}
