package the_fireplace.grandeconomy.io;

import the_fireplace.grandeconomy.GrandEconomy;
import the_fireplace.lib.api.io.DirectoryResolver;
import the_fireplace.lib.api.io.JsonSerializable;
import the_fireplace.lib.api.io.SaveTimer;

import java.util.UUID;

public abstract class AccountData extends JsonSerializable {
    protected AccountData(UUID accountId, String saveFolder) {
        super(accountId, DirectoryResolver.getInstance().getSavePath().resolve(GrandEconomy.MODID).resolve(saveFolder));
        SaveTimer.getInstance().registerSaveFunction((short) 5, this::save);
    }
}
