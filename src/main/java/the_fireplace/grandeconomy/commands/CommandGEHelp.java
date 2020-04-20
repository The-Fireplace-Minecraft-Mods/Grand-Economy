package the_fireplace.grandeconomy.commands;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import the_fireplace.grandeconomy.ChatPageUtil;
import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;

public class CommandGEHelp extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "geconhelp";
    }

    @Override
    @Nonnull
    public String getUsage(@Nullable ICommandSender sender) {
        return TranslationUtil.getRawTranslationString(sender, "commands.grandeconomy.gehelp.usage");
    }

    @Override
    public void execute(@Nullable MinecraftServer server, @Nonnull ICommandSender sender, @Nullable String[] args) throws CommandException {
        List<ITextComponent> helps = Lists.newArrayList();
        for(CommandBase command: GrandEconomy.commands)
            helps.add(TranslationUtil.getTranslation(sender, "commands.grandeconomy.gehelp.format",
                    TranslationUtil.getStringTranslation(sender, "commands.grandeconomy."+command.getName()+".usage"),
                    TranslationUtil.getStringTranslation(sender, "commands.grandeconomy."+command.getName()+".description")));
        helps.sort(Comparator.comparing(ITextComponent::getUnformattedText));

        ChatPageUtil.showPaginatedChat(sender, "/geconhelp %s", helps, args == null || args.length < 1 ? 1 : parseInt(args[0]));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }
}
