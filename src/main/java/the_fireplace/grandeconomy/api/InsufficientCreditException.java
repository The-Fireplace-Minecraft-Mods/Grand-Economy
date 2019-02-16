package the_fireplace.grandeconomy.api;

import net.minecraft.command.CommandException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import the_fireplace.grandeconomy.TextStyles;

public class InsufficientCreditException extends CommandException {

    public InsufficientCreditException() {
        this(new TextComponentString("You don't have enough money to do that.").setStyle(TextStyles.RED));
    }

    private InsufficientCreditException(ITextComponent message) {
        super(message);
    }
}