package com.bilicraft.mantle_fabric.recipe.data;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Common logic to create a recipe builder class
 * @param <T>
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbstractRecipeBuilder<T extends AbstractRecipeBuilder<T>> {
  /** Advancement builder for this class */
  protected final Advancement.Builder advancementBuilder = Advancement.Builder.builder();
  /** Group for this recipe */
  @Nonnull
  protected String group = "";

  /**
   * Adds a criteria to the recipe
   * @param name      Criteria name
   * @param criteria  Criteria instance
   * @return  Builder
   */
  @SuppressWarnings("unchecked")
  public T addCriterion(String name, ICriterionInstance criteria) {
    this.advancementBuilder.withCriterion(name, criteria);
    return (T)this;
  }

  /**
   * Sets the group for this recipe
   * @param group  Recipe group
   * @return  Builder
   */
  @SuppressWarnings("unchecked")
  public T setGroup(String group) {
    this.group = group;
    return (T)this;
  }

  /**
   * Sets the group for this recipe
   * @param group  Recipe resource location group
   * @return  Builder
   */
  public T setGroup(ResourceLocation group) {
    // if minecraft, no namepsace. Groups are technically not namespaced so this is for consistency with vanilla
    if ("minecraft".equals(group.getNamespace())) {
      return setGroup(group.getPath());
    }
    return setGroup(group.toString());
  }

  /**
   * Builds the recipe with a default recipe ID, typically based on the output
   * @param consumerIn  Recipe consumer
   */
  public abstract void build(Consumer<IFinishedRecipe> consumerIn);

  /**
   * Builds the recipe
   * @param consumerIn  Recipe consumer
   * @param id          Recipe ID
   */
  public abstract void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id);

  /**
   * Base logic for advancement building
   * @param id      Recipe ID
   * @param folder  Group folder for saving recipes. Vanilla typically uses item groups, but for mods might as well base on the recipe
   * @return Advancement ID
   */
  private ResourceLocation buildAdvancementInternal(ResourceLocation id, String folder) {
    this.advancementBuilder
        .withParentId(new ResourceLocation("recipes/root"))
        .withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
        .withRewards(AdvancementRewards.Builder.recipe(id))
        .withRequirementsStrategy(IRequirementsStrategy.OR);
    return new ResourceLocation(id.getNamespace(), "recipes/" + folder + "/" + id.getPath());
  }

  /**
   * Builds and validates the advancement, intended to be called in {@link #build(Consumer, ResourceLocation)}
   * @param id      Recipe ID
   * @param folder  Group folder for saving recipes. Vanilla typically uses item groups, but for mods might as well base on the recipe
   * @return Advancement ID
   */
  protected ResourceLocation buildAdvancement(ResourceLocation id, String folder) {
    if (this.advancementBuilder.getCriteria().isEmpty()) {
      throw new IllegalStateException("No way of obtaining recipe " + id);
    }
    return buildAdvancementInternal(id, folder);
  }

  /**
   * Builds an optional advancement, intended to be called in {@link #build(Consumer, ResourceLocation)}
   * @param id        Recipe ID
   * @param folder    Group folder for saving recipes. Vanilla typically uses item groups, but for mods might as well base on the recipe
   * @return Advancement ID, or null if the advancement was not defined
   */
  @Nullable
  protected ResourceLocation buildOptionalAdvancement(ResourceLocation id, String folder) {
    if (this.advancementBuilder.getCriteria().isEmpty()) {
      return null;
    }
    return buildAdvancementInternal(id, folder);
  }

  /** Class to implement basic finished recipe methods */
  @RequiredArgsConstructor
  protected abstract class AbstractFinishedRecipe implements IFinishedRecipe {
    @Getter
    private final ResourceLocation ID;
    @Getter @Nullable
    private final ResourceLocation advancementID;

    @Nullable
    @Override
    public JsonObject getAdvancementJson() {
      if (advancementID == null) {
        return null;
      }
      return advancementBuilder.serialize();
    }
  }
}
