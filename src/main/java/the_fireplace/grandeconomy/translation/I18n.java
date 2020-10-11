package the_fireplace.grandeconomy.translation;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public final class I18n {
    private static final GrandEconomyLanguageMap localizedName = GrandEconomyLanguageMap.getInstance();
    private static final GrandEconomyLanguageMap fallbackTranslator = new GrandEconomyLanguageMap("en_us");

    static String translateToLocalFormatted(String key, Object... format) {
        return canTranslate(key) ? localizedName.translateKeyFormat(key, format) : translateToFallback(key, format);
    }

    private static String translateToFallback(String key, Object... format) {
        return fallbackTranslator.translateKeyFormat(key, format);
    }

    public static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }

    public static boolean hasLocale(String locale) {
        InputStream inputstream = GrandEconomyLanguageMap.class.getResourceAsStream("/assets/grandeconomy/lang/" + locale + ".json");
        boolean exists = inputstream != null;
        if(exists) {
            try {
                inputstream.close();
            } catch (IOException ignored) {}
        }
        return exists;
    }

    public static Set<String> getLocales() {
        Set<String> locales = Sets.newHashSet();
        GrandEconomyLanguageMap.class.get
    }
}
