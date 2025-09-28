package net.redstone233.atm;


import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.redstone233.atm.data.ModEnglishLanguageProvider;

@EventBusSubscriber(modid = AnnouncementTestMod.MOD_ID)
public class ModDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
       event.createProvider(ModEnglishLanguageProvider::new);
    }
}
