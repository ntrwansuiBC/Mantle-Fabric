package com.bilicraft.mantle_fabric.network;

import net.minecraftforge.fml.network.NetworkDirection;
import com.bilicraft.mantle_fabric.Mantle;
import com.bilicraft.mantle_fabric.network.packet.DropLecternBookPacket;
import com.bilicraft.mantle_fabric.network.packet.OpenLecternBookPacket;
import com.bilicraft.mantle_fabric.network.packet.SwingArmPacket;
import com.bilicraft.mantle_fabric.network.packet.UpdateHeldPagePacket;
import com.bilicraft.mantle_fabric.network.packet.UpdateLecternPagePacket;

public class MantleNetwork {
  /** Network instance */
  public static final NetworkWrapper INSTANCE = new NetworkWrapper(Mantle.getResource("network"));

  /**
   * Registers packets into this network
   */
  public static void registerPackets() {
    INSTANCE.registerPacket(OpenLecternBookPacket.class, OpenLecternBookPacket::new, NetworkDirection.PLAY_TO_CLIENT);
    INSTANCE.registerPacket(UpdateHeldPagePacket.class, UpdateHeldPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(UpdateLecternPagePacket.class, UpdateLecternPagePacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(DropLecternBookPacket.class, DropLecternBookPacket::new, NetworkDirection.PLAY_TO_SERVER);
    INSTANCE.registerPacket(SwingArmPacket.class, SwingArmPacket::new, NetworkDirection.PLAY_TO_CLIENT);
  }
}
