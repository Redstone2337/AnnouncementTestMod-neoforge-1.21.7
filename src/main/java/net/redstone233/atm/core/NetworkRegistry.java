package net.redstone233.atm.core;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.redstone233.atm.core.pocket.ShowAnnouncementPacket;

public class NetworkRegistry {

    private static final String PROTOCOL_VERSION = "1.0";

    @SubscribeEvent
    public static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        // 注册显示公告的数据包（服务端 → 客户端）
        registrar.playToClient(
                ShowAnnouncementPacket.TYPE,
                ShowAnnouncementPacket.STREAM_CODEC,
                ShowAnnouncementPacket::handle
        );

        System.out.println("Announcement Mod 网络系统注册完成");
    }
}
