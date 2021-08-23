package com.bilicraft.mantle_fabric.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * Recipe that has an output other than an {@link ItemStack}
 * @param <C>  Inventory type
 */
public interface ICustomOutputRecipe<C extends IInventory> extends ICommonRecipe<C> {
  /** @deprecated Item stack output not supported */
  @Override
  @Deprecated
  default ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }

  /** @deprecated Item stack output not supported */
  @Override
  @Deprecated
  default ItemStack getCraftingResult(C inv) {
    return ItemStack.EMPTY;
  }
}
