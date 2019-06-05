package the_fireplace.grandeconomy.api;

import net.minecraft.command.CommandException;

public class InsufficientCreditException extends CommandException {

    public InsufficientCreditException() {
        this("You don't have enough money to do that.");
    }

    private InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}