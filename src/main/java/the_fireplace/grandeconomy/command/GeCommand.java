package the_fireplace.grandeconomy.command;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.grandeconomy.api.CurrencyAPI;
import the_fireplace.lib.api.command.FeedbackSender;
import the_fireplace.lib.api.command.RegisterableCommand;
import the_fireplace.lib.api.command.Requirements;

public abstract class GeCommand implements RegisterableCommand {

	protected final CurrencyAPI currencyAPI;
	protected final FeedbackSender feedbackSender;
	protected final Requirements requirements;

	GeCommand() {
		this.currencyAPI = CurrencyAPI.getInstance();
		this.feedbackSender = GrandEconomy.getFeedbackSender();
		this.requirements = Requirements.getInstance();
	}
}
