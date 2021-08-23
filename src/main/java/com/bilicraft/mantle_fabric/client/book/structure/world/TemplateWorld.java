// Credit to Immersive Engineering and blusunrize for this class
// See: https://github.com/BluSunrize/ImmersiveEngineering/blob/1.16.5/src/main/java/blusunrize/immersiveengineering/common/util/fakeworld/TemplateWorld.java
package com.bilicraft.mantle_fabric.client.book.structure.world;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
import net.minecraft.world.EmptyTickList;
import net.minecraft.world.ITickList;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import net.minecraft.world.storage.MapData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class TemplateWorld extends World {

  private final Map<String, MapData> maps = new HashMap<>();
  private final Scoreboard scoreboard = new Scoreboard();
  private final RecipeManager recipeManager = new RecipeManager();
  private final TemplateChunkProvider chunkProvider;
  private final DynamicRegistries registries = DynamicRegistries.func_239770_b_();

  public TemplateWorld(List<BlockInfo> blocks, Predicate<BlockPos> shouldShow) {
    super(
      new FakeSpawnInfo(), World.OVERWORLD, DimensionType.OVERWORLD_TYPE,
      () -> EmptyProfiler.INSTANCE, true, false, 0
    );

    this.chunkProvider = new TemplateChunkProvider(blocks, this, shouldShow);
  }

  @Override
  public void notifyBlockUpdate(@Nonnull BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newState, int flags) {
  }

  @Override
  public void playSound(@Nullable PlayerEntity player, double x, double y, double z, @Nonnull SoundEvent soundIn, @Nonnull SoundCategory category, float volume, float pitch) {
  }

  @Override
  public void playMovingSound(@Nullable PlayerEntity playerIn, @Nonnull Entity entityIn, @Nonnull SoundEvent eventIn, @Nonnull SoundCategory categoryIn, float volume, float pitch) {
  }

  @Nullable
  @Override
  public Entity getEntityByID(int id) {
    return null;
  }

  @Nullable
  @Override
  public MapData getMapData(@Nonnull String mapName) {
    return this.maps.get(mapName);
  }

  @Override
  public void registerMapData(@Nonnull MapData mapDataIn) {
    this.maps.put(mapDataIn.getName(), mapDataIn);
  }

  @Override
  public int getNextMapId() {
    return this.maps.size();
  }

  @Override
  public void sendBlockBreakProgress(int breakerId, @Nonnull BlockPos pos, int progress) {
  }

  @Nonnull
  @Override
  public Scoreboard getScoreboard() {
    return this.scoreboard;
  }

  @Nonnull
  @Override
  public RecipeManager getRecipeManager() {
    return this.recipeManager;
  }

  @Nonnull
  @Override
  public ITagCollectionSupplier getTags() {
    return ITagCollectionSupplier.TAG_COLLECTION_SUPPLIER;
  }

  @Nonnull
  @Override
  public ITickList<Block> getPendingBlockTicks() {
    return EmptyTickList.get();
  }

  @Nonnull
  @Override
  public ITickList<Fluid> getPendingFluidTicks() {
    return EmptyTickList.get();
  }

  @Nonnull
  @Override
  public AbstractChunkProvider getChunkProvider() {
    return this.chunkProvider;
  }

  @Override
  public void playEvent(@Nullable PlayerEntity player, int type, @Nonnull BlockPos pos, int data) {
  }

  @Nonnull
  @Override
  public DynamicRegistries func_241828_r() {
    return this.registries;
  }

  @Override
  public float func_230487_a_(@Nonnull Direction p_230487_1_, boolean p_230487_2_) {
    return 0;
  }

  @Nonnull
  @Override
  public List<? extends PlayerEntity> getPlayers() {
    return ImmutableList.of();
  }

  @Nonnull
  @Override
  public Biome getNoiseBiomeRaw(int x, int y, int z) {
    return this.func_241828_r().getRegistry(Registry.BIOME_KEY).getOrThrow(Biomes.PLAINS);
  }
}
