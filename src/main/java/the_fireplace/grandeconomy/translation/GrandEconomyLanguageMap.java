package the_fireplace.grandeconomy.translation;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import the_fireplace.grandeconomy.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;

public class GrandEconomyLanguageMap {
    private static final Logger LOGGER = LogManager.getLogger();
    /** Pattern that matches numeric variable placeholders in a resource string, such as "%d", "%3$d", "%.2f" */
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final GrandEconomyLanguageMap instance = new GrandEconomyLanguageMap(Config.locale);
    private final Map<String, String> languageList = Maps.newHashMap();

    GrandEconomyLanguageMap(String locale) {
        try (InputStream inputstream = GrandEconomyLanguageMap.class.getResourceAsStream("/assets/grandeconomy/lang/" + locale + ".json")) {
            JsonElement jsonelement = (new Gson()).fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonelement, "strings");

            for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                String s = NUMERIC_VARIABLE_PATTERN.matcher(JSONUtils.getString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                this.languageList.put(entry.getKey(), s);
            }
        } catch (JsonParseException | IOException ioexception) {
            LOGGER.error("/assets/grandeconomy/lang/" + locale + ".json", ioexception);
        }
    }

    static GrandEconomyLanguageMap getInstance() {
        return instance;
    }

    synchronized String translateKeyFormat(String key, Object... format) {
        String s = this.tryTranslateKey(key);

        try {
            return String.format(s, format);
        } catch (IllegalFormatException e) {
            return "Format error: " + s;
        }
    }

    private String tryTranslateKey(String key) {
        String s = this.languageList.get(key);
        return s == null ? key : s;
    }

    synchronized boolean isKeyTranslated(String key) {
        return this.languageList.containsKey(key);
    }
}
