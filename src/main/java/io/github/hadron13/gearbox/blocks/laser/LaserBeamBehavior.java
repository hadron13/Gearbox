package io.github.hadron13.gearbox.blocks.laser;

import com.jozufozu.flywheel.util.Color;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.List;


public class LaserBeamBehavior extends BlockEntityBehaviour {
    public static final BehaviourType<LaserBeamBehavior> TYPE = new BehaviourType<>();
    public static final int MAX_LENGTH = 100;

    public Color color = Color.BLACK;
    public float length = 0;
    public float power = 0;
    public Direction facing = Direction.NORTH;
    public BlockPos origin = BlockPos.ZERO;

    public List<Entity> caughtEntities;
    public int breakTimer = 0;
    public LaserBeamBehavior(SmartBlockEntity be) {
        super(be);
        setLazyTickRate(4);
        caughtEntities = new ArrayList<>();
    }
    @Override
    public void tick(){
        super.tick();
        if(blockEntity.getLevel()==null)
            return;
        updateBeam();
    }
    @Override
    public void lazyTick(){
        if(blockEntity.getLevel() == null)
            return;

        if(blockEntity.getLevel().isClientSide){
            generateParticles();
        }else{
            updateCaughtEntities();
        }
    }
    public void updateBeam(){
        BlockState blockState = null;
        Level level = blockEntity.getLevel();

        for(length = 1; length <= MAX_LENGTH; length++){
            BlockPos currentPosition = origin.relative(facing, (int)length);

            blockState = level.getBlockState(currentPosition);

            if(blockState.isAir() || !blockState.getFluidState().isEmpty())
                continue;

            boolean translucent = blockState.is(Tags.Blocks.GLASS) || blockState.is(Tags.Blocks.GLASS_PANES);

            if(translucent){
                continue;
            }
            if(level.getBlockEntity(currentPosition) instanceof LaserReceiver receiver){
                receiver.receiveLaser(facing.getOpposite(), color, power);
                break;
            }

            boolean catchesFire = blockState.isFlammable(level, currentPosition, facing.getOpposite());

            float hardness = blockState.getDestroySpeed(level, currentPosition);
            boolean canBurn = hardness < power;

            if(!canBurn && !catchesFire) {
                breakTimer = 0;
                break;
            }
            breakTimer++;

            if((canBurn && breakTimer >= (hardness * 10)/power) || (catchesFire && breakTimer >= 15)){
                level.destroyBlock(currentPosition, true);
                breakTimer = 0;
            }
            break;
        }

        for(Entity entity : caughtEntities){
            if(entity instanceof ItemEntity){
                ItemEntity item = (ItemEntity) entity;
                continue;
            }

            if(!entity.isAlive())
                continue;
            blockEntity.getLevel().explode(null, entity.getX(), entity.getY(), entity.getZ(), 10f, Explosion.BlockInteraction.NONE);
            entity.hurt(DamageSource.IN_FIRE, 3f);
            entity.setSecondsOnFire(3);
        }

    }
    public void updateCaughtEntities(){
        Vec3 min = Vec3.atCenterOf( origin );
        Vec3 max = Vec3.atCenterOf( origin.relative(facing, (int)length) );

        AABB laser_collision = new AABB(min, max);

        caughtEntities = blockEntity.getLevel().getEntities(null, laser_collision);
    }
    public void generateParticles(){
        if(breakTimer == 0)
            return;

        Direction oppositeFace = facing.getOpposite();
        Vec3 particlePos = Vec3.atCenterOf(origin.relative(facing, (int)length));

        Vec3 velocity = VecHelper.offsetRandomly(new Vec3(oppositeFace.getStepX(), 0, oppositeFace.getStepZ()), blockEntity.getLevel().random, 1.0f);

        blockEntity.getLevel().addParticle(ParticleTypes.LAVA,
                particlePos.x - facing.getStepX()/2f, particlePos.y + 0.1f, particlePos.z - facing.getStepZ()/2f,
                velocity.x, velocity.y, velocity.z);
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putFloat("length", length);
        compound.putInt("color", color.getRGB());
        compound.putInt("break", breakTimer);
    }

    @Override
    public void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        length = compound.getFloat("length");
        color = new Color(compound.getInt("color"));
        breakTimer = compound.getInt("break");
    }
    @Override
    public BehaviourType<?> getType() {
        return TYPE;
    }
}
