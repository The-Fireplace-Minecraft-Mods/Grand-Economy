package the_fireplace.grandeconomy.translation;

import io.netty.util.internal.ConcurrentSet;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

public class TranslationUtil {

    public static Set<UUID> geClients = new ConcurrentSet<>();

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(String translationKey, Object... args) {
        return getTranslation(translationKey, args).getString();
    }

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(ServerCommandSource target, String translationKey, Object... args) {
        return target.getEntity() != null ? getStringTranslation(target.getEntity(), translationKey, args) : getStringTranslation(target.getMinecraftServer(), translationKey, args);
    }

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(CommandOutput target, String translationKey, Object... args) {
        return getTranslation(target, translationKey, args).getString();
    }

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(UUID target, String translationKey, Object... args) {
        return getTranslation(target, translationKey, args).getString();
    }

    /**
     * Returns the translation key if the sender is able to translate it, or the translated string otherwise.
     */
    public static String getRawTranslationString(CommandOutput target, String translationKey) {
        return getRawTranslationString(target instanceof ServerPlayerEntity ? ((ServerPlayerEntity) target).getUuid() : null, translationKey);
    }

    /**
     * Returns the translation key if the target is able to translate it, or the translated string otherwise.
     */
    public static String getRawTranslationString(UUID target, String translationKey) {
        if(target == null || !geClients.contains(target))
            return I18n.translateToLocalFormatted(translationKey);
        else
            return translationKey;
    }

    /**
     * Returns the translated LiteralText for the supplied key and arguments
     */
    public static MutableText getTranslation(String translationKey, Object... args) {
        return getTranslation((UUID)null, translationKey, args);
    }

    /**
     * Returns the TranslatableText if the target is able to translate it, or the translated LiteralText otherwise.
     */
    public static MutableText getTranslation(ServerCommandSource target, String translationKey, Object... args) {
        return target.getEntity() != null ? getTranslation(target.getEntity(), translationKey, args) : getTranslation(target.getMinecraftServer(), translationKey, args);
    }

    /**
     * Returns the TranslatableText if the target is able to translate it, or the translated LiteralText otherwise.
     */
    public static MutableText getTranslation(CommandOutput target, String translationKey, Object... args) {
        return getTranslation(target instanceof ServerPlayerEntity ? ((ServerPlayerEntity) target).getUuid() : null, translationKey, args);
    }

    /**
     * Returns the TranslatableText if the target is able to translate it, or the translated LiteralText otherwise.
     */
    public static MutableText getTranslation(@Nullable UUID target, String translationKey, Object... args) {
        if(target == null || !geClients.contains(target))
            return new LiteralText(I18n.translateToLocalFormatted(translationKey, args));
        else
            return new TranslatableText(translationKey, args);
    }
}
