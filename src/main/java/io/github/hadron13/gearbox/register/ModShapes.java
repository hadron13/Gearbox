package io.github.hadron13.gearbox.register;


import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

import static net.minecraft.core.Direction.*;

public class ModShapes {


    public static final VoxelShaper
        SAPPER = shape(0, 0, 0, 32, 16, 16).forHorizontal(WEST),
        SMALL_LASER = shape(3, 0, 0, 13, 12, 16).forHorizontal(NORTH),
        LASER_DRILL = shape(0, -16, 0, 16, 16, 16).forAxis();

    private static ModShapes.Builder shape(VoxelShape shape) {
        return new ModShapes.Builder(shape);
    }

    private static ModShapes.Builder shape(double x1, double y1, double z1, double x2, double y2, double z2) {
        return shape(cuboid(x1, y1, z1, x2, y2, z2));
    }

    private static VoxelShape cuboid(double x1, double y1, double z1, double x2, double y2, double z2) {
        return Block.box(x1, y1, z1, x2, y2, z2);
    }

    public static class Builder {

        private VoxelShape shape;

        public Builder(VoxelShape shape) {
            this.shape = shape;
        }

        public ModShapes.Builder add(VoxelShape shape) {
            this.shape = Shapes.or(this.shape, shape);
            return this;
        }

        public ModShapes.Builder add(double x1, double y1, double z1, double x2, double y2, double z2) {
            return add(cuboid(x1, y1, z1, x2, y2, z2));
        }

        public ModShapes.Builder erase(double x1, double y1, double z1, double x2, double y2, double z2) {
            this.shape = Shapes.join(shape, cuboid(x1, y1, z1, x2, y2, z2), BooleanOp.ONLY_FIRST);
            return this;
        }

        public VoxelShape build() {
            return shape;
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction, VoxelShaper> factory, Direction direction) {
            return factory.apply(shape, direction);
        }

        public VoxelShaper build(BiFunction<VoxelShape, Direction.Axis, VoxelShaper> factory, Direction.Axis axis) {
            return factory.apply(shape, axis);
        }

        public VoxelShaper forDirectional(Direction direction) {
            return build(VoxelShaper::forDirectional, direction);
        }

        public VoxelShaper forAxis() {
            return build(VoxelShaper::forAxis, Direction.Axis.Y);
        }

        public VoxelShaper forHorizontalAxis() {
            return build(VoxelShaper::forHorizontalAxis, Direction.Axis.Z);
        }

        public VoxelShaper forHorizontal(Direction direction) {
            return build(VoxelShaper::forHorizontal, direction);
        }

        public VoxelShaper forDirectional() {
            return forDirectional(UP);
        }

    }

}
