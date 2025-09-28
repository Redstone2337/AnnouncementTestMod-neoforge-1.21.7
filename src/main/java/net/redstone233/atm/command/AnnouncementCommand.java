package net.redstone233.atm.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.redstone233.atm.config.Config;
import net.redstone233.atm.config.ConfigManager;
import net.redstone233.atm.screen.AnnouncementScreen;

public class AnnouncementCommand {

    private static Config config;

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("atm")
                .requires(src -> src.hasPermission(1))
                .then(Commands.literal("display")
                        .then(Commands.argument("isDisplay", BoolArgumentType.bool())
                                .executes(run -> executeDisplay(
                                                run.getSource(),
                                                BoolArgumentType.getBool(run, "isDisplay")
                                        )
                                )
                        )
                )
                .then(Commands.literal("settings")
                        .then(Commands.literal("mainTitle")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("messages", MessageArgument.message())
                                                .executes(run -> executeSetMainTitle(
                                                        run.getSource(),
                                                        MessageArgument.getMessage(run, "messages")
                                                ))
                                        )
                                        .then(Commands.literal("empty")
                                                .then(Commands.argument("isEmpty", BoolArgumentType.bool())
                                                        .executes(run -> executeEmptyMainTitle(
                                                                run.getSource(),
                                                                BoolArgumentType.getBool(run, "isEmpty")
                                                        ))
                                                )
                                        )
                                )
                                .then(Commands.literal("reset")
                                        .then(Commands.argument("refurnish", BoolArgumentType.bool())
                                                .executes(run -> executeResetMainTitle(
                                                        run.getSource(),
                                                        BoolArgumentType.getBool(run, "refurnish")
                                                ))
                                        )
                                )
                        )
                        .then(Commands.literal("subTitle")
                                .then(Commands.literal("set")
                                        .then(Commands.argument("messages", MessageArgument.message())
                                                .executes(run -> executeSetSubTitle(
                                                        run.getSource(),
                                                        MessageArgument.getMessage(run, "messages")
                                                ))
                                        )
                                        .then(Commands.literal("empty")
                                                .then(Commands.argument("isEmpty", BoolArgumentType.bool())
                                                        .executes(run -> executeEmptySubTitle(
                                                                run.getSource(),
                                                                BoolArgumentType.getBool(run, "isEmpty")
                                                        ))
                                                )
                                        )
                                )
                                .then(Commands.literal("reset")
                                        .then(Commands.argument("refurnish", BoolArgumentType.bool())
                                                .executes(run -> executeResetSubTitle(
                                                        run.getSource(),
                                                        BoolArgumentType.getBool(run, "refurnish")
                                                ))
                                        )
                                )
                        )
                );
    }

    private static int executeDisplay(CommandSourceStack source, boolean isDisplay) {
        if (isDisplay) {
            Player player = source.getPlayer();
            if (player != null) {
                if (player.level().isClientSide) {
                    Minecraft.getInstance().setScreen(new AnnouncementScreen());
                }
            }
            source.sendSuccess(() -> Component.translatable("commands.atm.display.success", source.getDisplayName()), true);
            return 1;
        } else {
            source.sendFailure(Component.translatable("commands.atm.display.failure"));
            return 0;
        }
    }

    private static int executeSetMainTitle(CommandSourceStack source, Component message) {
        try {
            String currentTitle = ConfigManager.getMainTitle();
            currentTitle = message.getString();
            String finalCurrentTitle = currentTitle;
            source.sendSuccess(() -> Component.translatable("commands.atm.settings.maintitle.set.success", finalCurrentTitle), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.translatable("commands.atm.settings.maintitle.set.failure", e.getMessage()));
            return 0;
        }
    }

    private static int executeEmptyMainTitle(CommandSourceStack source, boolean isEmpty) {
        if (isEmpty) {
            try {
                String currentTitle = ConfigManager.getMainTitle();
                boolean isCurrentlyEmpty = currentTitle == null || currentTitle.isEmpty();

                source.sendSuccess(() -> Component.translatable("commands.atm.settings.maintitle.empty.status", isCurrentlyEmpty), true);
                return 1;
            } catch (Exception e) {
                source.sendFailure(Component.translatable("commands.atm.settings.maintitle.empty.failure", e.getMessage()));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.atm.settings.argument.true"));
            return 0;
        }
    }

    private static int executeResetMainTitle(CommandSourceStack source, boolean refurnish) {
        if (refurnish) {
            try {
                String defaultTitle = ConfigManager.getMainTitle();
                source.sendSuccess(() -> Component.translatable("commands.atm.settings.maintitle.reset.success", defaultTitle), true);
                return 1;
            } catch (Exception e) {
                source.sendFailure(Component.translatable("commands.atm.settings.maintitle.reset.failure", e.getMessage()));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.atm.settings.argument.true"));
            return 0;
        }
    }

    private static int executeSetSubTitle(CommandSourceStack source, Component message) {
        try {
            String currentSubTitle = ConfigManager.getSubTitle();
            currentSubTitle = message.getString();
            String finalCurrentSubTitle = currentSubTitle;
            source.sendSuccess(() -> Component.translatable("commands.atm.settings.subtitle.set.success", finalCurrentSubTitle), true);
            return 1;
        } catch (Exception e) {
            source.sendFailure(Component.translatable("commands.atm.settings.subtitle.set.failure", e.getMessage()));
            return 0;
        }
    }

    private static int executeEmptySubTitle(CommandSourceStack source, boolean isEmpty) {
        if (isEmpty) {
            try {
                String currentSubTitle = ConfigManager.getSubTitle();
                boolean isCurrentlyEmpty = currentSubTitle == null || currentSubTitle.isEmpty();

                source.sendSuccess(() -> Component.translatable("commands.atm.settings.subtitle.empty.status", isCurrentlyEmpty), true);
                return 1;
            } catch (Exception e) {
                source.sendFailure(Component.translatable("commands.atm.settings.subtitle.empty.failure", e.getMessage()));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.atm.settings.argument.true"));
            return 0;
        }
    }

    private static int executeResetSubTitle(CommandSourceStack source, boolean refurnish) {
        if (refurnish) {
            try {
                String defaultSubTitle = ConfigManager.getSubTitle();
                source.sendSuccess(() -> Component.translatable("commands.atm.settings.subtitle.reset.success", defaultSubTitle), true);
                return 1;
            } catch (Exception e) {
                source.sendFailure(Component.translatable("commands.atm.settings.subtitle.reset.failure", e.getMessage()));
                return 0;
            }
        } else {
            source.sendFailure(Component.translatable("commands.atm.settings.argument.true"));
            return 0;
        }
    }
}