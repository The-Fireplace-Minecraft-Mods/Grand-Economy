package the_fireplace.grandeconomy.command.framework;

import net.minecraft.server.command.ServerCommandSource;

public class CommandManager extends net.minecraft.server.command.CommandManager {
    public CommandManager(RegistrationEnvironment environment) {
        super(environment);
    }

    public static AliasedArgumentBuilder<ServerCommandSource> aliased(String literal, String... aliases) {
        return AliasedArgumentBuilder.aliased(literal, aliases);
    }
}
