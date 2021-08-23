package com.bilicraft.mantle_fabric.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.SuggestionProviders;
import net.minecraft.world.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import com.bilicraft.mantle_fabric.Mantle;

import java.util.function.Consumer;

/**
 * Root command for all commands in mantle
 */
public class MantleCommand {
  /** Permission level that allows a user to build in spawn protected areas */
  public static final int PERMISSION_EDIT_SPAWN = 1;
  /** Permission level that can run standard game commands, used by command blocks and functions */
  public static final int PERMISSION_GAME_COMMANDS = 2;
  /** Standard permission level for server operators */
  public static final int PERMISSION_PLAYER_COMMANDS = 3;
  /** Permission level for the server owner, server console, or the player in single player */
  public static final int PERMISSION_OWNER = 4;

  /** Suggestion provider that lists tags for this type */
  public static SuggestionProvider<CommandSource> VALID_TAGS;
  /** Suggestion provider that lists tags values for this registry */
  public static SuggestionProvider<CommandSource> REGISTRY_VALUES;

  /** Registers all Mantle command related content */
  public static void init() {
    // register arguments
    ArgumentTypes.register("mantle:tag_collection", TagCollectionArgument.class, new ArgumentSerializer<>(TagCollectionArgument::collection));
    VALID_TAGS = SuggestionProviders.register(Mantle.getResource("valid_tags"), (context, builder) -> {
      TagCollectionArgument.Result result = context.getArgument("type", TagCollectionArgument.Result.class);
      return ISuggestionProvider.suggestIterable(result.getCollection().getRegisteredTags(), builder);
    });
    REGISTRY_VALUES = SuggestionProviders.register(Mantle.getResource("registry_values"), (context, builder) -> {
      TagCollectionArgument.Result result = context.getArgument("type", TagCollectionArgument.Result.class);
      return ISuggestionProvider.suggestIterable(result.getRegistry().getKeys(), builder);
    });

    // add command listener
    MinecraftForge.EVENT_BUS.addListener(MantleCommand::registerCommand);
  }

  /** Registers a sub command for the root Mantle command */
  private static void register(LiteralArgumentBuilder<CommandSource> root, String name, Consumer<LiteralArgumentBuilder<CommandSource>> consumer) {
    LiteralArgumentBuilder<CommandSource> subCommand = Commands.literal(name);
    consumer.accept(subCommand);
    root.then(subCommand);
  }

  /** Event listener to register the Mantle command */
  private static void registerCommand(RegisterCommandsEvent event) {
    LiteralArgumentBuilder<CommandSource> builder = Commands.literal("mantle");

    // sub commands
    register(builder, "view_tag", ViewTagCommand::register);
    register(builder, "dump_tag", DumpTagCommand::register);
    register(builder, "dump_loot_modifiers", DumpLootModifiers::register);
    register(builder, "dump_all_tags", DumpAllTagsCommand::register);
    register(builder, "tags_for", TagsForCommand::register);

    // register final command
    event.getDispatcher().register(builder);
  }

  /* Helpers */

  /**
   * Returns true if the source either does not have reduced debug info or they have the proper level
   * Allows limiting a command that prints debug info to not work in reduced debug info
   * @param source             Command source
   * @param reducedDebugLevel  Level to use when reduced debug info is true
   * @return  True if the command can be run
   */
  public static boolean requiresDebugInfoOrOp(CommandSource source, int reducedDebugLevel) {
    return !source.getWorld().getGameRules().getBoolean(GameRules.REDUCED_DEBUG_INFO) || source.hasPermissionLevel(reducedDebugLevel);
  }
}
