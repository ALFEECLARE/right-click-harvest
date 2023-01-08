package io.github.jamalam360.rightclickharvest.gametest;

import com.mojang.authlib.GameProfile;
import io.github.jamalam360.rightclickharvest.RightClickHarvestModInit;
import java.util.UUID;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.block.CropBlock;
import net.minecraft.block.NetherWartBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.test.TestContext;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class TestHelper {

    public static BlockPos wheat(TestContext ctx) {
        ctx.setBlockState(0, 2, 0, Blocks.FARMLAND);
        ctx.setBlockState(0, 3, 0, Blocks.WHEAT.getDefaultState().with(CropBlock.AGE, CropBlock.MAX_AGE));
        return new BlockPos(0, 3, 0);
    }

    public static BlockPos netherWart(TestContext ctx) {
        ctx.setBlockState(0, 2, 0, Blocks.SOUL_SAND);
        ctx.setBlockState(0, 3, 0, Blocks.NETHER_WART.getDefaultState().with(NetherWartBlock.AGE, NetherWartBlock.MAX_AGE));
        return new BlockPos(0, 3, 0);
    }

    public static BlockPos sugarcane(TestContext ctx) {
        ctx.setBlockState(0, 1, 0, Blocks.SAND);
        ctx.setBlockState(0, 2, 0, Blocks.SUGAR_CANE);
        ctx.setBlockState(0, 3, 0, Blocks.SUGAR_CANE);
        return new BlockPos(0, 2, 0);
    }

    public static BlockPos cocoaBeans(TestContext ctx) {
        ctx.setBlockState(0, 2, 0, Blocks.JUNGLE_LOG);
        ctx.setBlockState(1, 2, 0, Blocks.COCOA.getDefaultState().with(CocoaBlock.AGE, CocoaBlock.MAX_AGE));
        return new BlockPos(1, 2, 0);
    }

    public static BlockPos[] radius(TestContext ctx) {
        BlockPos[] arr = new BlockPos[9];
        int i = 0;

        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    ctx.setBlockState(x, y + 2, z, y == 0 ? Blocks.FARMLAND.getDefaultState() : Blocks.WHEAT.getDefaultState().with(CropBlock.AGE, CropBlock.MAX_AGE));

                    if (y == 1) {
                        arr[i] = new BlockPos(x, 3, z);
                        i++;
                    }
                }
            }
        }

        return arr;
    }

    public static void interact(TestContext ctx, BlockPos pos, ItemStack stack) {
        interact(ctx, ctx.createMockPlayer(), pos, stack);
    }

    public static void interact(TestContext ctx, PlayerEntity player, BlockPos pos, ItemStack stack) {
        pos = ctx.getAbsolutePos(pos);
        player.setStackInHand(Hand.MAIN_HAND, stack);
        BlockHitResult hitResult = new BlockHitResult(Vec3d.ofCenter(pos), Direction.NORTH, pos, false);
        RightClickHarvestModInit.onBlockUse(player, player.world, Hand.MAIN_HAND, hitResult);
    }

    public static PlayerEntity createMockPlayer(TestContext ctx) {
        return new PlayerEntity(ctx.getWorld(), BlockPos.ORIGIN, 0.0F, new GameProfile(UUID.randomUUID(), "test-mock-player")) {
            public boolean isSpectator() {
                return false;
            }

            public boolean isCreative() {
                return false;
            }

            public boolean isMainPlayer() {
                return true;
            }
        };
    }
}
