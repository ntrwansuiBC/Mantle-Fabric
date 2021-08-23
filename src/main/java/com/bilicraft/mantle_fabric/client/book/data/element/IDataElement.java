package com.bilicraft.mantle_fabric.client.book.data.element;

import com.bilicraft.mantle_fabric.client.book.repository.BookRepository;

public interface IDataElement {

  void load(BookRepository source);
}
