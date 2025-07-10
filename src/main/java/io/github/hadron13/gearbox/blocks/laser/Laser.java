package io.github.hadron13.gearbox.blocks.laser;

import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.register.data.ModDamageTypes;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Laser {
    public int color;
    public Vec3 position;
    public Vec3 rotation;
    public float power;
    public float length;


    public int breakTimer = 0;
    public List<Entity> caughtEntities = new ArrayList<>();

    public Laser(){
        color = 0;
        position = Vec3.ZERO;
        rotation = Vec3.ZERO;
    }

    public Laser(int color, Vec3 position, Vec3 rotation) {
        this.color = color;
        this.position = position;
        this.rotation = rotation;
        this.power = 2f;
    }

    public void tick(Level level){
        BlockHitResult block = level.clip(new ClipContext(position, position.add(rotation.scale(1000f)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
        if(block.getType() != HitResult.Type.MISS){

            BlockState blockState = level.getBlockState(block.getBlockPos());
            boolean catchesFire = blockState.isFlammable(level, block.getBlockPos(), block.getDirection());

            float hardness = blockState.getDestroySpeed(level, block.getBlockPos());
            boolean canBreak = hardness > -1 && hardness < power;

            if(!canBreak && !catchesFire) {
                breakTimer = 0;
            }else {
                breakTimer++;
                if(level.isClientSide){
                    Vec3 opposite= rotation.reverse();
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
        }

        AABB aabb = new AABB(position, position.add(rotation.scale(100f))).inflate(1.0); // Expand AABB slightly to catch entities
        List<Entity> entities = level.getEntities((Entity) null, aabb, entity -> entity.isAlive() && entity.isPickable());


        for (Entity entity : entities) {
            // Get the entity's bounding box, expanded by its collision border
            AABB entityAABB = entity.getBoundingBox().inflate(entity.getPickRadius());
            // Check if the ray intersects the entity's bounding box
            if (entityAABB.clip(position, position.add(rotation.scale(100f))).isPresent()) {
                entity.hurt(new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ModDamageTypes.laser)), power * 2);
                entity.setSecondsOnFire(3);
            }
        }

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

    public Vec3 getRotation() {
        return rotation;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public CompoundTag write(CompoundTag nbt, String prefix) {
        nbt.putInt(prefix + "color", color);
        nbt.putFloat(prefix + "length", length);
        nbt.put(prefix + "position", writeVec3(position));
        nbt.put(prefix + "rotation", writeVec3(rotation));
        return nbt;
    }

    public void read(CompoundTag nbt, String prefix) {
        color = nbt.getInt(prefix + "color");
        length = nbt.getFloat(prefix + "length");
        position = readVec3(nbt.getList(prefix + "position", Tag.TAG_INT));
        rotation = readVec3(nbt.getList(prefix + "rotation", Tag.TAG_INT));
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
