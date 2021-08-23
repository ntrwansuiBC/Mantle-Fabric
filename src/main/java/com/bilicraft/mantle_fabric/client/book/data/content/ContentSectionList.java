package com.bilicraft.mantle_fabric.client.book.data.content;

import com.bilicraft.mantle_fabric.client.book.data.BookData;
import com.bilicraft.mantle_fabric.client.book.data.SectionData;
import com.bilicraft.mantle_fabric.client.screen.book.BookScreen;
import com.bilicraft.mantle_fabric.client.screen.book.element.BookElement;
import com.bilicraft.mantle_fabric.client.screen.book.element.SelectionElement;

import java.util.ArrayList;

public class ContentSectionList extends PageContent {

  protected ArrayList<SectionData> sections = new ArrayList<>();

  public boolean addSection(SectionData data) {
    return this.sections.size() < 9 && this.sections.add(data);
  }

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
    int width = (SelectionElement.WIDTH + 5) * 3 - 5;
    int height = (SelectionElement.HEIGHT + 5) * 3 - 5;

    int ox = (BookScreen.PAGE_WIDTH - width) / 2;
    int oy = (BookScreen.PAGE_HEIGHT - height) / 2;

    for (int i = 0; i < this.sections.size(); i++) {
      int ix = i % 3;
      int iy = (int) Math.floor(i / 3F);

      int x = ox + ix * (SelectionElement.WIDTH + 5);
      int y = oy + iy * (SelectionElement.HEIGHT + 5);

      list.add(new SelectionElement(x, y, this.sections.get(i)));
    }
  }
}
