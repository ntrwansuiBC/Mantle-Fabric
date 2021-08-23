package com.bilicraft.mantle_fabric.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

/**
 * Item group that sets its item based on an item supplier
 */
public class SupplierItemGroup extends ItemGroup {
  private final Supplier<ItemStack> supplier;

  /**
   * Creates a new item group
   * @param modId     Tab owner mod ID
   * @param name      Tab name
   * @param supplier  Item stack supplier
   */
  public SupplierItemGroup(String modId, String name, Supplier<ItemStack> supplier) {
    super(String.format("%s.%s", modId, name));
    this.setTabPath(String.format("%s/%s", modId, name));
    this.supplier = supplier;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public ItemStack createIcon() {
    return supplier.get();
  }
}
