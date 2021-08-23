package com.bilicraft.mantle_fabric.plugin.jei;

import com.bilicraft.mantle_fabric.client.screen.MultiModuleScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.util.ResourceLocation;
import com.bilicraft.mantle_fabric.Mantle;
import com.bilicraft.mantle_fabric.inventory.MultiModuleContainer;
import com.bilicraft.mantle_fabric.recipe.crafting.ShapedRetexturedRecipe;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
  static IRecipeManager recipeManager;
  static ICraftingGridHelper vanillaCraftingHelper;
  static IModIdHelper modIdHelper;

  @Override
  public ResourceLocation getPluginUid() {
    return Mantle.getResource("jei");
  }

  @Override
  public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registry) {
    registry.getCraftingCategory().addCategoryExtension(ShapedRetexturedRecipe.class, RetexturedRecipeExtension::new);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registration.addGuiContainerHandler(MultiModuleScreen.class, new MultiModuleContainerHandler());
  }

  @Override
  public void registerRecipes(IRecipeRegistration registry) {
    vanillaCraftingHelper = registry.getJeiHelpers().getGuiHelper().createCraftingGridHelper(1);
    modIdHelper = registry.getJeiHelpers().getModIdHelper();
  }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    recipeManager = jeiRuntime.getRecipeManager();
  }

  private static class MultiModuleContainerHandler<C extends MultiModuleContainer<?>> implements IGuiContainerHandler<MultiModuleScreen<C>> {
    @Override
    public List<Rectangle2d> getGuiExtraAreas(MultiModuleScreen<C> guiContainer) {
      return guiContainer.getModuleAreas();
    }
  }
}
