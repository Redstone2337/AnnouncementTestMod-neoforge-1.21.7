package net.redstone233.atm.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.redstone233.atm.AnnouncementTestMod;

public class ModEnglishLanguageProvider extends LanguageProvider {
    public ModEnglishLanguageProvider(PackOutput output) {
        super(output, AnnouncementTestMod.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("key.atm.announcement","Displays the custom announcement.");
        add("category.atm","Custom Announcement.");

        add("commands.atm.display.success","Announcement displayed successfully for %s");
        add("commands.atm.display.failure", "Failed to display announcement");
        add("commands.atm.settings.maintitle.set.success", "Main title set to: %s");
        add("commands.atm.settings.maintitle.set.failure", "Failed to set main title: %s");
        add("commands.atm.settings.maintitle.empty.status", "Main title empty status: %s");
        add("commands.atm.settings.maintitle.empty.failure", "Failed to check main title empty status: %s");
        add("commands.atm.settings.maintitle.reset.success", "Main title reset to: %s");
        add("commands.atm.settings.maintitle.reset.failure", "Failed to reset main title: %s");
        add("commands.atm.settings.subtitle.set.success", "Sub title set to: %s");
        add("commands.atm.settings.subtitle.set.failure", "Failed to set sub title: %s");
        add("commands.atm.settings.subtitle.empty.status", "Sub title empty status: %s");
        add("commands.atm.settings.subtitle.empty.failure", "Failed to check sub title empty status: %s");
        add("commands.atm.settings.subtitle.reset.success", "Sub title reset to: %s");
        add("commands.atm.settings.subtitle.reset.failure", "Failed to reset sub title: %s");
        add("commands.atm.settings.argument.true", "This command requires the argument to be 'true'");
    }
}