package com.gmail.Xeiotos.HabitatSocial.Managers;

import com.gmail.Xeiotos.HabitatSocial.HabitatSocial;
import com.gmail.Xeiotos.HabitatAPI.Managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

/**
 *
 * @author Xeiotos
 */
public class MailManager {

    private static MailManager mailManager;

    /**
     * Create a new instance of a MailManager
     */
    public MailManager() {
    }
    
    /**
     * Get the MailManager instance
     *
     * @return MailManager instance
     */
    public static MailManager getManager() {
        if (mailManager == null) {
            mailManager = new MailManager();
        }

        return mailManager; // NOT THREAD SAFE!
    }
    
    public void logMail(String realName, String playername, String email) {
        HabitatSocial.getInstance().getConfigManager().loadConfigFiles(new ConfigManager.ConfigPath("mailList", "MailList.yml", "mailList.yml"));
        FileConfiguration mailList = HabitatSocial.getInstance().getConfigManager().getFileConfig("mailList");
        
        mailList.options().copyDefaults(true);
        mailList.addDefault(realName, playername + " " + email);
        HabitatSocial.getInstance().getConfigManager().saveConfig("mailList");
    }
}
