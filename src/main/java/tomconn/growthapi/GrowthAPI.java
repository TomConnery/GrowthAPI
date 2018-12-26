package tomconn.growthapi;

import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tomconn.growthapi.implementations.EventManager;
import tomconn.growthapi.interfaces.registry.IRegistry;
import tomconn.growthapi.runtimetests.RuntimeTest;

@SuppressWarnings("WeakerAccess")
@Mod(modid = GrowthAPI.modId, name = GrowthAPI.name, version = GrowthAPI.version)
@Mod.EventBusSubscriber
public class GrowthAPI {

    public final static String modId = "growthapi";
    public final static String name = "Growth API";
    public final static String version = "0.0.3";


    private static EventManager eventManager;
    private static IRegistry registry;


    @Mod.Instance(modId)
    public static GrowthAPI instance;

    public static IRegistry getRegistry() {
        return registry;
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        eventManager = new EventManager();
        registry = eventManager.getRegistry();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        RuntimeTest.test();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCropGrowthEventPre(BlockEvent.CropGrowEvent.Pre event) {
        eventManager.manage(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onCropGrowthEventPost(BlockEvent.CropGrowEvent.Post event) {
        eventManager.manage(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
        eventManager.manage(event);
    }
}
