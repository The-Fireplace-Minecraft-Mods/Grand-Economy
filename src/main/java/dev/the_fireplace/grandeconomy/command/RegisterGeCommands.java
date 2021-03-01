package dev.the_fireplace.grandeconomy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.command.commands.BalanceCommand;
import dev.the_fireplace.grandeconomy.command.commands.PayCommand;
import dev.the_fireplace.grandeconomy.command.commands.WalletCommand;
import dev.the_fireplace.lib.api.chat.TextPaginator;
import dev.the_fireplace.lib.api.command.HelpCommandFactory;
import net.minecraft.server.command.ServerCommandSource;

public final class RegisterGeCommands {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        HelpCommandFactory.getInstance().create(
            GrandEconomy.getTranslator(),
            TextPaginator.getInstance(),
            GrandEconomy.MODID,
            LiteralArgumentBuilder.literal("gehelp")
        ).addCommands(
            new BalanceCommand().register(commandDispatcher),
            new PayCommand().register(commandDispatcher)
        ).addSubCommandsFromCommands(
            new WalletCommand().register(commandDispatcher)
        ).register(commandDispatcher);
    }
}
