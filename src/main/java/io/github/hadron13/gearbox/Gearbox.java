package io.github.hadron13.gearbox;

import com.mojang.logging.LogUtils;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.hadron13.gearbox.config.GearboxConfig;
import io.github.hadron13.gearbox.data.GearboxDatagen;
import io.github.hadron13.gearbox.ponder.GearboxPonderPlugin;
import io.github.hadron13.gearbox.register.*;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gearbox.MODID)
public class Gearbox {

    public static final String MODID = "gearbox";
    public static final String DISPLAY_NAME = "Gearbox";
    public static boolean oculusLoaded = false;
    public static boolean adlodsLoaded = false;

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE
                .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
                .setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }


    public Gearbox(IEventBus modEventBus, ModContainer modContainer) {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();


        REGISTRATE.registerEventListeners(modEventBus);

        GearboxCreativeModeTabs.register(modEventBus);
        GearboxBlocks.register();
        GearboxItems.register();
        GearboxBlockEntities.register();
        GearboxFluids.register();
        GearboxPartialModels.init();
        GearboxRecipeTypes.register(modEventBus);
        GearboxConfig.register(modLoadingContext, modContainer);

        modEventBus.addListener(EventPriority.HIGHEST, GearboxDatagen::gatherDataHighPriority);
        modEventBus.addListener(EventPriority.LOWEST, GearboxDatagen::gatherData);

//        NeoForge.EVENT_BUS.register(this);

        oculusLoaded = ModList.get().isLoaded("oculus");
        adlodsLoaded = ModList.get().isLoaded("adlods");

    }




    public static void clientInit(final FMLClientSetupEvent event){
    }

    public static CreateRegistrate registrate(){
        return REGISTRATE;
    }


    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
