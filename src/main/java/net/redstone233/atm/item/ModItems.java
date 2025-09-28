package net.redstone233.atm.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redstone233.atm.AnnouncementTestMod;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnnouncementTestMod.MOD_ID);

    public static final DeferredItem<Item> BLAZING_FLAME_SWORD =
            ITEMS.register("blazing_flame_sword", () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ICE_FREEZE_SWORD =
            ITEMS.register("ice_freeze_sword", () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}