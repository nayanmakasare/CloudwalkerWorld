package tv.cloudwalker.cloudwalkerworld.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import tv.cloudwalker.cloudwalkerworld.dataLoader.AppListLoader;

public class AppBdReceiver extends BroadcastReceiver
{
    private AppListLoader loader;


    public AppBdReceiver(AppListLoader loader)
    {
        this.loader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_INSTALL_PACKAGE);
        filter.addAction(Intent.ACTION_UNINSTALL_PACKAGE);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        loader.getContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        loader.onContentChanged();
    }
}
