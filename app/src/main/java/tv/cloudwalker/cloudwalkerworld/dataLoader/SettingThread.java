package tv.cloudwalker.cloudwalkerworld.dataLoader;

import android.provider.Settings;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;

import java.util.ArrayList;
import java.util.List;

import tv.cloudwalker.cloudwalkerworld.CustomPresenter.CharacterCardPresenter;
import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.Utils.OttoBus;
import tv.cloudwalker.cloudwalkerworld.module.SettingsModel;

public class SettingThread extends Thread
{
    public SettingThread(){
        OttoBus.getBus().register(this);
    }

    @Override
    public void run() {
        loadSettingsData();
    }

    private void loadSettingsData()
    {
        List<SettingsModel> settingsList = new ArrayList<>();
        boolean isSetting;
        String name, action, className;
        int iconResId;

        isSetting = true;
        name = "Network";
        iconResId = R.drawable.settings_network;
        action = "";
        className = "com.cvte.settings.Settings$NetworkSettings";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action, className));

        isSetting = true;
        name = "Date & Time";
        iconResId = R.drawable.settings_clock;
        action = Settings.ACTION_DATE_SETTINGS;
        className = "com.cvte.settings.Settings$DateSettings";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action, className));

        isSetting = true;
        name = "General";
        iconResId = R.drawable.settings_general;
        action = Settings.ACTION_SECURITY_SETTINGS;
        className = "com.cvte.settings.Settings$GeneralSettings";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action, className));

        isSetting = true;
        name = "Device Info";
        iconResId = R.drawable.settings_info;
        action = Settings.ACTION_DEVICE_INFO_SETTINGS;
        className = "com.cvte.settings.Settings$DeviceInfoSettings";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action, className));

        isSetting = false;
        name = "Profile Settings";
        iconResId = R.drawable.settings_profile;
        action = "profile_settings";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action));

        isSetting = false;
        name = "Terms & Condition";
        iconResId = R.drawable.settings_term;
        action = "term_condition";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action));

        isSetting = false;
        name = "About Us";
        iconResId = R.drawable.settings_about;
        action = "about_us";
        settingsList.add(new SettingsModel(name, iconResId, isSetting, action));

        ArrayObjectAdapter settingsAdapter = new ArrayObjectAdapter(new CharacterCardPresenter());
        settingsAdapter.addAll(0, settingsList);
        ListRow settingListRow = new ListRow(new HeaderItem(4, "Settings"), settingsAdapter);
        OttoBus.getBus().post(settingListRow);
    }
}
