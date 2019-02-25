package tv.cloudwalker.cloudwalkerworld.module;

/**
 * Created by cognoscis on 15/3/18.
 */

public class SettingsModel {

    private String settingsName;
    private boolean isSetting;
    private String action;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    private String className ;

    public int getSettings_icon_resId() {
        return settings_icon_resId;
    }

    public void setSettings_icon_resId(int settings_icon_resId) {
        this.settings_icon_resId = settings_icon_resId;
    }

    private int settings_icon_resId;


    public SettingsModel() {

    }

    public SettingsModel(String settingsName, int settings_icon_resId, boolean isSetting, String action, String settingsClassName) {
        this.settingsName = settingsName;
        this.settings_icon_resId = settings_icon_resId;
        this.isSetting = isSetting;
        this.action = action;
        this.className = settingsClassName;
    }

    public SettingsModel(String settingsName, int settings_icon_resId, boolean isSetting, String action) {
        this.settingsName = settingsName;
        this.settings_icon_resId = settings_icon_resId;
        this.isSetting = isSetting;
        this.action = action;
    }

    public String getSettingsName() {
        return settingsName;
    }

    public void setSettingsName(String settingsName) {
        this.settingsName = settingsName;
    }


    public boolean isSetting() {
        return isSetting;
    }

    public void setSetting(boolean setting) {
        isSetting = setting;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
