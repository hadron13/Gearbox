package io.github.hadron13.gearbox;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.TagGen;
import io.github.hadron13.gearbox.ponder.ModPonderIndex;
import io.github.hadron13.gearbox.ponder.ModPonderTags;
import io.github.hadron13.gearbox.register.*;
import io.github.hadron13.gearbox.groups.ModGroup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Scanner;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gearbox.MODID)
public class Gearbox {

    public static final String MODID = "gearbox";
    public static final String DISPLAY_NAME = "Gearbox";

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public Gearbox() {
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        REGISTRATE.registerEventListeners(modEventBus);

        new ModGroup("main");

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModFluids.register();
        ModPartialModels.init();
        ModRecipeTypes.register(modEventBus);

        modEventBus.addListener(EventPriority.LOWEST, Gearbox::gatherData);


        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(Gearbox::clientInit) );

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void clientInit(final FMLClientSetupEvent event){

        ModPonderTags.register();
        ModPonderIndex.register();

    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
    }

    public static CreateRegistrate registrate(){
        return REGISTRATE;
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
