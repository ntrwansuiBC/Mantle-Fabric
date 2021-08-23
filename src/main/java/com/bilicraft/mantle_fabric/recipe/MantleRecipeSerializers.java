package com.bilicraft.mantle_fabric.recipe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.registries.ObjectHolder;
import com.bilicraft.mantle_fabric.Mantle;

import static com.bilicraft.mantle_fabric.registration.RegistrationHelper.injected;

/**
 * All recipe serializers registered under Mantles name
 */
@ObjectHolder(Mantle.modId)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MantleRecipeSerializers {
  public static final IRecipeSerializer<?> CRAFTING_SHAPED_FALLBACK = injected();
  public static final IRecipeSerializer<?> CRAFTING_SHAPED_RETEXTURED = injected();
}
