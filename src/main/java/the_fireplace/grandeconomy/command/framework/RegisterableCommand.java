package the_fireplace.grandeconomy.command.framework;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public interface RegisterableCommand {
    void register(CommandDispatcher<ServerCommandSource> commandDispatcher);
}
