package com.bilicraft.mantle_fabric.client.book.data.content;

import com.bilicraft.mantle_fabric.client.book.data.BookData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.bilicraft.mantle_fabric.client.screen.book.element.BookElement;

import java.util.ArrayList;

@OnlyIn(Dist.CLIENT)
public class ContentDummy extends PageContent {

  @Override
  public void build(BookData book, ArrayList<BookElement> list, boolean rightSide) {
    //TODO load from JSON
  }
}
