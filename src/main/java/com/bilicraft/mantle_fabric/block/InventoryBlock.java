package com.bilicraft.mantle_fabric.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import com.bilicraft.mantle_fabric.inventory.BaseContainer;
import com.bilicraft.mantle_fabric.inventory.EmptyItemHandler;
import com.bilicraft.mantle_fabric.tileentity.IRenamableContainerProvider;
import com.bilicraft.mantle_fabric.tileentity.InventoryTileEntity;

import javax.annotation.Nullable;

/**
 * Base class for blocks with an inventory
 */
@SuppressWarnings("WeakerAccess")
public abstract class InventoryBlock extends Block {

  protected InventoryBlock(Block.Properties builder) {
    super(builder);
  }

  /* Tile entity */

  // inventories usually need a tileEntity
  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

  /**
   * Called when the block is activated to open the UI. Override to return false for blocks with no inventory
   * @param player Player instance
   * @param world  World instance
   * @param pos    Block position
   * @return true if the GUI opened, false if not
   */
  protected boolean openGui(PlayerEntity player, World world, BlockPos pos) {
    if (world.isClient()) {
      INamedContainerProvider container = this.getContainer(world.getBlockState(pos), world, pos);
      if (container != null && player instanceof ServerPlayerEntity) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        NetworkHooks.openGui(serverPlayer, container, pos);
        if (player.openContainer instanceof BaseContainer<?>) {
          ((BaseContainer<?>) player.openContainer).syncOnOpen(serverPlayer);
        }
      }
    }

    return true;
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
    if (player.isSuppressingBounce()) {
      return ActionResultType.PASS;
    }
    if (!world.isRemote) {
      return this.openGui(player, world, pos) ? ActionResultType.SUCCESS : ActionResultType.PASS;
    }
    return ActionResultType.SUCCESS;
  }


  /* Naming */

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

    // set custom name from named stack
    if (stack.hasDisplayName()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity instanceof IRenamableContainerProvider) {
        ((IRenamableContainerProvider) tileentity).setCustomName(stack.getDisplayName());
      }
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  @Nullable
  @Deprecated
  public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
  }


  /* Inventory handling */

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    if (state.getBlock() != newState.getBlock()) {
      TileEntity te = worldIn.getTileEntity(pos);
      if (te != null) {
        // FIXME legacy support, switch to non-deprecated method in 1.17
        if (te instanceof InventoryTileEntity) {
          dropInventoryItems(state, worldIn, pos, (InventoryTileEntity) te);
        } else {
          te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            .ifPresent(inventory -> dropInventoryItems(state, worldIn, pos, inventory));
        }
        worldIn.updateComparatorOutputLevel(pos, this);
      }
    }

    super.onReplaced(state, worldIn, pos, newState, isMoving);
  }

  /**
   * Called when the block is replaced to drop contained items.
   * @param state       Block state
   * @param worldIn     Tile world
   * @param pos         Tile position
   * @param inventory   Tile entity instance
   * @deprecated  Will remove in 1.17, use {@link #dropInventoryItems(BlockState, World, BlockPos, IItemHandler)}
   */
  @Deprecated
  protected void dropInventoryItems(BlockState state, World worldIn, BlockPos pos, InventoryTileEntity inventory) {
    LazyOptional<IItemHandler> itemCapability = inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    if (itemCapability.isPresent()) {
      dropInventoryItems(state, worldIn, pos, itemCapability.orElse(EmptyItemHandler.INSTANCE));
    } else {
      InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
    }
  }

  /**
   * Called when the block is replaced to drop contained items.
   * @param state       Block state
   * @param worldIn     Tile world
   * @param pos         Tile position
   * @param inventory   Item handler
   */
  protected void dropInventoryItems(BlockState state, World worldIn, BlockPos pos, IItemHandler inventory) {
    dropInventoryItems(worldIn, pos, inventory);
  }

  /**
   * Drops all items from the given inventory in world
   * @param world      World instance
   * @param pos        Position to drop
   * @param inventory  Inventory instance
   */
  public static void dropInventoryItems(World world, BlockPos pos, IItemHandler inventory) {
    double x = pos.getX();
    double y = pos.getY();
    double z = pos.getZ();
    for(int i = 0; i < inventory.getSlots(); ++i) {
      InventoryHelper.spawnItemStack(world, x, y, z, inventory.getStackInSlot(i));
    }
  }

  @SuppressWarnings("deprecation")
  @Deprecated
  @Override
  public boolean eventReceived(BlockState state, World worldIn, BlockPos pos, int id, int param) {
    super.eventReceived(state, worldIn, pos, id, param);
    TileEntity tileentity = worldIn.getTileEntity(pos);
    return tileentity != null && tileentity.receiveClientEvent(id, param);
  }
}
