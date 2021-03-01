package dev.the_fireplace.grandeconomy.command;

import dev.the_fireplace.grandeconomy.GrandEconomy;
import dev.the_fireplace.grandeconomy.api.CurrencyAPI;
import dev.the_fireplace.lib.api.command.FeedbackSender;
import dev.the_fireplace.lib.api.command.RegisterableCommand;
import dev.the_fireplace.lib.api.command.Requirements;

public abstract class GeCommand implements RegisterableCommand {

	protected final CurrencyAPI currencyAPI;
	protected final FeedbackSender feedbackSender;
	protected final Requirements requirements;

	protected GeCommand() {
		this.currencyAPI = CurrencyAPI.getInstance();
		this.feedbackSender = GrandEconomy.getFeedbackSender();
		this.requirements = Requirements.getInstance();
	}
}
