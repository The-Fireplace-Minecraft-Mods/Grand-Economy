package the_fireplace.grandeconomy.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import the_fireplace.grandeconomy.api.Economy;

public interface EconomySelectedEvent {
	Event<EconomySelectedEvent> EVENT = EventFactory.createArrayBacked(EconomySelectedEvent.class,
		(listeners) -> (economy) -> {
			for (EconomySelectedEvent event: listeners) {
				event.onEconomySelected(economy);
			}
		});

	void onEconomySelected(Economy economy);
}
