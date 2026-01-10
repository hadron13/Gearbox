package io.github.hadron13.gearbox.blocks.irradiator;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.compat.jei.category.sequencedAssembly.SequencedAssemblySubCategory;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;


import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.IAssemblyRecipe;
import io.github.hadron13.gearbox.GearboxLang;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzingRecipe;
import io.github.hadron13.gearbox.blocks.electrolyzer.EnergyRecipeParams;
import io.github.hadron13.gearbox.compat.jei.category.assembly_subcategories.AssemblyTransmuting;
import io.github.hadron13.gearbox.register.GearboxBlocks;
import io.github.hadron13.gearbox.register.GearboxRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;


public class TransmutingRecipe extends ProcessingRecipe<SingleRecipeInput, LaserRecipeParams> implements LaserRecipe, IAssemblyRecipe {
    public int requiredColor;
    public float requiredPower;

    public TransmutingRecipe(LaserRecipeParams params){
        super(GearboxRecipeTypes.TRANSMUTING, params);
        requiredPower = params.power;
        requiredColor = params.color;
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

    @Override
    protected boolean canSpecifyDuration() {
        return true;
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

    public static class Serializer<R extends TransmutingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(ProcessingRecipe.Factory<LaserRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, LaserRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, LaserRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
