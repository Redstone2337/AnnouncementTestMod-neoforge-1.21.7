package net.redstone233.atm.config;

import net.minecraft.ChatFormatting;
import net.redstone233.atm.AnnouncementTestMod;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    public static String getMainTitle() {
        return Config.MAIN_TITLE.get();
    }

    public static String getSubTitle() {
        return Config.SUB_TITLE.get();
    }

    public static List<String> getAnnouncementContent() {
        try {
            // 安全地转换类型
            List<?> rawList = Config.ANNOUNCEMENT_CONTENT.get();
            List<String> result = new ArrayList<>();

            for (Object item : rawList) {
                if (item instanceof String) {
                    result.add((String) item);
                }
            }

            return result;
        } catch (Exception e) {
            AnnouncementTestMod.LOGGER.warn("获取公告内容时出错，使用默认值", e);
            return getDefaultAnnouncementContent();
        }
    }

    private static List<String> getDefaultAnnouncementContent() {
        return List.of(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        );
    }

    public static int getMainTitleColor() {
        return Config.MAIN_TITLE_COLOR.get();
    }

    public static int getSubTitleColor() {
        return Config.SUB_TITLE_COLOR.get();
    }

    public static int getContentColor() {
        return Config.CONTENT_COLOR.get();
    }

    public static boolean shouldShowOnWorldEnter() {
        return Config.SHOW_ON_WORLD_ENTER.get();
    }

    public static boolean isDebugMode() {
        return Config.DEBUG_MODE.get();
    }

    public static boolean shouldShowIcon() {
        return Config.SHOW_ICON.get();
    }

    public static String getIconPath() {
        return Config.ICON_PATH.get();
    }

    public static int getIconWidth() {
        return Config.ICON_WIDTH.get();
    }

    public static int getIconHeight() {
        return Config.ICON_HEIGHT.get();
    }

    public static int getIconTextSpacing() {
        return Config.ICON_TEXT_SPACING.get();
    }

    public static int getScrollSpeed() {
        return Config.SCROLL_SPEED.get();
    }

    public static String getConfirmButtonText() {
        return Config.CONFIRM_BUTTON_TEXT.get();
    }

    public static String getSubmitButtonText() {
        return Config.SUBMIT_BUTTON_TEXT.get();
    }

    public static String getButtonLink() {
        return Config.BUTTON_LINK.get();
    }

    public static boolean useCustomRGB() {
        return Config.USE_CUSTOM_RGB.get();
    }

    public static boolean useCustomAnnouncementBackground() {
        return Config.USE_CUSTOM_ANNOUNCEMENT_BACKGROUND.get();
    }

    public static String getAnnouncementBackgroundPath() {
        return Config.ANNOUNCEMENT_BACKGROUND_PATH.get();
    }

    public static String getLastDisplayedHash() {
        return Config.LAST_DISPLAYED_HASH.get();
    }

    public static void setLastDisplayedHash(String hash) {
        // 注意：在NeoForge中，直接修改配置值比较复杂
        // 通常需要通过配置文件或配置屏幕来修改
    }

    // 辅助方法：从颜色值获取ChatFormatting枚举
    public static ChatFormatting getChatFormattingFromColor(int color) {
        int rgbColor = color & 0xFFFFFF;

        for (ChatFormatting ChatFormatting : ChatFormatting.values()) {
            if (ChatFormatting.isColor() && ChatFormatting.getColor() != null) {
                int ChatFormattingColor = ChatFormatting.getColor() & 0xFFFFFF;
                if (ChatFormattingColor == rgbColor) {
                    return ChatFormatting;
                }
            }
        }
        return ChatFormatting.WHITE;
    }

    // 辅助方法：从ChatFormatting枚举获取颜色值
    public static int getColorFromChatFormatting(ChatFormatting ChatFormatting) {
        return ChatFormatting.getColor() != null ? ChatFormatting.getColor() : 0xFFFFFF;
    }
}
