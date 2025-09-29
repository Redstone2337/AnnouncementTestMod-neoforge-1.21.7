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
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public record IceFreezeSwordComponent(boolean display) implements TooltipProvider {
    public static final IceFreezeSwordComponent DEFAULT = new IceFreezeSwordComponent(true);
    
    public static final Codec<IceFreezeSwordComponent> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.BOOL.fieldOf("isDisplay").forGetter(IceFreezeSwordComponent::display)
            ).apply(instance, IceFreezeSwordComponent::new)
    );

    public static final StreamCodec<ByteBuf, IceFreezeSwordComponent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, IceFreezeSwordComponent::display,
            IceFreezeSwordComponent::new
    );
    
    @Override
    public void addToTooltip(Item.@NotNull TooltipContext context, Consumer<Component> tooltipAdder, @NotNull TooltipFlag flag, @NotNull DataComponentGetter componentGetter) {
        tooltipAdder.accept(Component.translatable("tooltip.ability_sword.display1").withStyle(ChatFormatting.WHITE)
                .append(Component.translatable("key.use_ability.item",Component.keybind(ModKeys.USE_ABILITY_KEY.getDisplayName().getString())
                                .withStyle(ChatFormatting.GOLD))
                        .append(Component.translatable("tooltip.ability_sword.display2").withStyle(ChatFormatting.WHITE))
                ));

        tooltipAdder.accept(Component.literal("[武器品质]").withStyle(ChatFormatting.DARK_GRAY,ChatFormatting.BOLD));
        tooltipAdder.accept(Component.literal("品质 ").withStyle(ChatFormatting.WHITE)
                .append(Component.literal("传说").withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)));

        tooltipAdder.accept(Component.literal("[武器介绍]").withStyle(ChatFormatting.DARK_GRAY,ChatFormatting.BOLD));
        tooltipAdder.accept(Component.literal("极寒玄冰凝聚而成的神剑，通体的冰晶。").withStyle(ChatFormatting.BLUE));
        tooltipAdder.accept(Component.literal("剑柄缠绕着银白色的冰蚕丝，护手处镶嵌着天蓝色的冰魄石。").withStyle(ChatFormatting.BLUE));
        tooltipAdder.accept(Component.literal("剑柄缠绕着银白色的冰蚕丝，护手处镶嵌着天蓝色的冰雕琢，薄如蝉翼却坚不可摧，挥动时会飘散出细小的冰晶。").withStyle(ChatFormatting.BLUE));
        tooltipAdder.accept(Component.literal("剑尖处常有寒气缭绕，形成小型的雪花漩涡。").withStyle(ChatFormatting.BLUE));
        tooltipAdder.accept(Component.literal("整体散发着幽蓝的寒光，仿佛能冻结时间。").withStyle(ChatFormatting.BLUE));
    }
}
