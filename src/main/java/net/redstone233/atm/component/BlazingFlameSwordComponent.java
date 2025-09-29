package net.redstone233.atm.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.redstone233.atm.keys.ModKeys;

import java.util.function.Consumer;

public record BlazingFlameSwordComponent(boolean display) implements TooltipProvider {
    public static final BlazingFlameSwordComponent DEFAULT = new BlazingFlameSwordComponent(true);
    
    public static final Codec<BlazingFlameSwordComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("isDisplay").forGetter(BlazingFlameSwordComponent::display)
            ).apply(instance, BlazingFlameSwordComponent::new)
    );

    public static final StreamCodec<ByteBuf, BlazingFlameSwordComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, BlazingFlameSwordComponent::display,
            BlazingFlameSwordComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, DataComponentGetter componentGetter) {
        tooltipAdder.accept(Component.translatable("tooltip.ability_sword.display1").withStyle(ChatFormatting.WHITE)
                .append(Component.translatable("key.use_ability.item",Component.keybind(ModKeys.USE_ABILITY_KEY.getDisplayName().getString())
                                .withStyle(ChatFormatting.GOLD))
                        .append(Component.translatable("tooltip.ability_sword.display2").withStyle(ChatFormatting.WHITE))
                ));

        tooltipAdder.accept(Component.literal("[武器品质]").withStyle(ChatFormatting.DARK_GRAY,ChatFormatting.BOLD));
        tooltipAdder.accept(Component.literal("品质 ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal("传说").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)));

        tooltipAdder.accept(Component.literal("[武器介绍]").withStyle(ChatFormatting.DARK_GRAY,ChatFormatting.BOLD));
        tooltipAdder.accept(Component.literal("由陨铁与火晶石锻造的神兵，剑身呈深红色，表面有流动的岩浆纹路。"));
        tooltipAdder.accept(Component.literal("剑柄由黑曜石制成，发着橙红色的光芒，仿佛握着一团永不熄灭的烈焰。"));
        tooltipAdder.accept(Component.literal("剑刃炽热通红，边缘隐约可见金色的火焰纹路。"));
        tooltipAdder.accept(Component.literal("动时会在空中留下火焰轨迹，剑尖处常有小火苗跳动。"));
        tooltipAdder.accept(Component.literal("整体散发着橙红色的光芒，仿佛握着一团永不熄灭的烈焰。"));
    }
}
