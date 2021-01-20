package the_fireplace.grandeconomy;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import the_fireplace.grandeconomy.command.BalanceCommand;
import the_fireplace.grandeconomy.command.HelpCommand;
import the_fireplace.grandeconomy.command.PayCommand;
import the_fireplace.grandeconomy.command.WalletCommand;

public class GeCommands {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        new BalanceCommand().register(commandDispatcher);
        new PayCommand().register(commandDispatcher);
        new WalletCommand().register(commandDispatcher);
        new HelpCommand().register(commandDispatcher);
    }
}
