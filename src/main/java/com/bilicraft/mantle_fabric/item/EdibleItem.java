package com.bilicraft.mantle_fabric.item;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.bilicraft.mantle_fabric.util.TranslationHelper;

import javax.annotation.Nullable;
import java.util.List;

public class EdibleItem extends Item {

  /** if false, does not display effects of food in tooltip */
  private boolean displayEffectsTooltip;

  public EdibleItem(Food foodIn, ItemGroup itemGroup) {
    this(foodIn, itemGroup, true);
  }

  public EdibleItem(Food foodIn, ItemGroup itemGroup, boolean displayEffectsTooltip) {
    super(new Properties().food(foodIn).group(itemGroup));
    this.displayEffectsTooltip = displayEffectsTooltip;
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    TranslationHelper.addOptionalTooltip(stack, tooltip);

    if (this.displayEffectsTooltip) {
      for (Pair<EffectInstance, Float> pair : stack.getItem().getFood().getEffects()) {
        if (pair.getFirst() != null) {
          tooltip.add(new StringTextComponent(I18n.format(pair.getFirst().getEffectName()).trim()).mergeStyle(TextFormatting.GRAY));
        }
      }
    }

    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
}
