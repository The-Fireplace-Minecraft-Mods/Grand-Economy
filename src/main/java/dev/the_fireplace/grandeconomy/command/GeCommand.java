package dev.the_fireplace.grandeconomy.command;

import dev.the_fireplace.grandeconomy.GrandEconomyConstants;
import dev.the_fireplace.grandeconomy.api.injectables.CurrencyAPI;
import dev.the_fireplace.lib.api.chat.injectables.TranslatorFactory;
import dev.the_fireplace.lib.api.command.injectables.FeedbackSenderFactory;
import dev.the_fireplace.lib.api.command.injectables.Requirements;
import dev.the_fireplace.lib.api.command.interfaces.FeedbackSender;
import dev.the_fireplace.lib.api.command.interfaces.RegisterableCommand;

public abstract class GeCommand implements RegisterableCommand {

	protected final CurrencyAPI currencyAPI;
	protected final FeedbackSender feedbackSender;
	protected final Requirements requirements;

	protected GeCommand(
		CurrencyAPI currencyAPI,
		TranslatorFactory translatorFactory,
		FeedbackSenderFactory feedbackSenderFactory,
		Requirements requirements
	) {
		this.currencyAPI = currencyAPI;
		this.feedbackSender = feedbackSenderFactory.get(translatorFactory.getTranslator(GrandEconomyConstants.MODID));
		this.requirements = requirements;
	}
}
