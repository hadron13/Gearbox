package io.github.hadron13.gearbox;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.hadron13.gearbox.ore_counter.CapabilityOreCounter;
import io.github.hadron13.gearbox.ore_counter.IOreCountHandler;
import io.github.hadron13.gearbox.ore_counter.OreCountHandler;
import io.github.hadron13.gearbox.config.GearboxConfig;
import io.github.hadron13.gearbox.data.GearboxDatagen;
import io.github.hadron13.gearbox.network.PacketHandler;
import io.github.hadron13.gearbox.ponder.GearboxPonderPlugin;
import io.github.hadron13.gearbox.register.*;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Gearbox.MODID)
public class Gearbox {

    public static final String MODID = "gearbox";
    public static final String DISPLAY_NAME = "Gearbox";
    public static boolean oculusLoaded = false;

    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static IEventBus modEventBus;
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setTooltipModifierFactory((item) -> (new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)).andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public Gearbox() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();
        modEventBus = FMLJavaModLoadingContext.get()
                .getModEventBus();

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);

        ModCreativeTabs.register(modEventBus);
        ModBlocks.register();
        ModItems.register();
        ModBlockEntities.register();
        ModFluids.register();
        ModPartialModels.init();
        ModRecipeTypes.register(modEventBus);

        PacketHandler.register();

        GearboxConfig.register(modLoadingContext);

        modEventBus.addListener(EventPriority.LOWEST, GearboxDatagen::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(Gearbox::clientInit) );

        MinecraftForge.EVENT_BUS.register(this);

        oculusLoaded = ModList.get().isLoaded("oculus");
    }

    public static void clientInit(final FMLClientSetupEvent event){

        PonderIndex.addPlugin(new GearboxPonderPlugin());

    }

    public static CreateRegistrate registrate(){
        return REGISTRATE;
    }


    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }

    @SubscribeEvent
    public void registerCapability(RegisterCapabilitiesEvent event){
        CapabilityOreCounter.register(event);
    }

    @SubscribeEvent
    public void attachCapability(final AttachCapabilitiesEvent<LevelChunk> event){

        OreCountHandler backend = new OreCountHandler(event.getObject());
        LazyOptional<IOreCountHandler> optionalStorage = LazyOptional.of(()->backend);

        ICapabilityProvider provider = new ICapabilitySerializable<CompoundTag>() {
            @NotNull
            @Override
            public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if(cap==CapabilityOreCounter.COUNTER){
                    return optionalStorage.cast();
                }else{
                    return LazyOptional.empty();
                }
            }

            @Override
            public CompoundTag serializeNBT() {
                return backend.serializeNBT();
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                backend.deserializeNBT(nbt);
            }
        };

        event.addCapability(asResource("ore_counter_capability"), provider);
    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
