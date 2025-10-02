package net.redstone233.atm.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redstone233.atm.AnnouncementTestMod;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> MODE_TAB_DEFERRED_REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AnnouncementTestMod.MOD_ID);

    public static final Supplier<CreativeModeTab> MODE_TAB_SUPPLIER =
            MODE_TAB_DEFERRED_REGISTER.register("announcement_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.BLAZING_FLAME_SWORD.asItem()))
                    .title(Component.translatable("itemGroup.announcement_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BLAZING_FLAME_SWORD);
                        output.accept(ModItems.ICE_FREEZE_SWORD);
                        output.accept(ModItems.TEST_ITEM);
                    })
                    .build()
            );

    public static void register(IEventBus eventBus) {
        MODE_TAB_DEFERRED_REGISTER.register(eventBus);
    }
}
