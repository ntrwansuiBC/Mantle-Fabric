package com.bilicraft.mantle_fabric.client.book.data.content;

import com.bilicraft.mantle_fabric.client.book.data.BookData;
import com.bilicraft.mantle_fabric.client.book.data.PageData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.bilicraft.mantle_fabric.client.book.data.element.TextData;
import com.bilicraft.mantle_fabric.client.book.repository.BookRepository;
import com.bilicraft.mantle_fabric.client.screen.book.BookScreen;
import com.bilicraft.mantle_fabric.client.screen.book.element.BookElement;
import com.bilicraft.mantle_fabric.client.screen.book.element.TextElement;

import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public abstract class PageContent {

  public static final transient int TITLE_HEIGHT = 16;

  public transient PageData parent;
  public transient BookRepository source;

  public void load() {
  }

  public abstract void build(BookData book, ArrayList<BookElement> list, boolean rightSide);

  public void addTitle(ArrayList<BookElement> list, String title) {
    TextData tdTitle = new TextData(title);
    tdTitle.underlined = true;
    this.addTitle(list, new TextData[]{tdTitle});
  }

  public void addTitle(ArrayList<BookElement> list, TextData[] title) {
    list.add(new TextElement(0, 0, BookScreen.PAGE_WIDTH, 9, title));
  }
}
