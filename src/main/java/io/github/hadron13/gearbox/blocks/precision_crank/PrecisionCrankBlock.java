package io.github.hadron13.gearbox.blocks.precision_crank;

import com.simibubi.create.content.kinetics.crank.HandCrankBlock;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import io.github.hadron13.gearbox.register.GearboxBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class PrecisionCrankBlock extends HandCrankBlock {
    public PrecisionCrankBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getRotationSpeed() {
        return 1;
    }

    @Override
    public BlockEntityType<? extends HandCrankBlockEntity> getBlockEntityType() {
        return GearboxBlockEntities.PRECISION_CRANK.get();
    }
}
