package io.github.hadron13.gearbox.blocks.irradiator;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
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
    public int animationTime = 0;
    public IrradiatingRecipe currentRecipe;
    public BeltProcessingBehaviour beltBehavior;
    public PressingBehaviour.Mode mode;

    public IrradiatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
        powers = new HashMap<>();
        colors = new HashMap<>();
        timeouts = new HashMap<>();
        currentRecipe = null;
    }

    @Override
    public void tick(){
        timeouts.forEach((dir, timer) -> {
            if(timer != 0)
                timeouts.replace(dir, --timer);
            else{
                powers.remove(dir);
                colors.remove(dir);
            }
        });
        int r = 0, g = 0, b = 0;
        for(Color color : colors.values()){
            r += color.getRed();
            g += color.getGreen();
            b += color.getBlue();
        }
        int ammount = colors.values().size();

        mixedColor.setRed  (r/ammount);
        mixedColor.setGreen(g/ammount);
        mixedColor.setBlue (b/ammount);

        totalPower = 0f;
        for(float power : powers.values())
            totalPower += power;



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

        if(face.getStepY() != 0){
            powers.put(face, power);
            colors.put(face, color);
            timeouts.put(face, 3);
        }

        return false;
    }
}
