package net.redstone233.atm.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redstone233.atm.AnnouncementTestMod;
import net.redstone233.atm.component.BlazingFlameSwordComponent;
import net.redstone233.atm.component.IceFreezeSwordComponent;
import net.redstone233.atm.component.types.ModComponentTypes;
import net.redstone233.atm.item.custom.BlazingFlameSwordItem;
import net.redstone233.atm.item.custom.IceFreezeSwordItem;
import net.redstone233.atm.materials.ModToolMaterials;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AnnouncementTestMod.MOD_ID);

    public static final DeferredItem<Item> BLAZING_FLAME_SWORD =
            ITEMS.register("blazing_flame_sword", resourceLocation -> new BlazingFlameSwordItem(
                    ModToolMaterials.SPECIAL,50,7.5f,
                        new Item.Properties()
                                .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
                                .durability(300000)
                                .rarity(Rarity.RARE)
                    )
            );

    public static final DeferredItem<Item> ICE_FREEZE_SWORD =
            ITEMS.register("ice_freeze_sword", resourceLocation -> new IceFreezeSwordItem(
                    ModToolMaterials.SPECIAL,55,7.5f,
                        new Item.Properties()
                                .setId(ResourceKey.create(Registries.ITEM, resourceLocation))
                                .durability(300000)
                                .rarity(Rarity.RARE)
                    )
            );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}