package com.bilicraft.mantle_fabric.client.book.repository;

import com.bilicraft.mantle_fabric.client.book.data.SectionData;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class DummyRepository extends BookRepository {

  @Override
  public List<SectionData> getSections() {
    return Collections.emptyList();
  }

  @Override
  public ResourceLocation getResourceLocation(@Nullable String path, boolean safe) {
    return null;
  }

  @Override
  public IResource getResource(@Nullable ResourceLocation loc) {
    return null;
  }

  @Override
  public boolean resourceExists(@Nullable ResourceLocation location) {
    return false;
  }

  @Override
  public String resourceToString(@Nullable IResource resource, boolean skipComments) {
    return "";
  }
}
