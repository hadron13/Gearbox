package io.github.hadron13.gearbox.mixin.accessor;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = BasinBlockEntity.class, remap = false)
public interface BasinBlockEntityAccessor {
    @Invoker("getHeatLevel")
    @NotNull BlazeBurnerBlock.HeatLevel getHeatLevelAccessor();
}
