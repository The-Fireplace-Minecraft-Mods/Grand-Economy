package the_fireplace.grandeconomy.command.framework;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.ArrayList;
import java.util.Collection;

public class AliasedArgumentBuilder<S> extends LiteralArgumentBuilder<S> {
    private final String[] aliases;

    protected AliasedArgumentBuilder(String literal, String[] aliases) {
        super(literal);
        this.aliases = aliases;
    }

    public static <S> AliasedArgumentBuilder<S> aliased(final String name, final String... aliases) {
        return new AliasedArgumentBuilder<>(name, aliases);
    }

    public String[] getAliases() {
        return aliases;
    }

    protected Collection<LiteralCommandNode<S>> buildAll() {
        Collection<LiteralCommandNode<S>> nodes = new ArrayList<>(aliases.length+1);
        nodes.add(build());

        for (String alias: aliases) {
            nodes.add(build(alias));
        }

        return nodes;
    }

    protected LiteralCommandNode<S> build(String literal) {
        final LiteralCommandNode<S> result = new LiteralCommandNode<>(literal, getCommand(), getRequirement(), getRedirect(), getRedirectModifier(), isFork());

        for (final CommandNode<S> argument : getArguments()) {
            result.addChild(argument);
        }

        return result;
    }
}
