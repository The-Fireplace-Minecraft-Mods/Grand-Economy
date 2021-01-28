package the_fireplace.grandeconomy.command.framework;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

public class Requirements {
    public static boolean entity(ServerCommandSource commandSource) {
        return commandSource.getEntity() != null;
    }

    public static boolean player(ServerCommandSource commandSource) {
        return commandSource.getEntity() instanceof PlayerEntity;
    }

    public static boolean manageGameSettings(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PermissionLevels.MANAGE_GAME_SETTINGS);
    }

    public static boolean managePlayerAccess(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PermissionLevels.MANAGE_PLAYER_ACCESS);
    }

    public static boolean manageServer(ServerCommandSource commandSource) {
        return commandSource.hasPermissionLevel(PermissionLevels.OP);
    }
}
