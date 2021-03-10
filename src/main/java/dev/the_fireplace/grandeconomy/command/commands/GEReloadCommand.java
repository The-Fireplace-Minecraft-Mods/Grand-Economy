package dev.the_fireplace.grandeconomy.command.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.command.GeCommand;
import dev.the_fireplace.lib.api.storage.utility.ReloadableManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public final class GEReloadCommand extends GeCommand {

    private final ReloadableManager reloadableManager;

    public GEReloadCommand() {
        reloadableManager = ReloadableManager.getInstance();
    }

    @Override
    public CommandNode<ServerCommandSource> register(CommandDispatcher<ServerCommandSource> commandDispatcher) {
        return commandDispatcher.register(CommandManager.literal("gereload")
            .requires(requirements::manageServer)
            .executes(this::execute)
        );
    }

    private int execute(CommandContext<ServerCommandSource> command) {
        reloadableManager.reload(GrandEconomy.MODID);
        feedbackSender.basic(command, "commands.grandeconomy.reload.reloaded");
        return Command.SINGLE_SUCCESS;
    }
}
