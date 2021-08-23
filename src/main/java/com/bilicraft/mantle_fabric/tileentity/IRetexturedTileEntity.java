package com.bilicraft.mantle_fabric.tileentity;

import com.bilicraft.mantle_fabric.block.RetexturedBlock;
import com.bilicraft.mantle_fabric.client.model.data.SinglePropertyData;
import com.bilicraft.mantle_fabric.item.RetexturedBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.model.data.IModelData;
import com.bilicraft.mantle_fabric.util.RetexturedHelper;

/**
 * Standard interface that should be used by retexturable tile entities, allows control over where the texture is saved.
 *
 * Use alongside {@link RetexturedBlock} and {@link RetexturedBlockItem}. See {@link RetexturedTileEntity} for implementation.
 */
public interface IRetexturedTileEntity {
  /* Gets the Forge tile data for the tile entity */
  CompoundNBT getTileData();

  /**
   * Gets the current texture block name
   * @return Texture block name
   */
  default String getTextureName() {
    return RetexturedHelper.getTextureName(getTileData());
  }
  /**
   * Gets the current texture block
   * @return Texture block
   */
  default Block getTexture() {
    return RetexturedHelper.getBlock(getTextureName());
  }

  /**
   * Updates the texture to the given name
   * @param name  Texture name
   */
  default void updateTexture(String name) {
    RetexturedHelper.setTexture(getTileData(), name);
  }

  /**
   * Gets the model data instance with the relevant texture block
   * @return  Model data for the TE
   */
  default IModelData getRetexturedModelData() {
    // texture not loaded
    Block block = getTexture();
    // cannot support air, saves a conditional on usage
    if (block == Blocks.AIR) {
      block = null;
    }
    return new SinglePropertyData<>(RetexturedHelper.BLOCK_PROPERTY, block);
  }
}
