package tv.cloudwalker.cloudwalkerworld.module;

import android.graphics.drawable.Drawable;

/**
 * Created by cognoscis on 5/1/18.
 */

public class AppInfo {

    private String appName;
    private Drawable appIcon;
    private String packageName;
    private boolean isApp;
    private boolean isVisible;
    private int versionCode;
    private boolean iconAvailable;

    public AppInfo() {
    }


    public AppInfo(String appName, Drawable appIcon, String packageName, boolean isApp, boolean iconAvailable) {
        this.appIcon = appIcon;
        this.appName = appName;
        this.packageName = packageName;
        this.isApp = isApp;
        this.iconAvailable = iconAvailable;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isApp() {
        return isApp;
    }

    public void setApp(boolean app) {
        isApp = app;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean isIconAvailable() {
        return iconAvailable;
    }

    public void setIconAvailable(boolean iconAvailable) {
        this.iconAvailable = iconAvailable;
    }
}
