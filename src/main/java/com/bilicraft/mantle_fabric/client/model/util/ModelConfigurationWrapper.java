package com.bilicraft.mantle_fabric.client.model.util;

import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.geometry.IModelGeometryPart;

import javax.annotation.Nullable;

/**
 * Wrapper around a {@link IModelConfiguration} instance to allow easier extending, mostly for dynamic textures
 */
@SuppressWarnings("WeakerAccess")
public class ModelConfigurationWrapper implements IModelConfiguration {
  private final IModelConfiguration base;

  /**
   * Creates a new configuration wrapper
   * @param base  Base model configuration
   */
  public ModelConfigurationWrapper(IModelConfiguration base) {
    this.base = base;
  }

  @Nullable
  @Override
  public IUnbakedModel getOwnerModel() {
    return base.getOwnerModel();
  }

  @Override
  public String getModelName() {
    return base.getModelName();
  }

  @Override
  public boolean isTexturePresent(String name) {
    return base.isTexturePresent(name);
  }

  @Override
  public RenderMaterial resolveTexture(String name) {
    return base.resolveTexture(name);
  }

  @Override
  public boolean isShadedInGui() {
    return base.isShadedInGui();
  }

  @Override
  public boolean isSideLit() {
    return base.isSideLit();
  }

  @Override
  public boolean useSmoothLighting() {
    return base.useSmoothLighting();
  }

  @Override
  public ItemCameraTransforms getCameraTransforms() {
    return base.getCameraTransforms();
  }

  @Override
  public IModelTransform getCombinedTransform() {
    return base.getCombinedTransform();
  }

  @Override
  public boolean getPartVisibility(IModelGeometryPart part, boolean fallback) {
    return base.getPartVisibility(part, fallback);
  }
}
