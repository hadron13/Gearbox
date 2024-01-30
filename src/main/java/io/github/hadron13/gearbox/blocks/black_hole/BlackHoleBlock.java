package io.github.hadron13.gearbox.blocks.black_hole;

import com.simibubi.create.foundation.block.IBE;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BlackHoleBlock extends Block implements IBE<BlackHoleBlockEntity> {
    public BlackHoleBlock(Properties pProperties) {
        super(pProperties);
    }



    @Override
    public Class<BlackHoleBlockEntity> getBlockEntityClass() {
        return BlackHoleBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BlackHoleBlockEntity> getBlockEntityType() {
        return ModBlockEntities.BLACK_HOLE.get();
    }
}
