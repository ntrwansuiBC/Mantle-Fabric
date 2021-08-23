package com.bilicraft.mantle_fabric.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.TagsProvider;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.bilicraft.mantle_fabric.Mantle;

import javax.annotation.Nullable;
import java.nio.file.Path;

/**
 * Tag provider for all mantle tags, doubles as an item tag provider without need for a block tag provider
 */
public class MantleItemTagProvider extends TagsProvider<Item> {
  protected MantleItemTagProvider(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
    super(dataGenerator, Registry.ITEM, modId, existingFileHelper);
  }

  public MantleItemTagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
    this(dataGenerator, Mantle.modId, existingFileHelper);
  }

  @Override
  public String getName() {
    return "Mantle Item Tags";
  }

  @Override
  protected void registerTags() {
    this.getOrCreateBuilder(MantleTags.Items.OFFHAND_COOLDOWN);
  }

  @Override
  protected Path makePath(ResourceLocation id) {
    return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/items/" + id.getPath() + ".json");
  }
}
