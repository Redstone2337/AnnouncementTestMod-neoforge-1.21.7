package net.redstone233.atm.core.pocket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.redstone233.atm.config.v1.ConfigManager;
import net.redstone233.atm.core.network.ClientPacketHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record ShowAnnouncementPacket(
        String mainTitle,
        String subTitle,
        List<String> announcementContent,
        boolean useCustomRGB,
        int mainTitleColor,
        int subTitleColor,
        int contentColor,
        double scrollSpeed,
        boolean showIcon,
        String iconPath,
        int iconWidth,
        int iconHeight,
        int iconTextSpacing,
        String confirmButtonText,
        String submitButtonText,
        String buttonLink,
        boolean useCustomBackground,
        String backgroundPath
) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ShowAnnouncementPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("announcement_mod", "show_announcement"));

    public static final StreamCodec<FriendlyByteBuf, ShowAnnouncementPacket> STREAM_CODEC =
            StreamCodec.of(ShowAnnouncementPacket::encode, ShowAnnouncementPacket::new);

    // 编码器
    public static void encode(FriendlyByteBuf buffer, ShowAnnouncementPacket packet) {
        buffer.writeUtf(packet.mainTitle != null ? packet.mainTitle : "");
        buffer.writeUtf(packet.subTitle != null ? packet.subTitle : "");

        if (packet.announcementContent != null) {
            buffer.writeVarInt(packet.announcementContent.size());
            for (String line : packet.announcementContent) {
                buffer.writeUtf(line != null ? line : "");
            }
        } else {
            buffer.writeVarInt(0);
        }

        buffer.writeBoolean(packet.useCustomRGB);
        buffer.writeInt(packet.mainTitleColor);
        buffer.writeInt(packet.subTitleColor);
        buffer.writeInt(packet.contentColor);
        buffer.writeDouble(packet.scrollSpeed);

        buffer.writeBoolean(packet.showIcon);
        buffer.writeUtf(packet.iconPath != null ? packet.iconPath : "");
        buffer.writeInt(packet.iconWidth);
        buffer.writeInt(packet.iconHeight);
        buffer.writeInt(packet.iconTextSpacing);

        buffer.writeUtf(packet.confirmButtonText != null ? packet.confirmButtonText : "");
        buffer.writeUtf(packet.submitButtonText != null ? packet.submitButtonText : "");
        buffer.writeUtf(packet.buttonLink != null ? packet.buttonLink : "");

        buffer.writeBoolean(packet.useCustomBackground);
        buffer.writeUtf(packet.backgroundPath != null ? packet.backgroundPath : "");
    }

    // 解码器
    public ShowAnnouncementPacket(FriendlyByteBuf buffer) {
        this(
                readStringSafe(buffer),
                readStringSafe(buffer),
                readStringList(buffer),
                buffer.readBoolean(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readDouble(),
                buffer.readBoolean(),
                readStringSafe(buffer),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readInt(),
                readStringSafe(buffer),
                readStringSafe(buffer),
                readStringSafe(buffer),
                buffer.readBoolean(),
                readStringSafe(buffer)
        );
    }

    private static String readStringSafe(FriendlyByteBuf buffer) {
        String str = buffer.readUtf();
        return str.isEmpty() ? null : str;
    }

    private static List<String> readStringList(FriendlyByteBuf buffer) {
        int size = buffer.readVarInt();
        if (size <= 0) {
            return new ArrayList<>();
        }

        List<String> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String line = buffer.readUtf();
            list.add(line);
        }
        return list;
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final ShowAnnouncementPacket packet, final IPayloadContext context) {
        context.enqueueWork(() -> {
            ClientPacketHandler.handleShowAnnouncement(packet);
        });
    }

    public static ShowAnnouncementPacket fromConfig() {
        return new ShowAnnouncementPacket(
                ConfigManager.getMainTitle(),
                ConfigManager.getSubTitle(),
                ConfigManager.getAnnouncementContent(),
                ConfigManager.useCustomRGB(),
                ConfigManager.getMainTitleColor(),
                ConfigManager.getSubTitleColor(),
                ConfigManager.getContentColor(),
                ConfigManager.getScrollSpeed(),
                ConfigManager.shouldShowIcon(),
                ConfigManager.getIconPath(),
                ConfigManager.getIconWidth(),
                ConfigManager.getIconHeight(),
                ConfigManager.getIconTextSpacing(),
                ConfigManager.getConfirmButtonText(),
                ConfigManager.getSubmitButtonText(),
                ConfigManager.getButtonLink(),
                ConfigManager.useCustomAnnouncementBackground(),
                ConfigManager.getAnnouncementBackgroundPath()
        );
    }
}
