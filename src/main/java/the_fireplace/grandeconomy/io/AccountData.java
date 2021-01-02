package the_fireplace.grandeconomy.io;

import the_fireplace.lib.api.io.SaveTimer;
import the_fireplace.lib.api.io.ThreadedJsonSerializable;

import java.util.UUID;

public abstract class AccountData extends ThreadedJsonSerializable {
    protected AccountData(UUID accountId, String saveFolder) {
        super(accountId, Directories.GE_DATA_LOCATION.resolve(saveFolder));
        SaveTimer.registerSaveFunction((short) 5, this::save);
    }
}
