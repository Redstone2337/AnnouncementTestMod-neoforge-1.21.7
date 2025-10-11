package net.redstone233.atm.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

import static net.redstone233.atm.AnnouncementTestMod.MOD_ID;

public class MenuScreen extends Screen {
    public MenuScreen() {
        super(Component.translatable("menu.title.text"));
    }

    @Override
    protected void init() {
        super.init();

        // 添加随机功能按钮
        int buttonWidth = 150;
        int buttonHeight = 20;
        int centerX = this.width / 2 - buttonWidth / 2;
        int startY = this.height / 4 + 30;

        // 随机按钮1
        this.addRenderableWidget(Button.builder(
                        Component.translatable("menu.button.text.1"),
                        button -> System.out.println("功能A被激活!"))
                .bounds(centerX, startY, buttonWidth, buttonHeight)
                .build());

        // 随机按钮2
        this.addRenderableWidget(Button.builder(
                        Component.translatable("menu.button.text.2"),
                        button -> System.out.println("功能B被激活!"))
                .bounds(centerX, startY + 30, buttonWidth, buttonHeight)
                .build());

        // 关闭按钮
        this.addRenderableWidget(Button.builder(
                        Component.translatable("gui.cancel"),
                        button -> this.onClose())
                .bounds(centerX, startY + 100, buttonWidth, buttonHeight)
                .build());
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // 渲染半透明黑色背景
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000); // 50% 透明度的黑色

        // 绘制主标题
        int titleY = this.height / 8;
        guiGraphics.drawCenteredString(
                this.font,
                Component.translatable("menu.body.text"),
                this.width / 2,
                titleY,
                0xFFFFFF // 白色
        );

        // 获取版本信息
        String modVersion = FMLLoader.getLoadingModList().getModFileById(MOD_ID).versionString();
        String gameVersion = FMLLoader.versionInfo().mcVersion();

        // 在标题左下角显示模组版本
        String modVersionText = "menu.title.modVersion.text" + modVersion;
        guiGraphics.drawString(
                this.font,
                modVersionText,
                10, // 左边距
                titleY + 15, // 标题下方
                0x88FF88, // 浅绿色
                false
        );

        // 在标题右下角显示游戏版本
        String gameVersionText = "menu.title.gameVersion.text" + gameVersion;
        int gameVersionWidth = this.font.width(gameVersionText);
        guiGraphics.drawString(
                this.font,
                gameVersionText,
                this.width - gameVersionWidth - 10, // 右边距
                titleY + 15, // 标题下方
                0x8888FF, // 浅蓝色
                false
        );

        // 绘制分隔线（可选）
        int separatorY = titleY + 35;
        guiGraphics.fill(10, separatorY, this.width - 10, separatorY + 1, 0x44FFFFFF);

        // 绘制说明文本
        guiGraphics.drawCenteredString(
                this.font,
                "菜单功能区域",
                this.width / 2,
                separatorY + 10,
                0xCCCCCC
        );

        // 渲染所有按钮
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }
}
