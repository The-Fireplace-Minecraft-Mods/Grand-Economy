package the_fireplace.grandeconomy.forge.translation;

class I18n {
    private static final GrandEconomyLanguageMap localizedName = GrandEconomyLanguageMap.getInstance();
    private static final GrandEconomyLanguageMap fallbackTranslator = new GrandEconomyLanguageMap("en_us");

    static String translateToLocalFormatted(String key, Object... format) {
        return canTranslate(key) ? localizedName.translateKeyFormat(key, format) : translateToFallback(key, format);
    }

    private static String translateToFallback(String key, Object... format) {
        return fallbackTranslator.translateKeyFormat(key, format);
    }

    private static boolean canTranslate(String key) {
        return localizedName.isKeyTranslated(key);
    }
}
