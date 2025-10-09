package net.redstone233.atm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class SuperFurnaceBlockEntityMixin {

    @Unique
    private boolean announcementTestMod_neoforge_1_21_7$isFast = false;

    @Unique
    private final static BlockPattern SUPER_FURNACE = BlockPatternBuilder.start()
            .aisle("AAA","AAA","AAA")
            .aisle("AAA","AAA","ABA")
            .aisle("AAA","AAA","AAA")
            .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE)))
            .where('B', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.FURNACE)))
            .build();


    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void onServerTick(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity furnace, CallbackInfo ci) {
        if (!(state.getBlock() instanceof FurnaceBlock)) return;
        SuperFurnaceBlockEntityMixin mixin = (SuperFurnaceBlockEntityMixin) (Object) furnace;
        boolean bl = SUPER_FURNACE.find(level, pos) != null;
        if (bl && mixin != null) {
            mixin.announcementTestMod_neoforge_1_21_7$isFast = true;
        } else if (mixin != null) {
            mixin.announcementTestMod_neoforge_1_21_7$isFast = false;
        }
    }

    @Inject(method = "getTotalCookTime", at = @At("RETURN"), cancellable = true)
    private static void onGetTotalCookTime(ServerLevel level, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {
        SuperFurnaceBlockEntityMixin mixin = (SuperFurnaceBlockEntityMixin) (Object) furnace;
        if (mixin != null && mixin.announcementTestMod_neoforge_1_21_7$isFast) {
            cir.setReturnValue(cir.getReturnValueI()/4);
        }
    }
}
