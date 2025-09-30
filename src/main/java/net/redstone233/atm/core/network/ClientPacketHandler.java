package net.redstone233.atm.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.redstone233.atm.config.v1.AnnouncementConfig;
import net.redstone233.atm.core.pocket.ShowAnnouncementPacket;
import net.redstone233.atm.screen.v1.AnnouncementScreen;

public class ClientPacketHandler {

    public static void handleShowAnnouncement(ShowAnnouncementPacket packet) {
        if (Minecraft.getInstance().level == null) {
            return;
        }

        AnnouncementConfig config = convertPacketToConfig(packet);

        Minecraft.getInstance().execute(() -> {
            Screen currentScreen = Minecraft.getInstance().screen;
            if (currentScreen == null) {
                Minecraft.getInstance().setScreen(new AnnouncementScreen(config));
            } else {
                currentScreen.onClose();
                Minecraft.getInstance().setScreen(new AnnouncementScreen(config));
            }
        });
    }

    private static AnnouncementConfig convertPacketToConfig(ShowAnnouncementPacket packet) {
        AnnouncementConfig config = new AnnouncementConfig();

        config.mainTitle = packet.mainTitle();
        config.subTitle = packet.subTitle();
        config.announcementContent = packet.announcementContent();
        config.useCustomRGB = packet.useCustomRGB();
        config.mainTitleColor = packet.mainTitleColor();
        config.subTitleColor = packet.subTitleColor();
        config.contentColor = packet.contentColor();
        config.scrollSpeed = packet.scrollSpeed();
        config.showIcon = packet.showIcon();
        config.iconPath = packet.iconPath();
        config.iconWidth = packet.iconWidth();
        config.iconHeight = packet.iconHeight();
        config.iconTextSpacing = packet.iconTextSpacing();
        config.confirmButtonText = packet.confirmButtonText();
        config.submitButtonText = packet.submitButtonText();
        config.buttonLink = packet.buttonLink();
        config.useCustomAnnouncementBackground = packet.useCustomBackground();
        config.announcementBackgroundPath = packet.backgroundPath();

        return config;
    }
}