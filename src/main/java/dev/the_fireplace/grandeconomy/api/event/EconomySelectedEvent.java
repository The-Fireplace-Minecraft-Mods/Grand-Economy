package dev.the_fireplace.grandeconomy.api.event;

import dev.the_fireplace.grandeconomy.api.Economy;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public interface EconomySelectedEvent {
	Event<EconomySelectedEvent> EVENT = EventFactory.createArrayBacked(EconomySelectedEvent.class,
		(listeners) -> (economy) -> {
			for (EconomySelectedEvent event: listeners) {
				event.onEconomySelected(economy);
			}
		});

	void onEconomySelected(Economy economy);
}
