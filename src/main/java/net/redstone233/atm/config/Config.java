package net.redstone233.atm.config;

import java.util.Arrays;
import java.util.List;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.redstone233.atm.AnnouncementTestModClient;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // 显示设置
    public static final ModConfigSpec.BooleanValue SHOW_ON_WORLD_ENTER = BUILDER
            .comment("是否在进入世界时显示公告")
            .define("showOnWorldEnter", true);

    public static final ModConfigSpec.ConfigValue<String> LAST_DISPLAYED_HASH = BUILDER
            .comment("上次显示公告的哈希值（用于检测公告是否已修改）")
            .define("lastDisplayedHash", "");

    // 标题设置
    public static final ModConfigSpec.ConfigValue<String> MAIN_TITLE = BUILDER
            .comment("主标题文本")
            .define("mainTitle", "模组公告");

    public static final ModConfigSpec.ConfigValue<String> SUB_TITLE = BUILDER
            .comment("副标题文本")
            .define("subTitle", "请仔细观看");

    // 按钮设置
    public static final ModConfigSpec.ConfigValue<String> CONFIRM_BUTTON_TEXT = BUILDER
            .comment("确定按钮文本")
            .define("confirmButtonText", "确定");

    public static final ModConfigSpec.ConfigValue<String> SUBMIT_BUTTON_TEXT = BUILDER
            .comment("前往投递按钮文本")
            .define("submitButtonText", "前往投递");

    public static final ModConfigSpec.ConfigValue<String> BUTTON_LINK = BUILDER
            .comment("按钮点击后打开的链接")
            .define("buttonLink", "https://github.com/Redstone2337/TestMod-1.21.7-fabric-master/issues");

    // 滚动设置
    public static final ModConfigSpec.IntValue SCROLL_SPEED = BUILDER
            .comment("文本滚动速度 (像素/秒)")
            .defineInRange("scrollSpeed", 30, 10, 100);

    // 颜色设置
    public static final ModConfigSpec.IntValue MAIN_TITLE_COLOR = BUILDER
            .comment("主标题颜色 (RGB整数，十六进制格式如0xFFD700)")
            .defineInRange("mainTitleColor", 0xFFD700, 0x000000, 0xFFFFFF);

    public static final ModConfigSpec.IntValue SUB_TITLE_COLOR = BUILDER
            .comment("副标题颜色 (RGB整数，十六进制格式如0xFFFFFF)")
            .defineInRange("subTitleColor", 0xFFFFFF, 0x000000, 0xFFFFFF);

    public static final ModConfigSpec.IntValue CONTENT_COLOR = BUILDER
            .comment("公告内容颜色 (RGB整数，十六进制格式如0x0610EA)")
            .defineInRange("contentColor", 0x0610EA, 0x000000, 0xFFFFFF);

    // 调试设置
    public static final ModConfigSpec.BooleanValue DEBUG_MODE = BUILDER
            .comment("启用调试模式（显示UI边界等辅助信息）")
            .define("debugMode", false);

    // 图标设置
    public static final ModConfigSpec.BooleanValue SHOW_ICON = BUILDER
            .comment("是否显示公告图标")
            .define("showIcon", false);

    public static final ModConfigSpec.ConfigValue<String> ICON_PATH = BUILDER
            .comment("图标资源路径 (例如: testmod:textures/gui/announcement_icon.png)")
            .define("iconPath", "mtc:textures/gui/announcement_icon.png");

    public static final ModConfigSpec.IntValue ICON_WIDTH = BUILDER
            .comment("图标宽度 (像素)")
            .defineInRange("iconWidth", 32, 16, 1024);

    public static final ModConfigSpec.IntValue ICON_HEIGHT = BUILDER
            .comment("图标高度 (像素)")
            .defineInRange("iconHeight", 32, 16, 1024);

    public static final ModConfigSpec.IntValue ICON_TEXT_SPACING = BUILDER
            .comment("图标与文本的间距 (像素)")
            .defineInRange("iconTextSpacing", 10, 0, 50);

    // 颜色模式
    public static final ModConfigSpec.BooleanValue USE_CUSTOM_RGB = BUILDER
            .comment("是否使用自定义RGB颜色（启用后将隐藏Formatting枚举颜色选择）")
            .define("useCustomRGB", false);

    // 背景设置
    public static final ModConfigSpec.BooleanValue USE_CUSTOM_ANNOUNCEMENT_BACKGROUND = BUILDER
            .comment("是否使用自定义公告背景图")
            .define("useCustomAnnouncementBackground", false);

    public static final ModConfigSpec.ConfigValue<String> ANNOUNCEMENT_BACKGROUND_PATH = BUILDER
            .comment("公告背景图路径 (例如: testmod:textures/gui/announcement_background.png)")
            .define("announcementBackgroundPath", "testmod:textures/gui/announcement_background.png");

    // 公告内容 - 使用列表
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ANNOUNCEMENT_CONTENT = BUILDER
            .comment("公告内容（每行一条，支持多行）")
            .defineListAllowEmpty("announcementContent",
                    Arrays.asList(
                            "§a欢迎游玩，我们团队做的模组！",
                            " ",
                            "§e一些提醒：",
                            "§f1. 模组仅限于1.21.7~1.21.8neoforge",
                            "§f2. 模组目前是半成品",
                            "§f3. 后面会继续更新",
                            " ",
                            "§b模组随缘更新",
                            "§c若发现bug可以向模组作者或者仓库反馈！"
                    ),
                    Config::validateString);

    public static final ModConfigSpec SPEC = BUILDER.build();

    // 验证方法
    private static boolean validateString(final Object obj) {
        return obj instanceof String;
    }

    // 配置加载事件处理
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) {
        validateConfig();
    }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) {
        validateConfig();
    }

    // 配置验证
    private static void validateConfig() {
        // 这里可以添加配置加载后的验证逻辑
        // 例如：同步调试模式到客户端

        AnnouncementTestModClient.DEBUG_MODE = DEBUG_MODE.get();
    }
}
