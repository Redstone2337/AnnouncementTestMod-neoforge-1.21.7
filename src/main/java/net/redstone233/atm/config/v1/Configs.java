package net.redstone233.atm.config.v1;

import java.util.Arrays;
import java.util.List;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Configs {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // ==================== 通用设置 ====================
    private static void setupGeneralSettings(ModConfigSpec.Builder builder) {
        builder.push("general");

        SHOW_ON_WORLD_ENTER = builder
                .comment("是否在进入世界时显示公告")
                .define("showOnWorldEnter", true);

        DEBUG_MODE = builder
                .comment("启用调试模式（显示UI边界等辅助信息）")
                .define("debugMode", false);

        builder.pop();
    }

    // ==================== 显示设置 ====================
    private static void setupDisplaySettings(ModConfigSpec.Builder builder) {
        builder.push("display");

        MAIN_TITLE = builder
                .comment("主标题文本")
                .define("mainTitle", "服务器公告");

        SUB_TITLE = builder
                .comment("副标题文本")
                .define("subTitle", "最新通知");

        SCROLL_SPEED = builder
                .comment("文本滚动速度")
                .defineInRange("scrollSpeed", 1, 1, 10);

        builder.pop();
    }

    // ==================== 颜色设置 ====================
    private static void setupColorSettings(ModConfigSpec.Builder builder) {
        builder.push("colors");

        USE_CUSTOM_RGB = builder
                .comment("是否使用自定义RGB颜色")
                .define("useCustomRGB", false);

        MAIN_TITLE_COLOR = builder
                .comment("主标题颜色 (RGB整数，十六进制格式如0xFFFFFF)")
                .defineInRange("mainTitleColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

        SUB_TITLE_COLOR = builder
                .comment("副标题颜色 (RGB整数，十六进制格式如0xCCCCCC)")
                .defineInRange("subTitleColor", 0xCCCCCC, 0x000000, 0xFFFFFF);

        CONTENT_COLOR = builder
                .comment("公告内容颜色 (RGB整数，十六进制格式如0xFFFFFF)")
                .defineInRange("contentColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

        builder.pop();
    }

    // ==================== 按钮设置 ====================
    private static void setupButtonSettings(ModConfigSpec.Builder builder) {
        builder.push("buttons");

        CONFIRM_BUTTON_TEXT = builder
                .comment("确定按钮文本")
                .define("confirmButtonText", "确定");

        SUBMIT_BUTTON_TEXT = builder
                .comment("前往投递按钮文本")
                .define("submitButtonText", "前往投递");

        BUTTON_LINK = builder
                .comment("按钮点击后打开的链接")
                .define("buttonLink", "https://github.com/Redstone2337/TestMod-1.21.7-fabric-master/issues");

        builder.pop();
    }

    // ==================== 图标设置 ====================
    private static void setupIconSettings(ModConfigSpec.Builder builder) {
        builder.push("icon");

        SHOW_ICON = builder
                .comment("是否显示公告图标")
                .define("showIcon", false);

        ICON_PATH = builder
                .comment("图标资源路径")
                .define("path", "announcement_mod:textures/gui/icon.png");

        ICON_WIDTH = builder
                .comment("图标宽度 (像素)")
                .defineInRange("width", 32, 16, 128);

        ICON_HEIGHT = builder
                .comment("图标高度 (像素)")
                .defineInRange("height", 32, 16, 128);

        ICON_TEXT_SPACING = builder
                .comment("图标与文本的间距 (像素)")
                .defineInRange("textSpacing", 10, 0, 50);

        builder.pop();
    }

    // ==================== 背景设置 ====================
    private static void setupBackgroundSettings(ModConfigSpec.Builder builder) {
        builder.push("background");

        USE_CUSTOM_ANNOUNCEMENT_BACKGROUND = builder
                .comment("是否使用自定义公告背景图")
                .define("enabled", false);

        ANNOUNCEMENT_BACKGROUND_PATH = builder
                .comment("公告背景图路径")
                .define("path", "announcement_mod:textures/gui/background.png");

        builder.pop();
    }

    // ==================== 内容设置 ====================
    private static void setupContentSettings(ModConfigSpec.Builder builder) {
        builder.push("content");

        ANNOUNCEMENT_CONTENT = builder
                .comment("公告内容（每行一条，支持多行）")
                .defineListAllowEmpty("lines",
                        Arrays.asList(
                                "§a欢迎游玩，我们团队做的模组！",
                                " ",
                                "§e一些提醒：",
                                "§f1. 模组仅限于1.21.7~1.21.8 NeoForge",
                                "§f2. 模组目前是半成品",
                                "§f3. 后面会继续更新",
                                " ",
                                "§b模组随缘更新",
                                "§c若发现bug可以向模组作者或者仓库反馈！"
                        ),
                        Configs::validateString);

        LAST_DISPLAYED_HASH = builder
                .comment("上次显示公告的哈希值（用于检测公告是否已修改）")
                .define("lastDisplayedHash", "");

        builder.pop();
    }

    // 配置字段声明
    public static ModConfigSpec.BooleanValue SHOW_ON_WORLD_ENTER;
    public static ModConfigSpec.BooleanValue DEBUG_MODE;
    public static ModConfigSpec.ConfigValue<String> MAIN_TITLE;
    public static ModConfigSpec.ConfigValue<String> SUB_TITLE;
    public static ModConfigSpec.IntValue SCROLL_SPEED;
    public static ModConfigSpec.BooleanValue USE_CUSTOM_RGB;
    public static ModConfigSpec.IntValue MAIN_TITLE_COLOR;
    public static ModConfigSpec.IntValue SUB_TITLE_COLOR;
    public static ModConfigSpec.IntValue CONTENT_COLOR;
    public static ModConfigSpec.ConfigValue<String> CONFIRM_BUTTON_TEXT;
    public static ModConfigSpec.ConfigValue<String> SUBMIT_BUTTON_TEXT;
    public static ModConfigSpec.ConfigValue<String> BUTTON_LINK;
    public static ModConfigSpec.BooleanValue SHOW_ICON;
    public static ModConfigSpec.ConfigValue<String> ICON_PATH;
    public static ModConfigSpec.IntValue ICON_WIDTH;
    public static ModConfigSpec.IntValue ICON_HEIGHT;
    public static ModConfigSpec.IntValue ICON_TEXT_SPACING;
    public static ModConfigSpec.BooleanValue USE_CUSTOM_ANNOUNCEMENT_BACKGROUND;
    public static ModConfigSpec.ConfigValue<String> ANNOUNCEMENT_BACKGROUND_PATH;
    public static ModConfigSpec.ConfigValue<List<? extends String>> ANNOUNCEMENT_CONTENT;
    public static ModConfigSpec.ConfigValue<String> LAST_DISPLAYED_HASH;

    static {
        // 按顺序初始化各个配置节
        setupGeneralSettings(BUILDER);
        setupDisplaySettings(BUILDER);
        setupColorSettings(BUILDER);
        setupButtonSettings(BUILDER);
        setupIconSettings(BUILDER);
        setupBackgroundSettings(BUILDER);
        setupContentSettings(BUILDER);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    // 验证方法
    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }

    // 初始化配置
    public static void init(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, SPEC, "announcement_mod-client.toml");

        // 注册配置事件
        modEventBus.addListener(Configs::onConfigLoad);
        modEventBus.addListener(Configs::onConfigReload);
    }

    // 配置加载事件处理
    public static void onConfigLoad(final ModConfigEvent.Loading configEvent) {
        validateConfig();
        System.out.println("Announcement Mod 配置加载完成");
    }

    public static void onConfigReload(final ModConfigEvent.Reloading configEvent) {
        validateConfig();
        System.out.println("Announcement Mod 配置重新加载");
    }

    // 配置验证
    private static void validateConfig() {
        // 验证颜色值在合理范围内
        if (MAIN_TITLE_COLOR.get() < 0 || MAIN_TITLE_COLOR.get() > 0xFFFFFF) {
            System.err.println("主标题颜色值超出范围");
        }

        if (SUB_TITLE_COLOR.get() < 0 || SUB_TITLE_COLOR.get() > 0xFFFFFF) {
            System.err.println("副标题颜色值超出范围");
        }

        if (CONTENT_COLOR.get() < 0 || CONTENT_COLOR.get() > 0xFFFFFF) {
            System.err.println("内容颜色值超出范围");
        }

        // 验证滚动速度
        if (SCROLL_SPEED.get() < 1 || SCROLL_SPEED.get() > 10) {
            System.err.println("滚动速度超出范围");
        }
    }
}