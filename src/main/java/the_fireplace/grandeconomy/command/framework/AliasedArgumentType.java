package the_fireplace.grandeconomy.command.framework;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class AliasedArgumentType implements ArgumentType<String> {
    private static final SimpleCommandExceptionType INVALID_ALIASED_LITERAL = new SimpleCommandExceptionType(new TranslatableText("argument.aliased.invalid"));
    private final String literal;
    private final String[] aliases;

    private AliasedArgumentType(String literal, String... aliases) {
        this.literal = literal;
        this.aliases = aliases;
    }

    public static RequiredArgumentBuilder<ServerCommandSource, String> aliased(String literal, String... aliases) {
        String[] allVariants = ArrayUtils.add(aliases, 0, literal);
        return CommandManager.argument(String.join("|", allVariants), aliasedArgumentType(literal, aliases));
    }

    public static AliasedArgumentType aliasedArgumentType(String literal, String... aliases) {
        return new AliasedArgumentType(literal, aliases);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof CommandSource
            ? CommandSource.suggestMatching(getExamples(), builder)
            : Suggestions.empty();
    }

    @Override
    public String parse(final StringReader reader) throws CommandSyntaxException {
        String argument = reader.readUnquotedString();
        if (getExamples().stream().map(s -> s.toLowerCase(Locale.ROOT)).anyMatch(s -> s.equals(argument.toLowerCase(Locale.ROOT)))) {
            return argument;
        }

        throw INVALID_ALIASED_LITERAL.create();
    }

    @Override
    public String toString() {
        return "string()";
    }

    @Override
    public Collection<String> getExamples() {
        List<String> examples = new ArrayList<>();
        examples.add(literal);
        examples.addAll(Arrays.asList(aliases));
        return examples;
    }
}
