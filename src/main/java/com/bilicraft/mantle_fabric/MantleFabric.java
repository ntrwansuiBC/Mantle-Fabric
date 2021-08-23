package com.bilicraft.mantle_fabric;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MantleFabric implements ModInitializer {
	public static final String modId = "mantle_fabric";
	public static final Logger logger = LogManager.getLogger("MantleFabric");

	public static MantleFabric instance;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		instance = this;
		System.out.println("Hello Fabric world!");
	}
}
