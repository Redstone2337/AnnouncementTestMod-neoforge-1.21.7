package net.redstone233.atm;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.redstone233.atm.config.v1.AnnouncementConfig;
import net.redstone233.atm.config.v1.ConfigManager;
import net.redstone233.atm.keys.ModKeys;
import net.redstone233.atm.screen.v1.AnnouncementScreen;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = AnnouncementTestMod.MOD_ID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = AnnouncementTestMod.MOD_ID, value = Dist.CLIENT)
public class AnnouncementTestModClient {
    public static boolean DEBUG_MODE = false;

    // 客户端配置实例
    private static AnnouncementConfig clientConfig;

    public AnnouncementTestModClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        // 初始化客户端配置
        initializeClientConfig();
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        AnnouncementTestMod.LOGGER.info("开始初始化公告模组客户端...");
        long startTime = System.currentTimeMillis();
        AnnouncementTestMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        AnnouncementTestMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

        // 更新调试模式状态
        updateDebugMode();

        // 确保客户端配置已初始化
        initializeClientConfig();

        AnnouncementTestMod.LOGGER.info("客户端初始化完成，总耗时 {}ms", System.currentTimeMillis() - startTime);
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
            // 打开公告屏幕，使用客户端配置
            openAnnouncementScreen();
        }
    }

    /**
     * 初始化客户端配置
     */
    private static void initializeClientConfig() {
        try {
            clientConfig = net.redstone233.atm.config.v1.ConfigManager.createAnnouncementConfig();
            if (DEBUG_MODE) {
                AnnouncementTestMod.LOGGER.info("客户端配置初始化完成");
                AnnouncementTestMod.LOGGER.info("主标题: {}", clientConfig.mainTitle);
                AnnouncementTestMod.LOGGER.info("副标题: {}", clientConfig.subTitle);
                AnnouncementTestMod.LOGGER.info("公告行数: {}", clientConfig.announcementContent.size());
            }
        } catch (Exception e) {
            AnnouncementTestMod.LOGGER.error("初始化客户端配置失败", e);
            // 创建默认配置作为回退
            clientConfig = createDefaultConfig();
        }
    }

    /**
     * 更新调试模式状态
     */
    private static void updateDebugMode() {
        DEBUG_MODE = ConfigManager.isDebugMode();
        if (DEBUG_MODE) {
            AnnouncementTestMod.LOGGER.info("调试模式已启用");
        }
    }

    /**
     * 打开公告屏幕
     */
    private static void openAnnouncementScreen() {
        try {
            Minecraft minecraft = Minecraft.getInstance();

            // 确保配置是最新的
            updateDebugMode();
            initializeClientConfig();

            // 创建并显示公告屏幕
            AnnouncementScreen screen = new AnnouncementScreen(clientConfig);
            minecraft.setScreen(screen);

            if (DEBUG_MODE) {
                AnnouncementTestMod.LOGGER.info("公告屏幕已打开");
            }
        } catch (Exception e) {
            AnnouncementTestMod.LOGGER.error("打开公告屏幕失败", e);

            // 显示错误消息给玩家
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(
                        net.minecraft.network.chat.Component.literal("打开公告屏幕时发生错误，请查看日志"),
                        false
                );
            }
        }
    }

    /**
     * 创建默认配置（回退方案）
     */
    private static AnnouncementConfig createDefaultConfig() {
        AnnouncementConfig config = new AnnouncementConfig();

        // 设置默认值
        config.mainTitle = "服务器公告";
        config.subTitle = "最新通知";
        config.announcementContent = java.util.List.of(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        );
        config.confirmButtonText = "确定";
        config.submitButtonText = "前往投递";
        config.buttonLink = "https://example.com";
        config.showIcon = false;
        config.iconPath = "";
        config.iconWidth = 32;
        config.iconHeight = 32;
        config.iconTextSpacing = 10;
        config.useCustomRGB = false;
        config.mainTitleColor = 0xFFFFFF;
        config.subTitleColor = 0xCCCCCC;
        config.contentColor = 0xFFFFFF;
        config.scrollSpeed = 1.0;
        config.useCustomAnnouncementBackground = false;
        config.announcementBackgroundPath = "";

        return config;
    }

    /**
     * 获取客户端配置实例
     */
    public static AnnouncementConfig getClientConfig() {
        if (clientConfig == null) {
            initializeClientConfig();
        }
        return clientConfig;
    }

    /**
     * 重新加载客户端配置
     */
    public static void reloadClientConfig() {
        initializeClientConfig();
        updateDebugMode();
        AnnouncementTestMod.LOGGER.info("客户端配置已重新加载");
    }
}