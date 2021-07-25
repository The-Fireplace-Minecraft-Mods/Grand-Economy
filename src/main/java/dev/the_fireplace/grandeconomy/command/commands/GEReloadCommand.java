package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.lazyio.injectables.ReloadableManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class GEReloadCommand extends GeCommand {

    private final ReloadableManager reloadableManager;

    @Inject
    public GEReloadCommand(
        CurrencyAPI currencyAPI,
        TranslatorFactory translatorFactory,
        FeedbackSenderFactory feedbackSenderFactory,
        Requirements requirements,
        ReloadableManager reloadableManager
    ) {
        super(currencyAPI, translatorFactory, feedbackSenderFactory, requirements);
        this.reloadableManager = reloadableManager;
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("gereload")
            .requires(requirements::manageServer)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) {
        reloadableManager.reload(GrandEconomyConstants.MODID);
        feedbackSender.basic(command, "commands.grandeconomy.reload.reloaded");
        return Command.SINGLE_SUCCESS;
    }
}
