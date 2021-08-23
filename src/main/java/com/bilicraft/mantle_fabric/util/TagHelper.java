package com.bilicraft.mantle_fabric.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants.NBT;

import javax.annotation.Nullable;

/**
 * Helpers to aid in reading and writing of NBT
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TagHelper {
  /* BlockPos */

  /**
   * Converts a block position to NBT
   * @param pos Position
   * @return NBT compound
   */
  public static CompoundNBT writePos(BlockPos pos) {
    CompoundNBT tag = new CompoundNBT();
    tag.putInt("X", pos.getX());
    tag.putInt("Y", pos.getY());
    tag.putInt("Z", pos.getZ());
    return tag;
  }

  /**
   * Reads a block position from a given tag compound
   * @param tag Tag to read
   * @return BlockPos, or null if invalid
   */
  @Nullable
  public static BlockPos readPos(CompoundNBT tag) {
    if (tag.contains("X", NBT.TAG_ANY_NUMERIC) && tag.contains("Y", NBT.TAG_ANY_NUMERIC) && tag.contains("Z", NBT.TAG_ANY_NUMERIC)) {
      return new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z"));
    }
    return null;
  }
}
