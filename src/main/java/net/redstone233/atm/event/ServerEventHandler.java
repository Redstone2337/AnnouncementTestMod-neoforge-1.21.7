package net.redstone233.atm.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.redstone233.atm.config.v1.ConfigManager;
import net.redstone233.atm.core.pocket.ShowAnnouncementPacket;

import java.util.Optional;

public class ServerEventHandler {

    private static final String SEEN_ANNOUNCEMENT_KEY = "announcement_mod:has_seen_announcement";

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (!ConfigManager.shouldShowOnWorldEnter()) {
            return;
        }

        if (!hasPlayerSeenAnnouncement(player).orElse(false)) {
            sendAnnouncementToPlayer(player);
            markPlayerSeenAnnouncement(player);
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        // 只在进入主世界时显示
        if (event.getTo().location().getPath().equals("overworld")) {
            if (!hasPlayerSeenAnnouncement(player).orElse(false)) {
                sendAnnouncementToPlayer(player);
                markPlayerSeenAnnouncement(player);
            }
        }
    }

    private Optional<Boolean> hasPlayerSeenAnnouncement(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        return data.getBoolean(SEEN_ANNOUNCEMENT_KEY);
    }

    private void markPlayerSeenAnnouncement(ServerPlayer player) {
        CompoundTag persistentData = player.getPersistentData();
        persistentData.putBoolean(SEEN_ANNOUNCEMENT_KEY, true);
        persistentData.put(ServerPlayer.PERSISTED_NBT_TAG, persistentData);
    }

    private void sendAnnouncementToPlayer(ServerPlayer player) {
        ShowAnnouncementPacket packet = ShowAnnouncementPacket.fromConfig();
        PacketDistributor.sendToPlayer(player, packet);
    }

    // 重置玩家公告状态的方法
    public static void resetPlayerAnnouncementStatus(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        data.putBoolean(SEEN_ANNOUNCEMENT_KEY, false);
        data.put(ServerPlayer.PERSISTED_NBT_TAG, data);
    }
}