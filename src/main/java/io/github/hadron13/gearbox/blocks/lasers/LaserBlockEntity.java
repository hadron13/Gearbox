package io.github.hadron13.gearbox.blocks.lasers;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import io.github.hadron13.gearbox.Gearbox;
import net.minecraft.client.ParticleStatus;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeBlockTagsProvider;

import java.util.ArrayList;
import java.util.List;

import static io.github.hadron13.gearbox.blocks.lasers.LaserBlock.HORIZONTAL_FACING;

public class LaserBlockEntity extends SmartBlockEntity implements LaserEmitter {

    public static final int MAX_LENGTH = 100;

    public Color color = Color.BLACK;
    public List<Entity> caughtEntities = new ArrayList<>(); // oh no
    public float length = 0.0f;
    public float power = 0.0f;
    public int breakTimer = 0;
    public int counter = 0;
    public LaserBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        setLazyTickRate(5);
        caughtEntities = new ArrayList<>();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {

    }
    @Override
    public void lazyTick(){
//        updateBeam();
        updateCaughtEntities();
    }
    @Override
    public void tick(){
        super.tick();
        if(level == null)
            return;
        counter+=3;

        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);
        if(level.isClientSide) {
            if(breakTimer != 0 && level.random.nextInt(3) == 1) {
                Direction oppositeFace = facing.getOpposite();
                Vec3 particlePos = Vec3.atCenterOf(getBlockPos().relative(facing, (int)getLength()));

                Vec3 velocity = VecHelper.offsetRandomly(new Vec3(oppositeFace.getStepX(), 0, oppositeFace.getStepZ()), level.random, 1.0f);

                level.addParticle(ParticleTypes.LAVA,
                        particlePos.x - facing.getStepX()/2f, particlePos.y + 0.1f, particlePos.z - facing.getStepZ()/2f,
                        velocity.x, velocity.y, velocity.z);
            }
            return;
        }

        BlockState blockState = null;
        for(length = 1; length <= MAX_LENGTH; length++){
            BlockPos currentPosition = getBlockPos().relative(facing, (int)length);

            blockState = level.getBlockState(currentPosition);

            if(blockState.isAir() || !blockState.getFluidState().isEmpty())
                continue;
            boolean translucent = blockState.is(Tags.Blocks.GLASS) || blockState.is(Tags.Blocks.GLASS_PANES);
            if(translucent){
                continue;
            }

            boolean catchesFire = blockState.isFlammable(level, currentPosition, facing.getOpposite());

            float hardness = blockState.getDestroySpeed(level, currentPosition);
            boolean canBurn = hardness < 3.0f;

            if(!canBurn && !catchesFire) {
                breakTimer = 0;
                break;
            }
            breakTimer++;

            if((canBurn && breakTimer >= (float)(hardness * 10)) || (catchesFire && breakTimer >= 15)){
                level.destroyBlock(currentPosition, true);
                breakTimer = 0;
            }
            break;
        }
        length += 0.49f;

        for(Entity entity : caughtEntities){
            if(entity instanceof ItemEntity){
                ItemEntity item = (ItemEntity) entity;
                continue;
            }

            if(!entity.isAlive())
                continue;
            entity.hurt(DamageSource.IN_FIRE, 3f);
            entity.setSecondsOnFire(3);
        }

        color = Color.rainbowColor(counter);
        sendData();
    }


//    public void updateBeam(){
//
//    }

    public void updateCaughtEntities(){
        if(level == null)
            return;
        BlockPos pos = getBlockPos();
        Direction facing = getBlockState().getValue(HORIZONTAL_FACING);

        Vec3 min = Vec3.atCenterOf(pos);
        Vec3 max = Vec3.atCenterOf( pos.relative(facing, (int)getLength()) );

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
        return length;
    }
    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("length", length);
        compound.putInt("color", color.getRGB());
        compound.putInt("break", breakTimer);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        length = compound.getFloat("length");
        color = new Color(compound.getInt("color"));
        breakTimer = compound.getInt("break");
    }
}
