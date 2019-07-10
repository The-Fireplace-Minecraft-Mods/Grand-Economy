package the_fireplace.grandeconomy.econhandlers.ge;

import net.minecraft.command.CommandException;
import the_fireplace.grandeconomy.translation.TranslationUtil;

import java.util.UUID;

public class InsufficientCreditException extends CommandException {

    public InsufficientCreditException() {
        this(TranslationUtil.getStringTranslation("grandeconomy.insufficient_credit"));
    }

    public InsufficientCreditException(UUID target) {
        this(TranslationUtil.getStringTranslation(target, "grandeconomy.insufficient_credit"));
    }

    private InsufficientCreditException(String message, Object... objects) {
        super(message, objects);
    }
}