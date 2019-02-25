package tv.cloudwalker.cloudwalkerworld.dataLoader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.Utils.AppBdReceiver;
import tv.cloudwalker.cloudwalkerworld.module.AppInfo;

import static tv.cloudwalker.cloudwalkerworld.Utils.Utils.appIconMap;

public class AppListLoader extends AsyncTaskLoader<List<AppInfo>> {
    private final PackageManager mPackageManager;
    private List<AppInfo> mApps;
    private AppBdReceiver mAppsObserver;

    private String[] blockApps;

    private static final Comparator<AppInfo> ALPHA_COMPARATOR = new Comparator<AppInfo>() {
        Collator sCollator = Collator.getInstance();

        @Override
        public int compare(AppInfo object1, AppInfo object2) {
            return sCollator.compare(object1.getAppName(), object2.getAppName());
        }
    };

    public AppListLoader(Context context) {
        super(context);
        mPackageManager = getContext().getPackageManager();
        blockApps = context.getResources().getStringArray(R.array.blockedApps);
    }

    @Override
    public List<AppInfo> loadInBackground() {
        List<PackageInfo> packageList = mPackageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS);

        if (packageList == null) {
            packageList = new ArrayList<>();
        }

        List<AppInfo> entries = new ArrayList<>(packageList.size());

        for (int i = 0; i < packageList.size(); i++) {
            PackageInfo p = packageList.get(i);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mPackageManager.getLaunchIntentForPackage(p.packageName) != null ||
                        mPackageManager.getLeanbackLaunchIntentForPackage(p.packageName) != null) {
                    Drawable icon;
                    String appName = p.applicationInfo.loadLabel(mPackageManager).toString();
                    String packageName = p.applicationInfo.packageName;
                    boolean iconAvailable;
                    if (appIconMap.containsKey(packageName)) {
                        int id = getContext().getResources().getIdentifier(appIconMap.get(packageName), "drawable", getContext().getPackageName());
                        icon = getContext().getResources().getDrawable(id);
                        iconAvailable = true;
                    } else {
                        icon = p.applicationInfo.loadIcon(mPackageManager);
                        iconAvailable = false;
                    }
                    AppInfo appInfo = new AppInfo(appName, icon, packageName, true, iconAvailable);
                    appInfo.setVersionCode(p.versionCode);
                    if(!Arrays.asList(blockApps).contains(p.packageName))
                    {
                        entries.add(appInfo);
                    }
                }
            }
            else
            {
                if(mPackageManager.getLaunchIntentForPackage(p.packageName) != null)
                {
                    Drawable icon;
                    String appName = p.applicationInfo.loadLabel(mPackageManager).toString();
                    String packageName = p.applicationInfo.packageName;
                    boolean iconAvailable;

                    if (appIconMap.containsKey(packageName)) {
                        int id = getContext().getResources().getIdentifier(appIconMap.get(packageName), "drawable", getContext().getPackageName());
                        icon = getContext().getResources().getDrawable(id);
                        iconAvailable = true;
                    }
                    else {
                        Intent launchIntent = mPackageManager.getLeanbackLaunchIntentForPackage(packageName);
                        if(launchIntent != null){
                            icon = p.applicationInfo.loadLogo(mPackageManager);
                        }else
                        {
                            icon = p.applicationInfo.loadIcon(mPackageManager);
                        }
                        iconAvailable = false;
                    }
                    AppInfo appInfo = new AppInfo(appName, icon, packageName, true, iconAvailable);
                    appInfo.setVersionCode(p.versionCode);
                    if(!Arrays.asList(blockApps).contains(p.packageName))
                    {
                        entries.add(appInfo);
                    }
                }
            }
        }
        Collections.sort(entries, ALPHA_COMPARATOR);
        return entries;
    }


    @Override
    public void deliverResult(List<AppInfo> apps) {
        if(isReset())
        {
            if(apps != null)
            {
                releaseResources(apps);
                return;
            }
        }

        List<AppInfo> oldApps = mApps;
        mApps = apps;

        if(isStarted())
        {
            super.deliverResult(apps);
        }

        if(oldApps != null && oldApps != apps)
        {
            releaseResources(oldApps);
        }
    }


    @Override
    protected void onStartLoading() {
        if(mApps != null)
        {
            deliverResult(mApps);
        }

        if(mAppsObserver == null)
        {
            mAppsObserver = new AppBdReceiver(this);
        }

        if(takeContentChanged())
        {
            forceLoad();
        }
        else if(mApps == null)
        {
            forceLoad();
        }

    }


    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mApps != null)
        {
            releaseResources(mApps);
            mApps = null;
        }

        if(mAppsObserver != null)
        {
            getContext().unregisterReceiver(mAppsObserver);
            mAppsObserver = null;
        }
    }


    @Override
    public void onCanceled(List<AppInfo> data) {
        super.onCanceled(data);
        releaseResources(data);
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }

    private void releaseResources(List<AppInfo> apps) {
        // For a simple List, there is nothing to do. For something like a Cursor,
        // we would close it in this method. All resources associated with the
        // Loader should be released here.
    }
}






















