package the_fireplace.grandeconomy.translation;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.Util;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    private long lastUpdateTimeInMilliseconds;

    GrandEconomyLanguageMap(String locale) {
        try (InputStream inputstream = GrandEconomyLanguageMap.class.getResourceAsStream("/data/grandeconomy/lang/" + locale + ".json")) {
            JsonElement jsonelement = (new Gson()).fromJson(new InputStreamReader(inputstream, StandardCharsets.UTF_8), JsonElement.class);
            JsonObject jsonobject = JSONUtils.getJsonObject(jsonelement, "strings");

            for(Map.Entry<String, JsonElement> entry : jsonobject.entrySet()) {
                String s = NUMERIC_VARIABLE_PATTERN.matcher(JSONUtils.getString(entry.getValue(), entry.getKey())).replaceAll("%$1s");
                this.languageList.put(entry.getKey(), s);
            }
            //LanguageHook.captureLanguageMap(this.languageList);
            this.lastUpdateTimeInMilliseconds = Util.milliTime();
        } catch (JsonParseException | IOException ioexception) {
            LOGGER.error("/data/grandeconomy/lang/" + locale + ".json", ioexception);
        }
    }

    static GrandEconomyLanguageMap getInstance() {
        return instance;
    }

    /**
     * Replaces all the current instance's translations with the ones that are passed in.
     */
    @OnlyIn(Dist.CLIENT)
    public static synchronized void replaceWith(Map<String, String> p_135063_0_) {
        instance.languageList.clear();
        instance.languageList.putAll(p_135063_0_);
        instance.lastUpdateTimeInMilliseconds = Util.milliTime();
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

    /**
     * Gets the time, in milliseconds since epoch, that this instance was last updated
     */
    public long getLastUpdateTimeInMilliseconds() {
        return this.lastUpdateTimeInMilliseconds;
    }
}
