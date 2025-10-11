package net.redstone233.atm.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.BlastFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.SmokerBlock;
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

import java.util.Set;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class SuperFurnaceBlockEntityMixin {

    @Unique
    private boolean announcementTestMod_neoforge_1_21_7$isFast = false;

    @Unique
    private final static BlockPattern SUPER_FURNACE = BlockPatternBuilder.start()
            .aisle("AAA","AAA","AAA")
            .aisle("AAA","AAA","AAB")
            .aisle("AAA","AAA","AAA")
            .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.STONE)))
            .where('B', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.FURNACE)))
            .build();

    @Unique
    private final static BlockPattern SUPER_BLAST_FURNACE = BlockPatternBuilder.start()
            .aisle("CCC","CCC","CCC")
            .aisle("CCC","CCC","CBC")
            .aisle("AAA","AAA","AAA")
            .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SMOOTH_STONE)))
            .where('B', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.BLAST_FURNACE)))
            .where('C', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
            .build();

    @Unique
    private final static BlockPattern SUPER_SMOKER = BlockPatternBuilder.start()
            .aisle("CAC","ACA","CAC")
            .aisle("CAC","ACA","CBC")
            .aisle("CAC","ACA","CAC")
            .where('A', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.OAK_LOG)
                    .or(BlockStatePredicate.forBlock(Blocks.OAK_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.SPRUCE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.BIRCH_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.JUNGLE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.ACACIA_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.DARK_OAK_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.MANGROVE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.CHERRY_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.CRIMSON_STEM))
                    .or(BlockStatePredicate.forBlock(Blocks.WARPED_STEM))
            ))
            .where('B', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.SMOKER)))
            .where('C', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.COAL_BLOCK)))
            .build();

    @Unique
    private static final Set<BlockPattern> announcementTestMod_neoforge_1_21_7$patterns = Set.of(SUPER_FURNACE, SUPER_BLAST_FURNACE, SUPER_SMOKER);



    @Inject(method = "serverTick", at = @At("TAIL"))
    private static void onServerTick(ServerLevel level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity furnace, CallbackInfo ci) {
        if (!(state.getBlock() instanceof FurnaceBlock ||
                state.getBlock() instanceof BlastFurnaceBlock ||
                state.getBlock() instanceof SmokerBlock)) return;
        SuperFurnaceBlockEntityMixin mixin = (SuperFurnaceBlockEntityMixin) (Object) furnace;
        boolean bl = announcementTestMod_neoforge_1_21_7$patterns.stream().anyMatch(pattern -> pattern.find(level, pos) != null);
        if (bl) {
            if (mixin != null) {
                mixin.announcementTestMod_neoforge_1_21_7$isFast = true;
            }
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
