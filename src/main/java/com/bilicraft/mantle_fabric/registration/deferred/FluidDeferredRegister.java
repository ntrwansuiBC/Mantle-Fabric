package com.bilicraft.mantle_fabric.registration.deferred;

import com.bilicraft.mantle_fabric.registration.DelayedSupplier;
import com.bilicraft.mantle_fabric.registration.FluidBuilder;
import com.bilicraft.mantle_fabric.registration.ItemProperties;
import com.bilicraft.mantle_fabric.registration.object.FluidObject;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid.Properties;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings({"unused", "WeakerAccess"})
public class FluidDeferredRegister extends DeferredRegisterWrapper<Fluid> {
  private final DeferredRegister<Block> blockRegister;
  private final DeferredRegister<Item> itemRegister;
  public FluidDeferredRegister(String modID) {
    super(ForgeRegistries.FLUIDS, modID);
    this.blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modID);
    this.itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modID);
  }

  @Override
  public void register(IEventBus bus) {
    super.register(bus);
    blockRegister.register(bus);
    itemRegister.register(bus);
  }

  /**
   * Registers a fluid to the registry
   * @param name  Name of the fluid to register
   * @param sup   Fluid supplier
   * @param <I>   Fluid type
   * @return  Fluid to supply
   */
  public <I extends Fluid> RegistryObject<I> registerFluid(final String name, final Supplier<? extends I> sup) {
    return register.register(name, sup);
  }

  /**
   * Registers a fluid with still, flowing, block, and bucket
   * @param name     Fluid name
   * @param tagName  Name for tagging under forge
   * @param builder  Properties builder
   * @param still    Function to create still from the properties
   * @param flowing  Function to create flowing from the properties
   * @param block    Function to create block from the fluid supplier
   * @param <F>      Fluid type
   * @return  Fluid object
   */
  public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidBuilder builder, Function<Properties,? extends F> still,
                                                               Function<Properties,? extends F> flowing, Function<Supplier<? extends FlowingFluid>,? extends FlowingFluidBlock> block) {

    // have to create still and flowing later, as the props need these suppliers
    DelayedSupplier<F> stillDelayed = new DelayedSupplier<>();
    DelayedSupplier<F> flowingDelayed = new DelayedSupplier<>();

    // create block and bucket, they just need a still supplier
    RegistryObject<FlowingFluidBlock> blockObj = blockRegister.register(name + "_fluid", () -> block.apply(stillDelayed));
    builder.bucket(itemRegister.register(name + "_bucket", () -> new BucketItem(stillDelayed, ItemProperties.BUCKET_PROPS)));

    // create props with the suppliers
    Properties props = builder.block(blockObj).build(stillDelayed, flowingDelayed);

    // create fluids now that we have props
    Supplier<F> stillSup = registerFluid(name, () -> still.apply(props));
    stillDelayed.setSupplier(stillSup);
    Supplier<F> flowingSup = registerFluid("flowing_" + name, () -> flowing.apply(props));
    flowingDelayed.setSupplier(flowingSup);

    // return the final nice object
    return new FluidObject<>(resource(name), tagName, stillSup, flowingSup, blockObj);
  }

  /**
   * Registers a fluid with still, flowing, block, bucket, and a common forgen name
   * @param name     Fluid name
   * @param builder  Properties builder
   * @param still    Function to create still from the properties
   * @param flowing  Function to create flowing from the properties
   * @param block    Function to create block from the fluid supplier
   * @param <F>      Fluid type
   * @return  Fluid object
   */
  public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidBuilder builder, Function<Properties,? extends F> still,
      Function<Properties,? extends F> flowing, Function<Supplier<? extends FlowingFluid>,? extends FlowingFluidBlock> block) {
    return register(name, name, builder, still, flowing, block);
  }

  /**
   * Registers a fluid with still, flowing, block, and bucket using the default fluid block
   * @param name       Fluid name
   * @param tagName    Name for tagging under forge
   * @param builder    Properties builder
   * @param still      Function to create still from the properties
   * @param flowing    Function to create flowing from the properties
   * @param material   Block material
   * @param lightLevel Block light level
   * @param <F>      Fluid type
   * @return  Fluid object
   */
  public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, String tagName, FluidAttributes.Builder builder,
      Function<Properties,? extends F> still, Function<Properties,? extends F> flowing, Material material, int lightLevel) {
    return register(
      name, tagName, new FluidBuilder(builder).explosionResistance(100f), still, flowing,
      (fluid) -> new FlowingFluidBlock(fluid, Block.Properties.create(material).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops().setLightLevel((state) -> lightLevel))
    );
  }

  /**
   * Registers a fluid with still, flowing, block, and bucket using the default fluid block
   * @param name       Fluid name
   * @param builder    Properties builder
   * @param still      Function to create still from the properties
   * @param flowing    Function to create flowing from the properties
   * @param material   Block material
   * @param lightLevel Block light level
   * @param <F>      Fluid type
   * @return  Fluid object
   */
  public <F extends ForgeFlowingFluid> FluidObject<F> register(String name, FluidAttributes.Builder builder,
      Function<Properties,? extends F> still, Function<Properties,? extends F> flowing, Material material, int lightLevel) {
    return register(name, name, builder, still, flowing, material, lightLevel);
  }

  /**
   * Registers a fluid with generic still, flowing, block, and bucket using the default Forge fluid
   * @param name       Fluid name
   * @param tagName    Name for tagging under forge
   * @param builder    Properties builder
   * @param material   Block material
   * @param lightLevel Block light level
   * @return  Fluid object
   */
  public FluidObject<ForgeFlowingFluid> register(String name, String tagName, FluidAttributes.Builder builder, Material material, int lightLevel) {
    return register(name, tagName, builder, ForgeFlowingFluid.Source::new, ForgeFlowingFluid.Flowing::new, material, lightLevel);
  }

  /**
   * Registers a fluid with generic still, flowing, block, and bucket using the default Forge fluid
   * @param name       Fluid name
   * @param builder    Properties builder
   * @param material   Block material
   * @param lightLevel Block light level
   * @return  Fluid object
   */
  public FluidObject<ForgeFlowingFluid> register(String name, FluidAttributes.Builder builder, Material material, int lightLevel) {
    return register(name, name, builder, material, lightLevel);
  }
}
