package the_fireplace.grandeconomy.command.framework;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.lib.api.chat.TextStyles;

public class SendFeedback {
    public static int throwFailure(CommandContext<ServerCommandSource> command, String translationKey, Object... args) throws CommandException {
        throw new CommandException(GrandEconomy.getTranslator().getTextForTarget(command.getSource(), translationKey, args).setStyle(TextStyles.RED));
    }

    public static void basic(CommandContext<ServerCommandSource> command, String translationKey, Object... args) {
        command.getSource().sendFeedback(GrandEconomy.getTranslator().getTextForTarget(command.getSource(), translationKey, args), false);
    }

    public static void basic(ServerPlayerEntity targetPlayer, String translationKey, Object... args) {
        targetPlayer.sendMessage(GrandEconomy.getTranslator().getTextForTarget(targetPlayer.getUuid(), translationKey, args), false);
    }

    public static void styled(CommandContext<ServerCommandSource> command, Style style, String translationKey, Object... args) {
        command.getSource().sendFeedback(GrandEconomy.getTranslator().getTextForTarget(command.getSource(), translationKey, args).setStyle(style), false);
    }

    public static void styled(ServerPlayerEntity targetPlayer, Style style, String translationKey, Object... args) {
        targetPlayer.sendMessage(GrandEconomy.getTranslator().getTextForTarget(targetPlayer.getUuid(), translationKey, args).setStyle(style), false);
    }
}
