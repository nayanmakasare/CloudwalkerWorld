package tv.cloudwalker.cloudwalkerworld;

import android.app.Application;

public class CloudwalkerApplication extends Application
{
    public CloudwalkerApplication()
    {

    }

    @Override
    public void onCreate() {
        super.onCreate();
//        FlurryAgent.setLogEnabled(true);
//        FlurryAgent.init(CloudwalkerApplication.this, "R5J7M7KHDSBT5KKXP7DR");
    }
}
