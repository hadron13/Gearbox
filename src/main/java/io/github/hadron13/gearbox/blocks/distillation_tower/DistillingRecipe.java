package io.github.hadron13.gearbox.blocks.distillation_tower;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DistillingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public DistillationControllerBlockEntity.DistilMode mode;

    public DistillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(GearboxRecipeTypes.DISTILLING, params);
    }

    public static  boolean match(DistillationControllerBlockEntity be, DistillingRecipe recipe){
        return recipe.getFluidIngredients().get(0).test(be.inputTank.getPrimaryHandler().getFluidInTank(0))
                && be.distilMode.get() == recipe.mode;
    }

    @Override
    protected int getMaxInputCount() {
        return 0;
    }

    @Override
    protected int getMaxOutputCount() {
        return 0;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 8;
    }

    public void readAdditional(JsonObject json) {
        String mode_name = GsonHelper.getAsString(json, "mode");
        if(mode_name == null){
            Gearbox.LOGGER.warn("invalid mode in recipe " + this.getId().getPath());
            return;
        }
        switch (mode_name){
            case "distil_flash" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_FLASH;
            case "distil_atmospheric" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_ATMOSPHERIC;
            case "distil_vacuum" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_VACUUM;
        }
    }

    public void readAdditional(FriendlyByteBuf buffer) {
        String mode_name = buffer.readUtf();
        if(mode_name== null){
            Gearbox.LOGGER.warn("invalid mode in recipe " + this.getId().getPath());
            return;
        }
        switch (mode_name){
            case "distil_flash" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_FLASH;
            case "distil_atmospheric" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_ATMOSPHERIC;
            case "distil_vacuum" -> mode = DistillationControllerBlockEntity.DistilMode.DISTIL_VACUUM;
        }
    }

    public void writeAdditional(JsonObject json) {
        json.addProperty("mode", mode.toString().toLowerCase());
    }

    public void writeAdditional(FriendlyByteBuf buffer) {
        buffer.writeUtf(mode.toString().toLowerCase());
    }


    @Override
    public boolean matches(RecipeWrapper container, Level level) {
        return false;
    }
}
