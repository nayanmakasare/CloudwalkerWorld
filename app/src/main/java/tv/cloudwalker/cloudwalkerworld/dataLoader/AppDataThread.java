package tv.cloudwalker.cloudwalkerworld.dataLoader;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import tv.cloudwalker.cloudwalkerworld.CustomPresenter.AppInfoPresenter;
import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.Utils.Utils;
import tv.cloudwalker.cloudwalkerworld.module.AppInfo;

import static tv.cloudwalker.cloudwalkerworld.Utils.Utils.isPackageInstalled;

public class AppDataThread extends Thread
{
    private Context context;

    public AppDataThread(Context context)
    {
        this.context = context;
    }

    @Override
    public void run() {
        loadDefaultAppList();
    }

    private void loadDefaultAppList()
    {
        fillAppMap(context);
        PackageManager packageManager = context.getPackageManager();
        String name , packageName;
        int drawId ;
        Drawable appIcon = null;
        String[] default_apps_array = context.getResources().getStringArray(R.array.defaultApps); ;

        List<AppInfo> appList = new ArrayList<>();
        for (String aDefault_apps_array : default_apps_array) {
            name = Utils.appIconMap.get(aDefault_apps_array);
            if(name != null){
                drawId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
                appIcon = context.getResources().getDrawable(drawId);
            }else {
                try {
                    appIcon = packageManager.getApplicationIcon(aDefault_apps_array);
                }
                catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            packageName = aDefault_apps_array;
            if(isPackageInstalled(packageName,packageManager)){
                appList.add(new AppInfo(name, appIcon, packageName, true, true));
            }
        }

        appList.add(new AppInfo("More Apps", context.getResources().getDrawable(R.drawable.app_allapps), null, false, true));
        ArrayObjectAdapter appAdapter = new ArrayObjectAdapter(new AppInfoPresenter(context));
        appAdapter.addAll(0, appList);
        ListRow appListRow = new ListRow(new HeaderItem(2, "Apps"), appAdapter);
    }

    private void fillAppMap(Context context)
    {
        try
        {
            InputStream is = context.getAssets().open("app_icons.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Utils.appIconMap.put(jsonObject.getString("package"), jsonObject.getString("name"));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
