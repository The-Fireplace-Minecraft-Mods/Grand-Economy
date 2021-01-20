package the_fireplace.grandeconomy.command.framework;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;

import java.util.Collection;

public class AliasedCommandRegister {

    public static <S> Collection<LiteralCommandNode<S>> register(final CommandDispatcher<S> commandDispatcher, final AliasedArgumentBuilder<S> command) {
        final Collection<LiteralCommandNode<S>> builtNodes = command.buildAll();

        for (LiteralCommandNode<S> node: builtNodes) {
            commandDispatcher.getRoot().addChild(node);
        }

        return builtNodes;
    }
}
