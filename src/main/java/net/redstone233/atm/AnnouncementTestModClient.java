package net.redstone233.atm;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.redstone233.atm.config.ConfigManager;
import net.redstone233.atm.keys.ModKeys;
import net.redstone233.atm.screen.AnnouncementScreen;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AnnouncementTestMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AnnouncementTestMod.MOD_ID, value = Dist.CLIENT)
public class AnnouncementTestModClient {
    public static boolean DEBUG_MODE = false;
    public static boolean SHOW_ICON = true;


    public AnnouncementTestModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        AnnouncementTestMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        AnnouncementTestMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        DEBUG_MODE = ConfigManager.isDebugMode();
    }

    // 添加客户端tick事件处理，用于检测按键
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        // 确保在游戏世界中且没有打开GUI
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        // 检查公告键是否被按下
        if (ModKeys.ANNOUNCEMENT_KEY.consumeClick()) {
            // 打开公告屏幕
            minecraft.setScreen(new AnnouncementScreen());
        }
    }
}
