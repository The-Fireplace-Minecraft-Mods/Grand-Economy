package dev.the_fireplace.grandeconomy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.command.commands.BalanceCommand;
import dev.the_fireplace.grandeconomy.command.commands.GEReloadCommand;
import dev.the_fireplace.grandeconomy.command.commands.PayCommand;
import dev.the_fireplace.grandeconomy.command.commands.WalletCommand;
import dev.the_fireplace.lib.api.command.injectables.HelpCommandFactory;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class RegisterGeCommands {

    private final HelpCommandFactory helpCommandFactory;
    private final BalanceCommand balanceCommand;
    private final PayCommand payCommand;
    private final GEReloadCommand geReloadCommand;
    private final WalletCommand walletCommand;

    @Inject
    public RegisterGeCommands(
        HelpCommandFactory helpCommandFactory,
        BalanceCommand balanceCommand,
        PayCommand payCommand,
        GEReloadCommand geReloadCommand,
        WalletCommand walletCommand
    ) {
        this.helpCommandFactory = helpCommandFactory;
        this.balanceCommand = balanceCommand;
        this.payCommand = payCommand;
        this.geReloadCommand = geReloadCommand;
        this.walletCommand = walletCommand;
    }

    public void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        helpCommandFactory.create(
            GrandEconomyConstants.MODID,
            LiteralArgumentBuilder.literal("gehelp")
        ).addCommands(
            balanceCommand.register(commandDispatcher),
            payCommand.register(commandDispatcher),
            geReloadCommand.register(commandDispatcher)
        ).addSubCommandsFromCommands(
            walletCommand.register(commandDispatcher)
        ).register(commandDispatcher);
    }
}
