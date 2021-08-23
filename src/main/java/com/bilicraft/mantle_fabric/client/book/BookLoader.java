package com.bilicraft.mantle_fabric.client.book;

import com.bilicraft.mantle_fabric.client.book.data.BookData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import com.bilicraft.mantle_fabric.client.book.action.StringActionProcessor;
import com.bilicraft.mantle_fabric.client.book.action.protocol.ProtocolGoToPage;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentBlank;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentBlockInteraction;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentCrafting;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentImage;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentImageText;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentSmelting;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentSmithing;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentStructure;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentText;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentTextImage;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentTextLeftImage;
import com.bilicraft.mantle_fabric.client.book.data.content.ContentTextRightImage;
import com.bilicraft.mantle_fabric.client.book.data.content.PageContent;
import com.bilicraft.mantle_fabric.client.book.data.deserializer.HexStringDeserializer;
import com.bilicraft.mantle_fabric.client.book.repository.BookRepository;
import com.bilicraft.mantle_fabric.network.MantleNetwork;
import com.bilicraft.mantle_fabric.network.packet.UpdateHeldPagePacket;
import com.bilicraft.mantle_fabric.network.packet.UpdateLecternPagePacket;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.function.Predicate;

@OnlyIn(Dist.CLIENT)
public class BookLoader implements ISelectiveResourceReloadListener {

  /**
   * GSON object to be used for book loading purposes
   */
  public static final Gson GSON = new GsonBuilder().registerTypeAdapter(int.class, new HexStringDeserializer()).create();

  /**
   * Maps page content presets to names
   */
  private static final HashMap<String, Class<? extends PageContent>> typeToContentMap = new HashMap<>();

  /**
   * Internal registry of all books for the purposes of the reloader, maps books to name
   */
  private static final HashMap<String, BookData> books = new HashMap<>();

  public BookLoader() {
    // Register page types
    registerPageType("blank", ContentBlank.class);
    registerPageType("text", ContentText.class);
    registerPageType("image", ContentImage.class);
    registerPageType("image with text below", ContentImageText.class);
    registerPageType("text with image below", ContentTextImage.class);
    registerPageType("text with left image etch", ContentTextLeftImage.class);
    registerPageType("text with right image etch", ContentTextRightImage.class);
    registerPageType("crafting", ContentCrafting.class);
    registerPageType("smelting", ContentSmelting.class);
    registerPageType("smithing", ContentSmithing.class);
    registerPageType("block interaction", ContentBlockInteraction.class);
    registerPageType(ContentStructure.ID, ContentStructure.class);

    // Register action protocols
    StringActionProcessor.registerProtocol(new ProtocolGoToPage());
    StringActionProcessor.registerProtocol(new ProtocolGoToPage(true, ProtocolGoToPage.GO_TO_RTN));
  }

  /**
   * Registers a type of page prefabricate
   *
   * @param name  The name of the page type
   * @param clazz The PageContent class for this page type
   * @RecommendedInvoke init
   */
  public static void registerPageType(String name, Class<? extends PageContent> clazz) {
    if (typeToContentMap.containsKey(name)) {
      throw new IllegalArgumentException("Page type " + name + " already in use.");
    }

    typeToContentMap.put(name, clazz);
  }

  /**
   * Gets a type of page prefabricate by name
   *
   * @param name The name of the page type
   * @return The class of the page type, ContentError.class if page type not registered
   */
  @Nullable
  public static Class<? extends PageContent> getPageType(String name) {
    return typeToContentMap.get(name);
  }

  /**
   * Adds a book to the loader, and returns a reference object
   * Be warned that the returned BookData object is not immediately populated, and is instead populated when the resources are loaded/reloaded
   *
   * @param name         The name of the book, modid: will be automatically appended to the front of the name unless that is already added
   * @param repositories All the repositories the book will load the sections from
   * @return The book object, not immediately populated
   */
  public static BookData registerBook(String name, BookRepository... repositories) {
    return registerBook(name, true, true, repositories);
  }

  /**
   * Adds a book to the loader, and returns a reference object
   * Be warned that the returned BookData object is not immediately populated, and is instead populated when the resources are loaded/reloaded
   *
   * @param name               The name of the book, modid: will be automatically appended to the front of the name unless that is already added
   * @param appendIndex        Whether an index should be added to the front of the book using a BookTransformer
   * @param appendContentTable Whether a table of contents should be added to the front of each section using a BookTransformer
   * @param repositories       All the repositories the book will load the sections from
   * @return The book object, not immediately populated
   */
  public static BookData registerBook(String name, boolean appendIndex, boolean appendContentTable, BookRepository... repositories) {
    BookData info = new BookData(repositories);

    books.put(name.contains(":") ? name : ModLoadingContext.get().getActiveContainer().getNamespace() + ":" + name, info);

    if (appendIndex) {
      info.addTransformer(BookTransformer.indexTranformer());
    }
    if (appendContentTable) {
      info.addTransformer(BookTransformer.contentTableTransformer());
    }

    return info;
  }

  /**
   * Updates the saved page of a held book
   * @param player  Player instance
   * @param hand    Hand
   * @param page    New page
   */
  public static void updateSavedPage(@Nullable PlayerEntity player, Hand hand, String page) {
    if (player != null) {
      ItemStack item = player.getHeldItem(hand);
      if (!item.isEmpty()) {
        BookHelper.writeSavedPageToBook(item, page);
        MantleNetwork.INSTANCE.network.sendToServer(new UpdateHeldPagePacket(hand, page));
      }
    }
  }

  /**
   * Updates the saved page of a held book
   * @param pos     Position being changed
   * @param page    New page
   */
  public static void updateSavedPage(BlockPos pos, String page) {
    MantleNetwork.INSTANCE.network.sendToServer(new UpdateLecternPagePacket(pos, page));
  }

  /**
   * Reloads all the books, called when the resource manager reloads, such as when the resource pack or the language is changed
   */
  @Override
  public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
    books.forEach((s, bookData) -> bookData.reset());
  }
}
