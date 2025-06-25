package io.github.hadron13.gearbox.blocks.laser;

import com.simibubi.create.content.kinetics.base.RotatingInstance;
import com.simibubi.create.foundation.utility.RaycastHelper;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import net.createmod.catnip.levelWrappers.RayTraceLevel;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Laser {
    public int color;
    public Vec3 position;
    public Vec3 rotation;
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
    }

    public void tick(){

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
