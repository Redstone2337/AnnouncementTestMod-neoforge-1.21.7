package net.redstone233.atm;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.redstone233.atm.command.AnnouncementCommand;
import net.redstone233.atm.component.types.ModComponentTypes;
import net.redstone233.atm.config.Config;
import net.redstone233.atm.config.v1.Configs;
import net.redstone233.atm.core.NetworkRegistry;
import net.redstone233.atm.event.ServerEventHandler;
import net.redstone233.atm.item.ModCreativeModeTabs;
import net.redstone233.atm.item.ModItems;
import net.redstone233.atm.keys.ModKeys;
import net.redstone233.atm.materials.ModToolMaterials;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(AnnouncementTestMod.MOD_ID)
public class AnnouncementTestMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "atm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private final ServerEventHandler serverEventHandler;

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public AnnouncementTestMod(IEventBus modEventBus, ModContainer modContainer, ServerEventHandler serverEventHandler) {
        LOGGER.info("开始初始化内容！");
        long startTime = System.currentTimeMillis();


        // 初始化服务器事件处理器
        this.serverEventHandler = new ServerEventHandler();
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // 注册物品和创造模式标签
        ModItems.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModComponentTypes.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (TutorialMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.

        // 注册事件总线
        // 注意：服务器事件处理器需要注册到 NeoForge 事件总线
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(this.serverEventHandler);


        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(NetworkRegistry::registerPayloadHandlers);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Configs.SPEC);


        // 注册网络系统
        modEventBus.addListener(NetworkRegistry::registerPayloadHandlers);
        LOGGER.info("网络系统注册完成");

        // 注册配置系统
        Configs.init(modEventBus, modContainer);
        LOGGER.info("配置系统初始化完成");

        LOGGER.info("模组初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        ModKeys.init();
        ModToolMaterials.init();
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(AnnouncementCommand.register());
    }
}
