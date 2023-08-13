package io.github.hadron13.gearbox.register;


import com.tterrag.registrate.util.entry.BlockEntityEntry;
import io.github.hadron13.gearbox.Gearbox;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerBlockEntity;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerInstance;
import io.github.hadron13.gearbox.blocks.exchanger.ExchangerRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.KilnRenderer;
import io.github.hadron13.gearbox.blocks.kiln.KilnInstance;

public class ModBlockEntities {
    public static final BlockEntityEntry<ExchangerBlockEntity> EXCHANGER = Gearbox.registrate()
            .blockEntity("exchanger", ExchangerBlockEntity::new)
            .instance(() -> ExchangerInstance::new)
            .validBlocks(ModBlocks.EXCHANGER)
            .renderer(() -> ExchangerRenderer::new)
            .register();

    public static final BlockEntityEntry<KilnBlockEntity> KILN = Gearbox.registrate()
            .blockEntity("kiln", KilnBlockEntity::new)
            .instance(() -> KilnInstance::new)
            .validBlocks(ModBlocks.KILN)
            .renderer(() -> KilnRenderer::new)
            .register();
    public static void register() {}
}