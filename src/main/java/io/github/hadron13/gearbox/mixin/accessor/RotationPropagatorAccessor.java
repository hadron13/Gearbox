package io.github.hadron13.gearbox.mixin.accessor;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import com.simibubi.create.content.kinetics.RotationPropagator;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;

import net.minecraft.core.Direction;
//  Stolen carefully  from
//
//  -* Petrolpark *-
//
//  link: https://github.com/petrolpark/Destroy/blob/1.20.1/src/main/java/com/petrolpark/destroy/mixin/accessor/RotationPropagatorAccessor.java
@Mixin(RotationPropagator.class)
public interface RotationPropagatorAccessor {

    @Invoker(
            value = "getAxisModifier",
            remap = false
    )
    public static float invokeGetAxisModifier(KineticBlockEntity be, Direction direction) {
        return 0f; // This return value is ignored.
    };
};