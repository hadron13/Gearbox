package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.processing.basin.BasinOperatingBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.ILaserReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class IrradiatorBlockEntity extends BasinOperatingBlockEntity implements ILaserReceiver {

    public Map<Direction, Float> powers;
    public Map<Direction, Color> colors;
    public Map<Direction, Integer> timeouts;
    public Color mixedColor = Color.BLACK;
    public float totalPower = 0f;
    public int recipeTimer = 0;
    public IrradiatingRecipe currentRecipe;
    public BeltProcessingBehaviour beltBehavior;

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
    }



    @Override
    protected boolean isRunning() {
        return false;
    }

    @Override
    protected void onBasinRemoved() {

    }

    @Override
    protected <C extends Container> boolean matchStaticFilters(Recipe<C> recipe) {
        return false;
    }

    @Override
    protected Object getRecipeCacheKey() {
        return null;
    }

    @Override
    public boolean receiveLaser(Direction face, Color color, float power) {
        return false;
    }
}
