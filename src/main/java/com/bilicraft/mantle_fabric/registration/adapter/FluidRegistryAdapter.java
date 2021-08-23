package com.bilicraft.mantle_fabric.registration.adapter;

import com.bilicraft.mantle_fabric.registration.DelayedSupplier;
import com.bilicraft.mantle_fabric.registration.FluidBuilder;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

/**
 * Registry adapter for registering fluids
 */
@SuppressWarnings("unused")
public class FluidRegistryAdapter extends RegistryAdapter<Fluid> {
  /** @inheritDoc */
  public FluidRegistryAdapter(IForgeRegistry<Fluid> registry) {
    super(registry);
  }

  /** @inheritDoc */
  public FluidRegistryAdapter(IForgeRegistry<Fluid> registry, String modId) {
    super(registry, modId);
  }

  /**
   * Registers a new fluid with both still and flowing variants
   * @param builder   Fluid properties builder
   * @param still     Still constructor
   * @param flowing   Flowing constructor
   * @param name      Fluid name
   * @param <F>       Fluid type
   * @return  Still fluid instance
   */
  public <F extends ForgeFlowingFluid> F register(FluidBuilder builder, Function<Properties, F> still, Function<Properties,F> flowing, String name) {
    // have to create still and flowing later, as the props need these suppliers
    DelayedSupplier<Fluid> stillDelayed = new DelayedSupplier<>();
    DelayedSupplier<Fluid> flowingDelayed = new DelayedSupplier<>();

    // create props with the suppliers
    Properties props = builder.build(stillDelayed, flowingDelayed);

    // create fluids now that we have props
    F fluid = register(still.apply(props), name);
    stillDelayed.setSupplier(fluid.delegate);
    flowingDelayed.setSupplier(register(flowing.apply(props), "flowing_" + name).delegate);

    // return the final nice object
    return fluid;
  }

  /**
   * Registers a fluid using default constructors
   * @param builder  Fluid builder
   * @param name     Fluid name
   * @return  Still fluid
   */
  public ForgeFlowingFluid register(FluidBuilder builder, String name) {
    return register(builder, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, name);
  }
}
