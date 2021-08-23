package com.bilicraft.mantle_fabric.client.book.data.content;

import com.bilicraft.mantle_fabric.client.book.data.BookData;
import net.minecraft.client.Minecraft;
import com.bilicraft.mantle_fabric.client.book.data.element.TextData;
import com.bilicraft.mantle_fabric.client.screen.book.BookScreen;
import com.bilicraft.mantle_fabric.client.screen.book.element.BookElement;
import com.bilicraft.mantle_fabric.client.screen.book.element.TextElement;

import java.util.ArrayList;

public class ContentTableOfContents extends PageContent {

  public String title;
  public TextData[] data;

  public ContentTableOfContents(String title, TextData... contents) {
    this.title = title;
    this.data = contents;
  }

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
    int y = 0;

    if (this.title != null && !this.title.trim().isEmpty()) {
      this.addTitle(list, this.title);
      y += TITLE_HEIGHT;
    }

    for (int i = 0; i < this.data.length; i++) {
      TextData text = this.data[i];
      list.add(new TextElement(0, y + i * (int) (Minecraft.getInstance().fontRenderer.FONT_HEIGHT * text.scale), BookScreen.PAGE_WIDTH, Minecraft.getInstance().fontRenderer.FONT_HEIGHT, text));
    }
  }
}
