package net.redstone233.atm.keys;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.redstone233.atm.AnnouncementTestMod;
import org.lwjgl.glfw.GLFW;


@EventBusSubscriber(modid = AnnouncementTestMod.MOD_ID, value = Dist.CLIENT)
public class ModKeys {
    private static final String ANNOUNCEMENT_NAME = "key.atm.announcement";
    private static final String KEY_CATEGORY = "category.atm";


    public static KeyMapping ANNOUNCEMENT_KEY = new KeyMapping(
            ANNOUNCEMENT_NAME,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            KEY_CATEGORY
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ANNOUNCEMENT_KEY);
    }

    public static boolean isAnnouncementKeyPressed() {
        return ANNOUNCEMENT_KEY.isDown();
    }

    // 可选：添加检查按键是否刚刚被按下（而不是持续按下）的方法
    public static boolean wasAnnouncementKeyPressed() {
        return ANNOUNCEMENT_KEY.consumeClick();
    }

    public static void init() {
        AnnouncementTestMod.LOGGER.info("命令注册成功！");
    }
}
