package com.bilicraft.mantle_fabric.registration.object;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags.IOptionalNamedTag;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Object containing registry entries for a fluid
 * @param <F>  Fluid class
 */
@SuppressWarnings("WeakerAccess")
public class FluidObject<F extends ForgeFlowingFluid> implements Supplier<F>, IItemProvider {
  // TODO: make final in 1.17
  protected ResourceLocation id;
  // TODO: make final in 1.17
  private IOptionalNamedTag<Fluid> localTag;
  private IOptionalNamedTag<Fluid> forgeTag;
  private final Supplier<? extends F> still;
  private final Supplier<? extends F> flowing;
  @Nullable
  private final Supplier<? extends FlowingFluidBlock> block;

  /** Main constructor */
  public FluidObject(ResourceLocation id, String tagName, Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends FlowingFluidBlock> block) {
    this.id = id;
    this.localTag = FluidTags.createOptional(id);
    this.forgeTag = FluidTags.createOptional(new ResourceLocation("forge", tagName));
    this.still = still;
    this.flowing = flowing;
    this.block = block;
  }

  /** @deprecated Use constructor with id and tag name parameter */
  @Deprecated
  public FluidObject(Supplier<? extends F> still, Supplier<? extends F> flowing, @Nullable Supplier<? extends FlowingFluidBlock> block) {
    this.still = still;
    this.flowing = flowing;
    this.block = block;
  }

  /** Gets the ID for this fluid object */
  public ResourceLocation getId() {
    if (id == null) {
      id = Objects.requireNonNull(getStill().getRegistryName(), "Fluid has null ID");
    }
    return id;
  }

  /**
   * Gets the still form of this fluid
   * @return  Still form
   */
  public F getStill() {
    return Objects.requireNonNull(still.get(), "Fluid object missing still fluid");
  }

  @Override
  public F get() {
    return getStill();
  }

  /**
   * Gets the flowing form of this fluid
   * @return  flowing form
   */
  public F getFlowing() {
    return Objects.requireNonNull(flowing.get(), "Fluid object missing flowing fluid");
  }

  /**
   * Gets the block form of this fluid
   * @return  Block form
   */
  @Nullable
  public FlowingFluidBlock getBlock() {
    if (block == null) {
      return null;
    }
    return block.get();
  }

  /**
   * Gets the bucket form of this fluid
   * @return  Bucket form
   */
  @Override
  public Item asItem() {
    return still.get().getFilledBucket();
  }

  /** Gets the mod local tag for this fluid object */
  public IOptionalNamedTag<Fluid> getLocalTag() {
    if (localTag == null) {
      localTag = FluidTags.createOptional(getId());
    }
    return localTag;
  }

  /** Gets the shared forge tag for this fluid object */
  public IOptionalNamedTag<Fluid> getForgeTag() {
    if (forgeTag == null) {
      forgeTag = FluidTags.createOptional(new ResourceLocation("forge", getId().getPath()));
    }
    return forgeTag;
  }
}
