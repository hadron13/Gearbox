package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;


import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.compat.jei.category.assembly_subcategories.AssemblyTransmuting;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;


public class TransmutingRecipe extends StandardProcessingRecipe<SingleRecipeInput> implements LaserRecipe, IAssemblyRecipe {
    public int requiredColor;
    public float requiredPower;

    public TransmutingRecipe(ProcessingRecipeParams params){
        super(GearboxRecipeTypes.TRANSMUTING, params);
    }


    public static boolean match(IrradiatorBlockEntity be, TransmutingRecipe recipe, ItemStack ingredient){
        if(recipe == null)
            return false;

        if(be.receivingLaser == null)
            return false;

        if(Mth.abs(be.getSpeed()) < 1f)
            return false;

        if(!LaserRecipe.matchLaser(recipe, be.receivingLaser))
            return false;

        ItemStack recipeStack = recipe.getIngredients().get(0).getItems()[0];
        return recipeStack.is(ingredient.getItem()) && ingredient.getCount() >= recipeStack.getCount();
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
    public Component getDescriptionForAssembly() {
        return GearboxLang.translateDirect("recipe.assembly.mechanizing");
    }

    @Override
    public void addRequiredMachines(Set<ItemLike> set) {
        set.add(GearboxBlocks.IRRADIATOR.get());
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {

    }

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> AssemblyTransmuting::new;
    }

    @Override
    public boolean matches(SingleRecipeInput singleRecipeInput, Level level) {
        return false;
    }
}
