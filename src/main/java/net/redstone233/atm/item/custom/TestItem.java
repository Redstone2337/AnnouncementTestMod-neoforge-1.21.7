package net.redstone233.atm.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class TestItem extends Item {
    public TestItem(Properties properties) {
        super(properties);
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 14.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -5.5F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof LivingEntity livingEntity && attacker instanceof ServerPlayer player) {
            livingEntity.setRemainingFireTicks(120);
            if (!livingEntity.isDeadOrDying()) {
                livingEntity.kill(player.level());
            }
            if (player instanceof Player player1) {
                player1.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,1200,4,false,false,false));
                player1.addEffect(new MobEffectInstance(MobEffects.SATURATION, 300,4,false,true,true));
            }
            // Custom behavior when the item hurts an enemy
        }
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST,6000,4,false,false,false));
                return InteractionResult.SUCCESS;
        } else {
            player.displayClientMessage(Component.literal("似乎没有正常运行"),false);
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() instanceof ServerPlayer player && context.getLevel().isClientSide()) {
            player.addEffect(new MobEffectInstance(MobEffects.WITHER,6000,1,true,true,true));
            if (!player.isDeadOrDying()) {
                player.kill(player.level());
            }
            if (player instanceof Player player1) {
               player1.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,1200,4,false,false,false));
                player1.addEffect(new MobEffectInstance(MobEffects.SATURATION, 300,4,false,true,true));
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }
}
