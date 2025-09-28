package net.redstone233.atm.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.redstone233.atm.keys.ModKeys;

import java.awt.*;

import static net.redstone233.atm.keys.ModKeys.isUseAbilityKeyPressed;

public class BlazingFlameSwordItem extends Item {
    public BlazingFlameSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(properties
                .sword(material, attackDamage, attackSpeed)
        );
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player && target instanceof LivingEntity livingEntity) {
            livingEntity.isOnFire();
            livingEntity.setRemainingFireTicks(120);
            player.addEffect(new MobEffectInstance(MobEffects.SPEED, 1200, 4, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 4, false, false, false));
        }
        super.postHurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {

        if (level.isClientSide) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1200, 4, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 300, 4, false, true, true));
            return InteractionResult.SUCCESS;
        } else {
            player.displayClientMessage(Component.literal("似乎并没有正常执行。").withStyle(ChatFormatting.RED, ChatFormatting.BOLD), false);
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player instanceof Player player1 && interactionTarget instanceof LivingEntity target) {
            // NeoForge 中需要重新实现按键检测
            // 可以使用 capability 或客户端-服务端通信
            if (ModKeys.wasUseAbilityKeyPressed()) {
                target.setRemainingFireTicks(120);
            }
            player1.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 3600, 4));
            player1.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 1800, 6));

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        super.hurtEnemy(stack, target, attacker);
    }
}
