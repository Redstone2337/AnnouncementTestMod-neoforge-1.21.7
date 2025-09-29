package net.redstone233.atm.component.types;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.redstone233.atm.AnnouncementTestMod;
import net.redstone233.atm.component.BlazingFlameSwordComponent;
import net.redstone233.atm.component.IceFreezeSwordComponent;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModComponentTypes {

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE,AnnouncementTestMod.MOD_ID);

    public static final Supplier<DataComponentType<BlazingFlameSwordComponent>> BLAZING_FLAME_SWORD_COMPONENT =
            DATA_COMPONENTS.registerComponentType("blazing_flame_sword",
                    blazingFlameSwordComponentBuilder -> blazingFlameSwordComponentBuilder
                            .persistent(BlazingFlameSwordComponent.CODEC)
                            .networkSynchronized(BlazingFlameSwordComponent.STREAM_CODEC)
                    );

    public static final Supplier<DataComponentType<IceFreezeSwordComponent>> ICE_FREEZE_SWORD_COMPONENT =
            DATA_COMPONENTS.registerComponentType("ice_freeze_sword",
                    iceFreezeSwordComponentBuilder -> iceFreezeSwordComponentBuilder
                            .persistent(IceFreezeSwordComponent.CODEC)
                            .networkSynchronized(IceFreezeSwordComponent.STREAM_CODEC)
            );


   public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
   }
}
