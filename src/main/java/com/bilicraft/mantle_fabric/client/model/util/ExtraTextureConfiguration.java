package com.bilicraft.mantle_fabric.client.model.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoaderRegistry;

import java.util.Map;

/**
 * Model configuration wrapper to add in an extra set of textures
 */
public class ExtraTextureConfiguration extends ModelConfigurationWrapper {
  private final Map<String,RenderMaterial> textures;

  /**
   * Creates a new wrapper using the given textures
   * @param base      Base configuration
   * @param textures  Textures map, any textures in this map will take precedence over those in the base configuration
   */
  public ExtraTextureConfiguration(IModelConfiguration base, Map<String,RenderMaterial> textures) {
    super(base);
    this.textures = textures;
  }

  /**
   * Creates a new wrapper for a single texture
   * @param base     Base configuration
   * @param name     Texture name, if it matches texture is returned
   * @param texture  Texture path
   */
  public ExtraTextureConfiguration(IModelConfiguration base, String name, ResourceLocation texture) {
    super(base);
    this.textures = ImmutableMap.of(name, ModelLoaderRegistry.blockMaterial(texture));
  }

  @Override
  public RenderMaterial resolveTexture(String name) {
    RenderMaterial connected = textures.get(name);
    if (connected != null) {
      return connected;
    }
    return super.resolveTexture(name);
  }

  @Override
  public boolean isTexturePresent(String name) {
    return textures.containsKey(name) || super.isTexturePresent(name);
  }
}
