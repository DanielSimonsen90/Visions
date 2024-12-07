package com.danho.visions.abilities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.context.UseOnContext; // For ItemUseContext


public class FireAbility extends BaseAbility {

  public static final FireAbility INSTANCE = new FireAbility();

  private static final Item FUEL_ITEM = Items.COAL; // The fuel item to add to the furnace on passive use
  private static final int FUEL_SLOT_INDEX = 1; // The index of the fuel slot in the furnace

  private FireAbility() { super(); }

  @Override
  public void onPassiveUsed() {
    ServerPlayer player = getServerPlayer();
    if (player == null) return;

    // Get the block the player is looking at
    BlockHitResult hitResult = getPlayerLookAtBlock(player);
    if (hitResult == null) return; // Prevent doing anything if player is not looking at a block

    BlockPos targetPos = hitResult.getBlockPos();
    Level level = player.level();

    // Check if the player is facing a fuelable utility block
    if (isFuelableBlock(level.getBlockState(targetPos))) {
      // If it's a fuelable block (like a furnace), try to add coal to the fuel slot
      if (canAddFuelToFurnace(level, targetPos)) addFuelToFurnace(level, targetPos);
    } else {
      // If it's not a fuelable block, try to ignite the block
      igniteBlock(player, targetPos);
    }
  }

  private boolean isFuelableBlock(BlockState blockState) {
    return (
      blockState.getBlock() instanceof FurnaceBlock
      || blockState.getBlock() instanceof BlastFurnaceBlock
      || blockState.getBlock() instanceof SmokerBlock
    );
  }

  private boolean canAddFuelToFurnace(Level level, BlockPos furnacePos) {
    BlockState blockState = level.getBlockState(furnacePos);

    // Ensure the block state is a fuelable block (Furnace, Blast Furnace, Smoker)
    if (blockState.getBlock() instanceof AbstractFurnaceBlock
      && level.getExistingBlockEntity(furnacePos) instanceof AbstractFurnaceBlockEntity furnaceBlockEntity
    ) {
      // Check if the furnace has an empty fuel slot.
      // This also balances the "overpowered-ness" of the ability by requiring the furnace to have an empty fuel slot.
      return furnaceBlockEntity.getItem(FUEL_SLOT_INDEX).isEmpty();
    }

    return false; // Return false if block isn't fuelable or no fuel slot available
  }

  private void addFuelToFurnace(Level level, BlockPos furnacePos) {
    BlockEntity blockEntity = level.getExistingBlockEntity(furnacePos);
    if (blockEntity instanceof AbstractFurnaceBlockEntity furnaceEntity) {
      furnaceEntity.setItem(FUEL_SLOT_INDEX, new ItemStack(FUEL_ITEM));
      furnaceEntity.setChanged(); // Mark the furnace as modified
    }
  }

  private void igniteBlock(ServerPlayer player, BlockPos targetPos) {
    // Save the original item the player is holding
    ItemStack originalItem = player.getMainHandItem().copy();

    // Temporarily set the player's main hand item to Flint and Steel
    player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.FLINT_AND_STEEL));

    // Create the UseOnContext to use Flint and Steel on target block
    UseOnContext context = new UseOnContext(
      player,
      InteractionHand.MAIN_HAND,
      new BlockHitResult(player.getEyePosition(), Direction.UP, targetPos, false)
    );

    // Perform use on the target block
    InteractionResult result = player.getMainHandItem().useOn(context);

    // Restore the player's original item
    player.setItemInHand(InteractionHand.MAIN_HAND, originalItem);

    // If the use was successful, return (expected CONSUME, but only return if it's not FAIL)
    if (result != InteractionResult.FAIL) return;

    // If interaction failed, place fire block manually

    BlockHitResult blockHitResult = player.level().clip(new ClipContext(
      player.getEyePosition(),
      player.getLookAngle(),
      ClipContext.Block.OUTLINE,
      ClipContext.Fluid.NONE,
      player
    ));
    BlockState fireState = Blocks.FIRE.getStateForPlacement(
      new BlockPlaceContext(player.level(), player, InteractionHand.MAIN_HAND, player.getMainHandItem(), blockHitResult)
    );
    if (fireState == null) fireState = Blocks.FIRE.defaultBlockState();

    Direction face = context.getClickedFace();
    BlockPos firePos = targetPos.relative(face);

    // Perform the placement of the fire block if the block above target block is air
    if (player.level().getBlockState(firePos).isAir()) player.level().setBlockAndUpdate(firePos, fireState);
  }



}
