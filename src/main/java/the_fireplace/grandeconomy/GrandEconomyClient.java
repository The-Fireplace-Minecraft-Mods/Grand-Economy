package the_fireplace.grandeconomy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class GrandEconomyClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        //We need to make sure all economy handlers are loaded on the client so the options can be populated in the config gui
        ClientLifecycleEvents.CLIENT_STARTED.register(c -> GrandEconomy.loadEconomy());
    }
}
