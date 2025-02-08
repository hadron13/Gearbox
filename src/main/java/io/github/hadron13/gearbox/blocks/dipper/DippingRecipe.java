package io.github.hadron13.gearbox.blocks.dipper;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class DippingRecipe extends BasinRecipe  {

    int dips;
    Item []layers = new Item[16];

    public DippingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.DIPPING, params);
    }

    public static boolean match(DipperBlockEntity dipper, DippingRecipe recipe){

        ItemStack input_item = recipe.getIngredients().get(0).getItems()[0];
        FluidStack atmosphere = recipe.getFluidResults().get(0);
        FluidStack dipped_fluid = recipe.getFluidResults().get(0);


        return false;
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
