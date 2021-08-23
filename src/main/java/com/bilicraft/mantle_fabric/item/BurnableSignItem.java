package com.bilicraft.mantle_fabric.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SignItem;

public class BurnableSignItem extends SignItem {
  private final int burnTime;
  public BurnableSignItem(Properties propertiesIn, Block floorBlockIn, Block wallBlockIn, int burnTime) {
    super(propertiesIn, floorBlockIn, wallBlockIn);
    this.burnTime = burnTime;
  }

  @Override
  public int getBurnTime(ItemStack itemStack) {
    return burnTime;
  }
}
