package io.github.hadron13.gearbox;

import io.github.hadron13.gearbox.blocks.amplifier.AmplifierBlockEntity;
import io.github.hadron13.gearbox.blocks.centrifuge.CentrifugeBlockEntity;
import io.github.hadron13.gearbox.blocks.compressor.CompressorBlockEntity;
import io.github.hadron13.gearbox.blocks.core_drill.CoreDrillBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationControllerBlockEntity;
import io.github.hadron13.gearbox.blocks.distillation_tower.DistillationOutputBlockEntity;
import io.github.hadron13.gearbox.blocks.electrolyzer.ElectrolyzerBlockEntity;
import io.github.hadron13.gearbox.blocks.kiln.KilnBlockEntity;
import io.github.hadron13.gearbox.blocks.laser.LaserBlockEntity;
import io.github.hadron13.gearbox.blocks.pumpjack.PumpjackWellBlockEntity;
import io.github.hadron13.gearbox.blocks.sapper.SapperBlockEntity;
import io.github.hadron13.gearbox.blocks.steel_tank.SteelTankBlockEntity;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber
public class GearboxCommonEvents {
    @net.neoforged.bus.api.SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        AmplifierBlockEntity.registerCapabilities(event);
        CentrifugeBlockEntity.registerCapabilities(event);
        CompressorBlockEntity.registerCapabilities(event);
        CoreDrillBlockEntity.registerCapabilities(event);
        DistillationControllerBlockEntity.registerCapabilities(event);
        DistillationOutputBlockEntity.registerCapabilities(event);
        ElectrolyzerBlockEntity.registerCapabilities(event);
        KilnBlockEntity.registerCapabilities(event);
        LaserBlockEntity.registerCapabilities(event);
        PumpjackWellBlockEntity.registerCapabilities(event);
        SapperBlockEntity.registerCapabilities(event);
        SteelTankBlockEntity.registerCapabilities(event);
    }
}
