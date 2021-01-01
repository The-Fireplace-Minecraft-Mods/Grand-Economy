package the_fireplace.grandeconomy.io;

import net.minecraft.util.WorldSavePath;
import the_fireplace.grandeconomy.GrandEconomy;

import java.nio.file.Path;

public final class Directories {
    private static final Path SAVE_DIRECTORY = GrandEconomy.getServer().getSavePath(WorldSavePath.ROOT);
    public static final Path GE_DATA_LOCATION = SAVE_DIRECTORY.resolve(GrandEconomy.MODID);
}
