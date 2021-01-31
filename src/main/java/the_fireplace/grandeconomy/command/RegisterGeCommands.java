package the_fireplace.grandeconomy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.command.commands.BalanceCommand;
import the_fireplace.grandeconomy.command.commands.PayCommand;
import the_fireplace.grandeconomy.command.commands.WalletCommand;
import the_fireplace.lib.api.chat.TextPaginator;
import the_fireplace.lib.api.command.HelpCommandFactory;

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
