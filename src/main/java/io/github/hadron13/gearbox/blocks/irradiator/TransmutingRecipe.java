package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder.ProcessingRecipeParams;

import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.compat.jei.category.assembly_subcategories.AssemblyTransmuting;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;


public class TransmutingRecipe extends ProcessingRecipe<RecipeWrapper> implements LaserRecipe, IAssemblyRecipe {
    public int requiredColor;
    public float requiredPower;

    public TransmutingRecipe(ProcessingRecipeParams params){
        super(ModRecipeTypes.TRANSMUTING, params);
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
    public boolean matches(RecipeWrapper pContainer, Level pLevel) {
        return false;
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
        set.add(ModBlocks.IRRADIATOR.get());
    }

    @Override
    public void addAssemblyIngredients(List<Ingredient> list) {

    }

    @Override
    public Supplier<Supplier<SequencedAssemblySubCategory>> getJEISubCategory() {
        return () -> AssemblyTransmuting::new;
    }
}
