package com.bilicraft.mantle_fabric.network.packet;

import com.bilicraft.mantle_fabric.client.book.BookHelper;
import lombok.RequiredArgsConstructor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * Packet to update the page in a book in the players hand
 */
@RequiredArgsConstructor
public class UpdateHeldPagePacket implements IThreadsafePacket {
  private final Hand hand;
  private final String page;
  public UpdateHeldPagePacket(PacketBuffer buffer) {
    this.hand = buffer.readEnumValue(Hand.class);
    this.page = buffer.readString(100);
  }

  @Override
  public void encode(PacketBuffer buf) {
    buf.writeEnumValue(hand);
    buf.writeString(this.page);
  }

  @Override
  public void handleThreadsafe(Context context) {
    PlayerEntity player = context.getSender();
    if (player != null && this.page != null) {
      ItemStack stack = player.getHeldItem(hand);
      if (!stack.isEmpty()) {
        BookHelper.writeSavedPageToBook(stack, this.page);
      }
    }
  }
}
