package com.bilicraft.mantle_fabric.client.book.action.protocol;

import com.bilicraft.mantle_fabric.client.screen.book.BookScreen;

public abstract class ActionProtocol {

  public final String protocol;

  public ActionProtocol(String protocol) {
    this.protocol = protocol;
  }

  public abstract void processCommand(BookScreen book, String param);
}
