package the_fireplace.grandeconomy.translation;

import com.google.common.collect.Lists;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class TranslationUtil {

    public static List<UUID> geClients = Lists.newArrayList();

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(String translationKey, Object... args) {
        return getTranslation(translationKey, args).getFormattedText();
    }

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(ICommandSource sender, String translationKey, Object... args) {
        return getTranslation(sender, translationKey, args).getFormattedText();
    }

    /**
     * Gets the translation for the given key and arguments and returns the formatted string.
     */
    public static String getStringTranslation(@Nullable UUID target, String translationKey, Object... args) {
        return getTranslation(target, translationKey, args).getUnformattedComponentText();
    }

    /**
     * Returns the translation key if the sender is able to translate it, or the translated string otherwise.
     */
    public static String getRawTranslationString(ICommandSource sender, String translationKey) {
        return getRawTranslationString(sender instanceof ServerPlayerEntity ? ((ServerPlayerEntity) sender).getUniqueID() : null, translationKey);
    }

    /**
     * Returns the translation key if the target is able to translate it, or the translated string otherwise.
     */
    public static String getRawTranslationString(@Nullable UUID target, String translationKey) {
        if(target == null || !geClients.contains(target))
            return I18n.translateToLocalFormatted(translationKey);
        else
            return translationKey;
    }

    /**
     * Returns the translated StringTextComponent for the supplied key and arguments
     */
    public static ITextComponent getTranslation(String translationKey, Object... args) {
        return getTranslation((UUID)null, translationKey, args);
    }

    /**
     * Returns the TranslationTextComponent if the target is able to translate it, or the translated StringTextComponent otherwise.
     */
    public static ITextComponent getTranslation(ICommandSource target, String translationKey, Object... args) {
        return getTranslation(target instanceof ServerPlayerEntity ? ((ServerPlayerEntity) target).getUniqueID() : null, translationKey, args);
    }

    /**
     * Returns the TranslationTextComponent if the target is able to translate it, or the translated StringTextComponent otherwise.
     */
    public static ITextComponent getTranslation(@Nullable UUID target, String translationKey, Object... args) {
        if(target == null || !geClients.contains(target))
            return new StringTextComponent(I18n.translateToLocalFormatted(translationKey, args));
        else
            return new TranslationTextComponent(translationKey, args);
    }
}
