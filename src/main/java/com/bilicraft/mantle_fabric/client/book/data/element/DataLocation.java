package com.bilicraft.mantle_fabric.client.book.data.element;

import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import com.bilicraft.mantle_fabric.client.book.repository.BookRepository;

public class DataLocation implements IDataElement {

  public String file;
  public transient ResourceLocation location;

  @Override
  public void load(BookRepository source) {
    this.location = "$BLOCK_ATLAS".equals(this.file) ? PlayerContainer.LOCATION_BLOCKS_TEXTURE : source.getResourceLocation(this.file, true);
  }
}
