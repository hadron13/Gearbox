package io.github.hadron13.gearbox.blocks.laser;

import com.google.common.base.Optional;
import io.github.hadron13.gearbox.register.data.ModDamageTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.common.Tags;

import java.util.List;

public class Laser {
    public int color;
    public Vec3 position;
    public Vec3 direction; //normalized
    public Vec3 lastDirection; //rotation in the last frame, for interpolation
    public float power;
    public float length;
    public int breakTimer = 0;
    public boolean enabled = true;
    public ILaserReceiver receiver = null;

    public Laser(){
        color = 0;
        position = Vec3.ZERO;
        direction = Vec3.ZERO;
    }

    public Laser(int color, Vec3 position, Vec3 rotation) {
        this.color = color;
        this.position = position;
        this.direction = rotation.normalize();//.add(0, 1f, 0);
        this.power = 2f;
        this.length = 100f;
        this.lastDirection = this.direction;
    }
    public Laser(int color, Vec3 position, Vec3 rotation, float power) {
        this.color = color;
        this.position = position;
        this.direction = rotation.normalize();//.add(0, 1f, 0);
        this.power = power;
        this.length = 100f;
        this.lastDirection = this.direction;
    }

    public void tick(Level level){
        if(!enabled) return;

        BlockHitResult block;
        Optional<Vec3> nextPosition = Optional.of(position.add(getDirection()));
        length = 1;
        do{
            block = level.clip(new ClipContext(nextPosition.get(), nextPosition.get().add(direction.scale(100f)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
            if(block.getType() == HitResult.Type.MISS) {
                length = 100f;
                if(this.receiver != null){
                    this.receiver.endReceiveLaser(this);
                    this.receiver = null;
                }
                break;
            }
            nextPosition = handleBlockIntersection(level, nextPosition.get(), block);
        }while (nextPosition.isPresent());

        if(level.random.nextInt(5) != 0)
            return;
        if(level.isClientSide)
            return;

        AABB aabb = new AABB(position, position.add(direction.scale(100f))).inflate(1.0); // Expand AABB slightly to catch entities
        List<Entity> entities = level.getEntities((Entity) null, aabb, entity -> entity.isAlive() && entity.isPickable());

        for (Entity entity : entities) {
            AABB entityAABB = entity.getBoundingBox().inflate(entity.getPickRadius());
            if (entityAABB.clip(position, position.add(direction.scale(length))).isPresent()) {
                entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.laser)), power * 4);
                entity.setSecondsOnFire(3);
            }
        }

    }


    /**
     * Handles block raycast result
     * @param level current world
     * @param position previous position used for raycasting
     * @param block the result from raycasting
     * @return new position to raycast from, in case it hits a block such as glass and needs to continue
     */
    public Optional<Vec3> handleBlockIntersection(Level level, Vec3 position, BlockHitResult block){
        BlockState blockState = level.getBlockState(block.getBlockPos());

        boolean catchesFire = blockState.isFlammable(level, block.getBlockPos(), block.getDirection());
        float hardness = blockState.getDestroySpeed(level, block.getBlockPos());
        float distance = (float)VecHelper.getCenterOf(block.getBlockPos()).distanceTo(position);
        boolean canBreak = hardness > -1 && hardness < power;

        length += distance;
        if(blockState.is(Tags.Blocks.GLASS)){
            return Optional.of( position.add(direction.scale(distance)) );
        }

        BlockEntity be = level.getBlockEntity(block.getBlockPos());
        if(be instanceof ILaserReceiver receiver){
            receiver.receiveLaser(this);
            if(this.receiver != receiver){
                if(this.receiver != null) this.receiver.endReceiveLaser(this);
                this.receiver = receiver;
            }
            return Optional.absent();
        }

        if(this.receiver != null){
            this.receiver.endReceiveLaser(this);
            this.receiver = null;
        }

        if(!canBreak && !catchesFire) {
            breakTimer = 0;
        }else {
            breakTimer++;
            if(level.isClientSide){
                Vec3 opposite= direction.reverse();
                Vec3 particlePos = Vec3.atCenterOf(block.getBlockPos());

                Vec3 velocity = VecHelper.offsetRandomly(opposite, level.random, 0.5f);

                level.addParticle(ParticleTypes.LAVA,
                        particlePos.x + opposite.x, particlePos.y + opposite.y, particlePos.z + opposite.z,
                        velocity.x, velocity.y, velocity.z);
            }

            if ((canBreak && breakTimer >= (hardness * 10) / power) || (catchesFire && breakTimer >= 20 / power)) {
                level.destroyBlock(block.getBlockPos(), true);
                breakTimer = 0;
            }
        }
        return Optional.absent();
    }

    public void enable(){
        this.enabled = true;
    }

    public void disable(){
        this.enabled = false;
        if(this.receiver != null) this.receiver.endReceiveLaser(this);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getDirection() {
        return direction;
    }

    public void setDirection(Vec3 direction) {
        lastDirection = this.direction;
        this.direction = direction;
    }

    public float getPower(){
        return this.power;
    }
    public void setPower(float power){
        this.power = power;
    }

    public CompoundTag write(CompoundTag nbt, String prefix) {
        nbt.putInt(prefix + "color", color);
        nbt.putFloat(prefix + "length", length);
        nbt.put(prefix + "position", writeVec3(position));
        nbt.put(prefix + "rotation", writeVec3(direction));
        nbt.putBoolean(prefix + "enabled", enabled);
        return nbt;
    }

    public void read(CompoundTag nbt, String prefix) {
        color = nbt.getInt(prefix + "color");
        length = nbt.getFloat(prefix + "length");
        position = readVec3(nbt.getList(prefix + "position", Tag.TAG_INT));
        direction = readVec3(nbt.getList(prefix + "rotation", Tag.TAG_INT));
        enabled = nbt.getBoolean(prefix + "enabled");
    }


    public static ListTag writeVec3(Vec3 vec) {
        ListTag tag = new ListTag();
        tag.add(DoubleTag.valueOf( vec.x() ));
        tag.add(DoubleTag.valueOf( vec.y() ));
        tag.add(DoubleTag.valueOf( vec.z() ));
        return tag;
    }

    public static Vec3 readVec3(ListTag tag) {
        return new Vec3(tag.getDouble(0), tag.getDouble(1), tag.getDouble(2));
    }
}
