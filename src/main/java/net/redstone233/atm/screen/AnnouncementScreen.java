package net.redstone233.atm.screen;

import net.minecraft.SharedConstants;
import net.redstone233.atm.AnnouncementTestMod;
import net.redstone233.atm.AnnouncementTestModClient;
import net.redstone233.atm.button.ScrollableTextWidget;
import net.redstone233.atm.config.ConfigManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.Util;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnnouncementScreen extends Screen {
    private ScrollableTextWidget scrollableText;
    private int tickCount = 0;
    private ResourceLocation iconTexture;
    private ResourceLocation backgroundTexture;

    private StringWidget titleWidget;
    private StringWidget subtitleWidget;

    public AnnouncementScreen() {
        super(Component.literal("Announcement Screen"));
    }

    @Override
    protected void init() {
        super.init();

        // 创建滚动文本部件
        int contentColor = ConfigManager.useCustomRGB() ? ConfigManager.getContentColor() : 0xFFFFFFFF;

        int centerX = this.width / 2;
        int buttonWidth = 100;
        int buttonHeight = 20;
        int buttonY = this.height - 30;

        // 版本兼容性检查
        if (!isVersionCompatible()) {
            addRenderableWidget(new StringWidget(centerX, 20, 300, 20,
                    Component.literal("此模组不兼容1.21.5及以下版本").withStyle(ChatFormatting.RED), this.font));
        }

        // 加载图标纹理
        if (ConfigManager.shouldShowIcon() && ConfigManager.getIconPath() != null && !ConfigManager.getIconPath().isEmpty()) {
            try {
                iconTexture = ResourceLocation.withDefaultNamespace(ConfigManager.getIconPath());
                // 检查纹理是否存在
                if (minecraft != null && minecraft.getResourceManager().getResource(iconTexture).isEmpty()) {
                    AnnouncementTestMod.LOGGER.warn("图标纹理不存在: {}", ConfigManager.getIconPath());
                    iconTexture = null;
                }
            } catch (Exception e) {
                AnnouncementTestMod.LOGGER.warn("无法加载图标纹理: {}", ConfigManager.getIconPath(), e);
                iconTexture = null;
            }
        }

        // 加载背景纹理（如果启用自定义背景）
        if (ConfigManager.useCustomAnnouncementBackground() && ConfigManager.getAnnouncementBackgroundPath() != null && !ConfigManager.getAnnouncementBackgroundPath().isEmpty()) {
            try {
                backgroundTexture = ResourceLocation.withDefaultNamespace(ConfigManager.getAnnouncementBackgroundPath());
                // 检查纹理是否存在
                if (minecraft != null && minecraft.getResourceManager().getResource(backgroundTexture).isEmpty()) {
                    AnnouncementTestMod.LOGGER.warn("公告背景纹理不存在: {}", ConfigManager.getAnnouncementBackgroundPath());
                    backgroundTexture = null;
                }
            } catch (Exception e) {
                AnnouncementTestMod.LOGGER.warn("无法加载公告背景纹理: {}", ConfigManager.getAnnouncementBackgroundPath(), e);
                backgroundTexture = null;
            }
        }

        // 计算标题位置（考虑图标显示）
        int titleX = centerX;
        if (ConfigManager.shouldShowIcon() && iconTexture != null) {
            // 如果有图标，标题向右移动
            int iconAreaWidth = ConfigManager.getIconWidth() + ConfigManager.getIconTextSpacing();
            titleX = centerX + iconAreaWidth / 2;
        }

        // 安全访问配置字段
        String mainTitleText = ConfigManager.getMainTitle() != null ? ConfigManager.getMainTitle() : "服务器公告";

        // 使用 MutableComponent 创建丰富的主标题
        MutableComponent mainTitle = Component.literal(mainTitleText);
        if (ConfigManager.useCustomRGB()) {
            mainTitle = mainTitle.withColor(ConfigManager.getMainTitleColor());
        } else {
            ChatFormatting formatting = ConfigManager.getChatFormattingFromColor(ConfigManager.getMainTitleColor());
            mainTitle = mainTitle.withStyle(formatting);
        }
        mainTitle = mainTitle.withStyle(ChatFormatting.BOLD);

        titleWidget = new StringWidget(titleX, 30, 200, 20, mainTitle, this.font);
        titleWidget.alignCenter();
        addRenderableWidget(titleWidget);

        String subTitleText = ConfigManager.getSubTitle() != null ? ConfigManager.getSubTitle() : "最新通知";

        // 使用 MutableComponent 创建丰富的副标题
        MutableComponent subTitle = Component.literal(subTitleText);
        if (ConfigManager.useCustomRGB()) {
            subTitle = subTitle.withColor(ConfigManager.getSubTitleColor());
        } else {
            ChatFormatting formatting = ConfigManager.getChatFormattingFromColor(ConfigManager.getSubTitleColor());
            subTitle = subTitle.withStyle(formatting);
        }

        subtitleWidget = new StringWidget(titleX, 55, 200, 20, subTitle, this.font);
        subtitleWidget.alignCenter();
        addRenderableWidget(subtitleWidget);

        // 创建公告内容
        MutableComponent contentText = createAnnouncementContent();

        // 创建滚动文本部件
        // 简化颜色对比度检查
        if ((contentColor & 0xFFFFFF) == (0xB4303030 & 0xFFFFFF)) {
            contentColor = 0xFFFFFFFF; // 如果颜色太相似，使用白色
        }

        // 调试日志
        if (ConfigManager.isDebugMode()) {
            AnnouncementTestMod.LOGGER.info("公告内容: {}", contentText.getString());
            AnnouncementTestMod.LOGGER.info("内容颜色: 0x{}", Integer.toHexString(contentColor));
        }

        scrollableText = new ScrollableTextWidget(
                centerX - 150, 80, 300, this.height - 150,
                contentText, this.font, minecraft,
                contentColor
        );
        addRenderableWidget(scrollableText);

        // 创建按钮
        createButtons(centerX, buttonWidth, buttonHeight, buttonY);
    }

    /**
     * 把整段公告字符串 → MutableComponent
     * 支持：
     *  - §a §b … 原版颜色码
     *  - &#RRGGBB  自定义 RGB 颜色码
     *  - 未写颜色 → 使用 config.contentColor
     */
    private MutableComponent createAnnouncementContent() {
        MutableComponent root = Component.empty();
        List<String> src = getStrings();

        for (String line : src) {
            // 跳过空行
            if (line.trim().isEmpty()) {
                root.append(Component.literal("\n"));
                continue;
            }

            MutableComponent lineText = Component.empty();
            Pattern pattern = Pattern.compile("(&#[0-9a-fA-F]{6}|§[0-9a-fk-or]|[^&§]+)");
            Matcher m = pattern.matcher(line);
            Style currentStyle = Style.EMPTY;

            while (m.find()) {
                String segment = m.group();

                if (segment.startsWith("&#")) {
                    // 处理自定义RGB颜色
                    try {
                        int rgb = Integer.parseInt(segment.substring(2), 16);
                        currentStyle = currentStyle.withColor(rgb);
                    } catch (NumberFormatException e) {
                        AnnouncementTestMod.LOGGER.warn("无效的RGB颜色代码: {}", segment);
                    }
                } else if (segment.startsWith("§")) {
                    // 处理原版格式代码
                    ChatFormatting formatting = ChatFormatting.getByCode(segment.charAt(1));
                    if (formatting != null) {
                        if (formatting == ChatFormatting.RESET) {
                            currentStyle = Style.EMPTY;
                        } else if (formatting.isColor()) {
                            currentStyle = currentStyle.withColor(formatting);
                        } else {
                            currentStyle = currentStyle.withColor(formatting);
                        }
                    }
                } else {
                    // 普通文本，应用当前样式
                    MutableComponent textSegment = Component.literal(segment).setStyle(currentStyle);
                    lineText.append(textSegment);
                }
            }

            root.append(lineText).append(Component.literal("\n"));
        }

        return root;
    }

    private List<String> getStrings() {
        List<String> defaultContent = List.of(
                "§a欢迎游玩，我们团队做的模组！",
                " ",
                "§e一些提醒：",
                "§f1. 模组仅限于1.21.7~1.21.8neoforge",
                "§f2. 模组目前是半成品",
                "§f3. 后面会继续更新",
                " ",
                "§b模组随缘更新",
                "§c若发现bug可以向模组作者或者仓库反馈！"
        );

        List<String> configContent = ConfigManager.getAnnouncementContent();
        return configContent != null && !configContent.isEmpty() ? configContent : defaultContent;
    }

    private void createButtons(int centerX, int buttonWidth, int buttonHeight, int buttonY) {
        // 使用 MutableComponent 创建按钮文本
        // 确定按钮使用配置的文本
        String confirmText = ConfigManager.getConfirmButtonText() != null ? ConfigManager.getConfirmButtonText() : "确定";
        MutableComponent confirmButtonText = Component.literal(confirmText);
        if (ConfigManager.useCustomRGB()) {
            confirmButtonText = confirmButtonText.withColor(0xFFFFFF); // 按钮文本使用白色
        } else {
            confirmButtonText = confirmButtonText.withStyle(ChatFormatting.WHITE);
        }

        // 确定按钮
        addRenderableWidget(Button.builder(confirmButtonText, button -> {
            if (this.minecraft != null) {
                this.onClose();
            }
        }).pos(centerX - buttonWidth - 5, buttonY).size(buttonWidth, buttonHeight).build());

        // 前往投递按钮使用配置的文本
        String submitText = ConfigManager.getSubmitButtonText() != null ? ConfigManager.getSubmitButtonText() : "前往投递";
        MutableComponent submitButtonText = Component.literal(submitText);
        if (ConfigManager.useCustomRGB()) {
            submitButtonText = submitButtonText.withColor(0xFFFFFF); // 按钮文本使用白色
        } else {
            submitButtonText = submitButtonText.withStyle(ChatFormatting.WHITE);
        }

        String buttonLink = ConfigManager.getButtonLink();

        addRenderableWidget(Button.builder(submitButtonText, button -> {
            try {
                String url = buttonLink;
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://" + url;
                }
                Util.getPlatform().openUri(new URI(url));
            } catch (Exception e) {
                if (this.minecraft != null && this.minecraft.player != null) {
                    this.minecraft.player.displayClientMessage(Component.literal("无法打开链接: " + e.getMessage()), false);
                }
            }
        }).pos(centerX + 5, buttonY).size(buttonWidth, buttonHeight).build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 渲染背景
        if (ConfigManager.useCustomAnnouncementBackground() && backgroundTexture != null) {
            // 使用自定义背景图
            guiGraphics.blit(backgroundTexture, 0, 0, 0, 0, this.width, this.height, this.width, this.height);
        } else {
            // 使用与原版UI相似的颜色 #B4303030
            guiGraphics.fill(0, 0, this.width, this.height, 0xB4303030);
        }

        // 绘制图标（如果有）
        if (ConfigManager.shouldShowIcon() && iconTexture != null) {
            int iconX = (this.width / 2) - 150 - ConfigManager.getIconWidth() - ConfigManager.getIconTextSpacing();
            int iconY = 30;

            try {
                // 使用GuiGraphics绘制纹理
                guiGraphics.blit(
                        iconTexture,
                        iconX,
                        iconY,
                        0,
                        0,
                        ConfigManager.getIconWidth(),
                        ConfigManager.getIconHeight(),
                        ConfigManager.getIconWidth(),
                        ConfigManager.getIconHeight()
                );
            } catch (Exception e) {
                AnnouncementTestMod.LOGGER.warn("无法绘制图标", e);
            }
        }

        // 先渲染标题
        renderTitle(guiGraphics, mouseX, mouseY, partialTick);

        // 然后渲染其他部件（包括滚动文本）
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // 滚动文本
        if (scrollableText != null && tickCount % 2 == 0) {
            double maxScroll = scrollableText.getTotalHeight() - scrollableText.getHeight();
            if (maxScroll > 0) {
                double scrollAmount = scrollableText.getScrollAmount() + (ConfigManager.getScrollSpeed() / 20.0);
                if (scrollAmount > maxScroll) scrollAmount = 0;
                scrollableText.setScrollAmount(Math.min(scrollAmount, maxScroll));
            }
        }

        // 调试模式：绘制ScrollableTextWidget的边框
        if (ConfigManager.isDebugMode() && scrollableText != null) {
            guiGraphics.fill(
                    scrollableText.getX() - 1,
                    scrollableText.getY() - 1,
                    scrollableText.getX() + scrollableText.getWidth() + 1,
                    scrollableText.getY() + scrollableText.getHeight() + 1,
                    0x40FF00FF
            );
        }
    }

    private boolean isVersionCompatible() {
        try {
            // NeoForge版本检查方式
            String version = SharedConstants.getCurrentVersion().name();
            AnnouncementTestMod.LOGGER.info("检测到 Minecraft 版本: {}", version);

            return isVersionAtLeast(version, 1, 21, 6);
        } catch (Exception e) {
            AnnouncementTestMod.LOGGER.warn("无法确定 Minecraft 版本", e);
            return true;
        }
    }

    private boolean isVersionAtLeast(String versionString, int minMajor, int minMinor, int minPatch) {
        try {
            String[] parts = versionString.split("\\.");
            if (parts.length < 3) {
                return false;
            }

            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);

            // 处理可能的版本后缀 (如 "1.21.8-neoforge")
            String patchPart = parts[2].replaceAll("[^0-9].*", "");
            int patch = Integer.parseInt(patchPart);

            if (major > minMajor) return true;
            if (major < minMajor) return false;

            if (minor > minMinor) return true;
            if (minor < minMinor) return false;

            return patch >= minPatch;
        } catch (NumberFormatException e) {
            AnnouncementTestMod.LOGGER.warn("无法解析版本号: {}", versionString, e);
            return false;
        }
    }

    private void renderTitle(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        if (ConfigManager.isDebugMode()) {
            if (titleWidget != null) {
                drawDebugOutline(guiGraphics, titleWidget, 0x40FF0000);
            }
            if (subtitleWidget != null) {
                drawDebugOutline(guiGraphics, subtitleWidget, 0x4000FF00);
            }

            // 绘制图标区域调试框
            if (ConfigManager.shouldShowIcon() && iconTexture != null) {
                int iconX = (this.width / 2) - 150 - ConfigManager.getIconWidth() - ConfigManager.getIconTextSpacing();
                int iconY = 30;

                guiGraphics.fill(
                        iconX - 1,
                        iconY - 1,
                        iconX + ConfigManager.getIconWidth() + 1,
                        iconY + ConfigManager.getIconHeight() + 1,
                        0x400000FF
                );
            }
        }

        if (titleWidget != null) titleWidget.render(guiGraphics, mouseX, mouseY, delta);
        if (subtitleWidget != null) subtitleWidget.render(guiGraphics, mouseX, mouseY, delta);
    }

    private void drawDebugOutline(GuiGraphics guiGraphics, AbstractWidget widget, int color) {
        guiGraphics.fill(
                widget.getX() - 1,
                widget.getY() - 1,
                widget.getX() + widget.getWidth() + 1,
                widget.getY() + widget.getHeight() + 1,
                color
        );
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return super.shouldCloseOnEsc();
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 我们已经在render方法中自定义了背景，所以这里可以留空或调用super
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderMenuBackground(GuiGraphics guiGraphics) {
        // 自定义菜单背景渲染
        super.renderMenuBackground(guiGraphics);
    }

    @Override
    protected void renderBlurredBackground(GuiGraphics guiGraphics) {
        super.renderBlurredBackground(guiGraphics);
    }

    @Override
    public void renderTransparentBackground(GuiGraphics guiGraphics) {
        super.renderTransparentBackground(guiGraphics);
    }
}