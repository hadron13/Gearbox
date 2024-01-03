package io.github.hadron13.gearbox.blocks.lasers;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static io.github.hadron13.gearbox.blocks.lasers.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements LaserEmitter {

    public Color color = Color.BLACK;
    public List<Entity> caughtEntities; // oh no
    public float length = 0.0f;
    public float power = 0.0f;
    public int counter = 0;
    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        color = Color.RED;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
    @Override
    public void lazyTick(){
        updateCaughtEntities();
    }
    @Override
    public void tick(){
        if(level == null)
            return;
        counter+=3;

        if(level.isClientSide) {
            return;
        }

        for(Entity entity : caughtEntities){
            if(!entity.isAlive())
                continue;
            entity.hurt(DamageSource.IN_FIRE, 5f);
        }

        color = Color.rainbowColor(counter);
        sendData();
    }
    public void updateCaughtEntities(){
        if(level == null)
            return;
        BlockPos pos = getBlockPos();
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);

        Vec3 min = Vec3.atLowerCornerOf(pos);
        Vec3 max = Vec3.atLowerCornerOf( pos.relative(facing, (int)getLength()).relative(facing.getCounterClockWise()).above() );

        AABB laser_collision = new AABB(min, max);


        caughtEntities = level.getEntities(null, laser_collision);
    }
    @Override
    public Color getColor() {
        return color;
    }
    @Override
    public float getPower() {
        return 1;
    }

    @Override
    public float getLength() {
        return 50.0f;
    }
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);

        compound.putInt("color", color.getRGB());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        int cor = compound.getInt("color");
        color = new Color(cor);
    }
}
