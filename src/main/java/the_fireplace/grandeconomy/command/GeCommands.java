package the_fireplace.grandeconomy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.lib.api.chat.TextPaginator;

public final class GeCommands {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        new HelpCommandBuilder(
            GrandEconomy.getTranslator(),
            TextPaginator.getInstance(),
            GrandEconomy.MODID,
            LiteralArgumentBuilder.literal("gehelp")
        ).addCommands(
            new BalanceCommand().register(commandDispatcher),
            new PayCommand().register(commandDispatcher)
        ).addSubCommands(
            new WalletCommand().register(commandDispatcher)
        ).register(commandDispatcher);
    }
}
