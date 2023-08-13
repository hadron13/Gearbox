package io.github.hadron13.gearbox;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import io.github.hadron13.gearbox.register.ModBlockEntities;
import io.github.hadron13.gearbox.register.ModBlocks;
import io.github.hadron13.gearbox.groups.ModGroup;
import io.github.hadron13.gearbox.register.ModItems;
import io.github.hadron13.gearbox.register.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

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
        // Register the setup method for modloading
        modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        new ModGroup("main");

        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModRecipeTypes.register(modEventBus);


        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static CreateRegistrate registrate(){
        return REGISTRATE;
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code

    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
    }
}
