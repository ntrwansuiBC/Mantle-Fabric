package com.bilicraft.mantle_fabric;

import com.bilicraft.mantle_fabric.command.MantleCommand;
import com.bilicraft.mantle_fabric.config.Config;
import com.bilicraft.mantle_fabric.data.MantleItemTagProvider;
import com.bilicraft.mantle_fabric.data.MantleTags;
import com.bilicraft.mantle_fabric.item.LecternBookItem;
import com.bilicraft.mantle_fabric.loot.AddEntryLootModifier;
import com.bilicraft.mantle_fabric.loot.MantleLoot;
import com.bilicraft.mantle_fabric.loot.ReplaceItemLootModifier;
import com.bilicraft.mantle_fabric.network.MantleNetwork;
import com.bilicraft.mantle_fabric.recipe.crafting.ShapedFallbackRecipe;
import com.bilicraft.mantle_fabric.recipe.crafting.ShapedRetexturedRecipe;
import com.bilicraft.mantle_fabric.recipe.ingredient.IngredientIntersection;
import com.bilicraft.mantle_fabric.recipe.ingredient.IngredientWithout;
import com.bilicraft.mantle_fabric.registration.RegistrationHelper;
import com.bilicraft.mantle_fabric.registration.adapter.RegistryAdapter;
import com.bilicraft.mantle_fabric.util.OffhandCooldownTracker;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Mantle
 *
 * Central mod object for Mantle
 *
 * @author Sunstrike <sun@sunstrike.io>
 */
@Mod(Mantle.modId)
public class Mantle {
  public static final String modId = "mantle";
  public static final Logger logger = LogManager.getLogger("Mantle");

  /* Instance of this mod, used for grabbing prototype fields */
  public static Mantle instance;

  /* Proxies for sides, used for graphics processing */
  public Mantle() {
    ModLoadingContext.get().registerConfig(Type.CLIENT, Config.CLIENT_SPEC);
    ModLoadingContext.get().registerConfig(Type.SERVER, Config.SERVER_SPEC);

    instance = this;
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    bus.addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, this::commonSetup);
    bus.addListener(EventPriority.NORMAL, false, GatherDataEvent.class, this::gatherData);
    bus.addListener(EventPriority.NORMAL, false, ModConfig.ModConfigEvent.class, Config::configChanged);
    bus.addGenericListener(IRecipeSerializer.class, this::registerRecipeSerializers);
    bus.addGenericListener(GlobalLootModifierSerializer.class, this::registerGlobalLootModifiers);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, false, PlayerInteractEvent.RightClickBlock.class, LecternBookItem::interactWithBlock);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {
    MantleNetwork.registerPackets();
    MantleCommand.init();
    OffhandCooldownTracker.register();
    MantleTags.init();

    // inject our new signs into the tile entity type
    event.enqueueWork(() -> {
      ImmutableSet.Builder<Block> builder = ImmutableSet.builder();
      builder.addAll(TileEntityType.SIGN.validBlocks);
      RegistrationHelper.forEachSignBlock(builder::add);
      TileEntityType.SIGN.validBlocks = builder.build();
    });
  }

  private void gatherData(final GatherDataEvent event) {
    if (event.includeServer()) {
      DataGenerator dataGenerator = event.getGenerator();
      dataGenerator.addProvider(new MantleItemTagProvider(dataGenerator, event.getExistingFileHelper()));
    }
  }

  private void registerRecipeSerializers(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
    RegistryAdapter<IRecipeSerializer<?>> adapter = new RegistryAdapter<>(event.getRegistry());
    adapter.register(new ShapedFallbackRecipe.Serializer(), "crafting_shaped_fallback");
    adapter.register(new ShapedRetexturedRecipe.Serializer(), "crafting_shaped_retextured");

    CraftingHelper.register(IngredientWithout.ID, IngredientWithout.SERIALIZER);
    CraftingHelper.register(IngredientIntersection.ID, IngredientIntersection.SERIALIZER);

    // done here as no dedicated event
    MantleLoot.register();
  }

  private void registerGlobalLootModifiers(final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
    RegistryAdapter<GlobalLootModifierSerializer<?>> adapter = new RegistryAdapter<>(event.getRegistry());
    adapter.register(new AddEntryLootModifier.Serializer(), "add_entry");
    adapter.register(new ReplaceItemLootModifier.Serializer(), "replace_item");
  }

  /**
   * Gets a resource location for Mantle
   * @param name  Name
   * @return  Resource location instance
   */
  public static ResourceLocation getResource(String name) {
    return new ResourceLocation(modId, name);
  }
}
