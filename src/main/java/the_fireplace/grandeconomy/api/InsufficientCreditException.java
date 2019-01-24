package the_fireplace.grandeconomy.api;

import net.minecraft.command.CommandException;

public class InsufficientCreditException extends CommandException {

    public InsufficientCreditException() {
        //noinspection RedundantArrayCreation
        this("You don't have money you want to pay :(.", new Object[0]);
    }

    private InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}