package io.github.hadron13.gearbox.blocks.dipper;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;

public class DippingRecipe extends BasinRecipe  {

    int dips;
    FluidStack atmosphere;
    Item []layers = new Item[16];

    public DippingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.DIPPING, params);
    }


    public void readAdditional(JsonObject json) {
        dips = GsonHelper.getAsInt(json, "dips", 1);
        for(int i = 0; i < dips; i++){
            //layers[i]
            //TODO: fix this
        }
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        dips = buffer.readInt();
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("dips", dips);
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeInt(dips);
    }

}
