package com.bilicraft.mantle_fabric.client.model.util;

import com.mojang.datafixers.util.Either;
import lombok.AllArgsConstructor;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraftforge.client.model.IModelConfiguration;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

@AllArgsConstructor
public class ModelTextureIteratable implements Iterable<Map<String,Either<RenderMaterial, String>>> {
  /** Initial map for iteration */
  @Nullable
  private final Map<String,Either<RenderMaterial, String>> startMap;
  /** Initial model for iteration */
  @Nullable
  private final BlockModel startModel;

  /**
   * Creates an iterable over the given model
   * @param model  Model
   */
  public ModelTextureIteratable(BlockModel model) {
    this(null, model);
  }

  /**
   *
   * @param owner     Model configuration owner
   * @param fallback  Fallback in case the owner does not contain a block model
   * @return  Iteratable over block model texture maps
   */
  public static ModelTextureIteratable of(IModelConfiguration owner, SimpleBlockModel fallback) {
    IUnbakedModel unbaked = owner.getOwnerModel();
    if (unbaked instanceof BlockModel) {
      return new ModelTextureIteratable(null, (BlockModel)unbaked);
    }
    return new ModelTextureIteratable(fallback.getTextures(), fallback.getParent());
  }

  @Override
  public MapIterator iterator() {
    return new MapIterator(startMap, startModel);
  }

  @AllArgsConstructor
  private static class MapIterator implements Iterator<Map<String,Either<RenderMaterial, String>>> {
    /** Initial map for iteration */
    @Nullable
    private Map<String,Either<RenderMaterial, String>> initial;
    /** current model in the iterator */
    @Nullable
    private BlockModel model;

    @Override
    public boolean hasNext() {
      return initial != null || model != null;
    }

    @Override
    public Map<String,Either<RenderMaterial,String>> next() {
      Map<String,Either<RenderMaterial, String>> map;
      if (initial != null) {
        map = initial;
        initial = null;
      } else if (model != null) {
        map = model.textures;
        model = model.parent;
      } else {
        throw new NoSuchElementException();
      }
      return map;
    }
  }
}
