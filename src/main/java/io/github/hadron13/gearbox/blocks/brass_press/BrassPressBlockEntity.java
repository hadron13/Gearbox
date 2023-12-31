package io.github.hadron13.gearbox.blocks.brass_press;


import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingBehaviour.Mode;
import com.simibubi.create.content.kinetics.press.PressingBehaviour.PressingBehaviourSpecifics;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BrassPressBlockEntity extends KineticBlockEntity implements PressingBehaviourSpecifics {

	public static final int CYCLE = 240;

	public PressingBehaviour pressingBehaviour;
	public ItemStack currentItem;
	public int pressingStage;
	public List<ItemStack> particleItems = new ArrayList<>();

	public BrassPressBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		pressingStage = 0;
	}

	@Override
	protected AABB createRenderBoundingBox() {
		return new AABB(worldPosition).expandTowards(0, -1.5, 0)
			.expandTowards(0, 1, 0);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		pressingBehaviour = new PressingBehaviour(this);
		behaviours.add(pressingBehaviour);
	}

	public void onItemPressed(ItemStack result) {

	}

	public PressingBehaviour getPressingBehaviour() {
		return pressingBehaviour;
	}

	@Override
	public boolean tryProcessInBasin(boolean simulate) {
		return false;
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);
		compound.putInt("stage", pressingStage);
		if (clientPacket) {
			compound.put("ParticleItems", NBTHelper.writeCompoundList(particleItems, ItemStack::serializeNBT));
			particleItems.clear();
		}
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);
		pressingStage = compound.getInt("stage");
		if (clientPacket) {
			NBTHelper.iterateCompoundList(compound.getList("ParticleItems", Tag.TAG_COMPOUND),
					c -> particleItems.add(ItemStack.of(c)));
		}
	}

	@Override
	public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
		return false;
	}

	@Override
	public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
		Optional<MechanizingRecipe> recipe = getRecipe(input.stack);
		if (!recipe.isPresent())
			return false;
		if (simulate)
			return true;
//		pressingBehaviour.particleItems.add(input.stack);
		particleItems.add(input.stack);

		List<ItemStack> outputs = RecipeApplier.applyRecipeOn(
			canProcessInBulk() ? input.stack : ItemHandlerHelper.copyStackWithSize(input.stack, 1), recipe.get());

		for (ItemStack created : outputs) {
			if (!created.isEmpty()) {
				onItemPressed(created);
				break;
			}
		}

		outputList.addAll(outputs);
		return true;
	}
	@Override
	public void tick(){
		super.tick();

		if(level == null){
			return;
		}

		int runningTicks = Mth.abs(pressingBehaviour.runningTicks) * 3;

		if((runningTicks == CYCLE / 2 || runningTicks == (CYCLE * 3)/2) && level.isClientSide){
			Vec3 pos = VecHelper.getCenterOf(worldPosition.below(2)).add(0, 8 / 16f, 0);
			for (int i = 0; i < 15; i++) {
				Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, level.random, .125f)
						.multiply(1, 0, 1);
				motion = motion.add(0, 0.125f / 16f, 0);
				level.addParticle(ParticleTypes.FLAME, pos.x, pos.y - .25f, pos.z, motion.x,
						motion.y + .04f, motion.z);
			}
		}

		if(runningTicks == CYCLE / 2 ){
			if(level.isClientSide) {
				particleItems.forEach(stack -> pressingBehaviour.makePressingParticleEffect(VecHelper.getCenterOf(worldPosition.below(2))
						.add(0, 8 / 16f, 0), stack));
			}
			particleItems.clear();

			if (level.getBlockState(worldPosition.below(2))
					.getSoundType() == SoundType.WOOL)
				AllSoundEvents.MECHANICAL_PRESS_ACTIVATION_ON_BELT.playOnServer(level, worldPosition, 1.5f, 1.0f);
			else
				AllSoundEvents.MECHANICAL_PRESS_ACTIVATION.playOnServer(level, worldPosition, 1.0f,
						.75f + (Math.abs(getKineticSpeed()) / 1024f));
			if (!level.isClientSide)
				sendData();
		}
	}
	@Override
	public void onPressingCompleted() {

	}

	private static final RecipeWrapper pressingInv = new RecipeWrapper(new ItemStackHandler(1));

	public Optional<MechanizingRecipe> getRecipe(ItemStack item) {
		Optional<MechanizingRecipe> assemblyRecipe =
			SequencedAssemblyRecipe.getRecipe(level, item, ModRecipeTypes.MECHANIZING.getType(), MechanizingRecipe.class);
		if (assemblyRecipe.isPresent())
			return assemblyRecipe;

		pressingInv.setItem(0, item);
		return ModRecipeTypes.MECHANIZING.find(pressingInv, level);
	}

	public static <C extends Container> boolean canCompress(Recipe<C> recipe) {
		if (!(recipe instanceof CraftingRecipe) || !AllConfigs.server().recipes.allowShapedSquareInPress.get())
			return false;
		NonNullList<Ingredient> ingredients = recipe.getIngredients();
		return (ingredients.size() == 4 || ingredients.size() == 9) && ItemHelper.matchAllIngredients(ingredients);
	}
	@Override
	public float getKineticSpeed() {
		return Mth.clamp(getSpeed(), -96, 96);
	}

	@Override
	public boolean canProcessInBulk() {
		return AllConfigs.server().recipes.bulkPressing.get();
	}
	@Override
	public int getParticleAmount() {
		return 15;
	}

	public float getRenderedHeadRotation(float partialTicks){
		if (!pressingBehaviour.running)
			return 0;
		int runningTicks = Math.abs(pressingBehaviour.runningTicks);

		float ticks = Mth.lerp(partialTicks, pressingBehaviour.prevRunningTicks, runningTicks);
		runningTicks *= 3;
		ticks *= 3;

		if(runningTicks > CYCLE * 2){
			return Mth.clamp( 180 - (ticks - CYCLE * 2), 0, 180);
		}

		if(runningTicks > CYCLE){
			return Mth.clamp((ticks - CYCLE) * 2, 0, 180);
		}
		return 0;
	}

	public float getRenderedHeadOffset(float partialTicks) { // I HATE J4VA
		if (!pressingBehaviour.running)
			return 0;
		int runningTicks = Math.abs(pressingBehaviour.runningTicks);

		float ticks = Mth.lerp(partialTicks, pressingBehaviour.prevRunningTicks, runningTicks);
		runningTicks *= 3;
		ticks *= 3;
		if(runningTicks > CYCLE){
			runningTicks -= CYCLE;
			ticks -= CYCLE;
		}
		if (runningTicks < (CYCLE * 2) / 3)
			return (float) Mth.clamp(Math.pow(ticks / CYCLE * 2, 3), 0, 1);

		return Mth.clamp((CYCLE - ticks) / CYCLE * 3, 0, 1);
	}

	protected boolean isRunning() {
		return pressingBehaviour.running;
	}
}
