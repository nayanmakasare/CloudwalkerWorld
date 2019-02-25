package tv.cloudwalker.cloudwalkerworld.Utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.RemoteException;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeIntents;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tv.cloudwalker.cloudwalkerworld.MainActivity;
import tv.cloudwalker.cloudwalkerworld.module.MovieTile;

public class Utils
{
    public static Map<String, String> appIconMap = new HashMap<>();


    public static SpannableStringBuilder getSpannableStringDescription(MovieTile movie) {
        SpannableStringBuilder spannableStringDescription = new SpannableStringBuilder();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
        String source = movie.getSynopsis();
        iterator.setText(source);
        int start = iterator.first();
        int maxLength = 0;
        for (int end = iterator.next(), lineCount = 1;
             end != BreakIterator.DONE && lineCount <= 4;
             start = end, end = iterator.next(), lineCount++) {
            maxLength += source.substring(start, end).length();
            if (maxLength > 375) {
                continue;
            }
            spannableStringDescription.append(source.substring(start, end));
        }
        return spannableStringDescription;
    }


    public static String getSeperatedValuesWithHeader(String seperator, String header, ArrayList<String> list) {
        String values = "";
        for (String value : list) {
            if (value.length() > 0) {
                values += value + seperator;
            } else {
                return "";
            }
        }
        values = values.replaceAll("[" + seperator + "] $", "");
        if (header.length() > 0) {
            return (header + " : " + values);
        } else {
            return values;
        }
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void handleVideoClick(MovieTile item, Activity mActivity) {
        String TAG = "handleVideoClick   ";
        /* Youtube handling block */
        if (item.getSource().compareToIgnoreCase("Youtube") == 0 && item.getType() != null) {
            //checking if youtube TV is installed or not
            Log.d(TAG, "handleVideoClick: 1");
            if (!isPackageInstalled("com.google.android.youtube.tv", mActivity.getPackageManager())) {
                try {
                    Log.d(TAG, "handleVideoClick: 2");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.google.android.youtube.tv"));
                    // find all applications handle our Deeplink
                    final List<ResolveInfo> otherApps = mActivity.getPackageManager().queryIntentActivities(intent, 0);
                    for (ResolveInfo otherApp : otherApps) {
                        if (otherApp.activityInfo.applicationInfo.packageName.equals("cm.aptoidetv.pt.cvt_platform7")) {
                            ActivityInfo otherAppActivity = otherApp.activityInfo;
                            ComponentName componentName = new ComponentName(
                                    otherAppActivity.applicationInfo.packageName,
                                    otherAppActivity.name
                            );
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setComponent(componentName);
                            break;
                        }
                    }
                    mActivity.startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else {
                Log.d(TAG, "handleVideoClick: 3");
                if (item.isUseAlternate()) {
                    Log.d(TAG, "handleVideoClick: 4");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getAlternateUrl()));
                    mActivity.startActivity(intent);
                }

                String target = item.getTarget().get(0);
                Log.d(TAG, "handleVideoClick: 5");
                boolean result = startYoutube(item.getType(), mActivity, target);
                if (!result) {
                }
            }
            /* Cloudwalker Universe application recursive */
        } else if (item.getSource().compareToIgnoreCase("cloudwalkeruniverse") == 0) {
            if (item.getPackageName() != null) {
                if (Utils.appInstalledOrNot(item.getPackageName(), mActivity)) {
                    Intent intent;
                    if (item.getTarget() != null) {
                        List<String> playlist = new ArrayList<>();
                        try {
                            JSONArray jsonArray = new JSONArray(item.getTarget());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                playlist.add(jsonArray.getString(i));
                            }
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playlist.get(0)));
                            mActivity.startActivity(intent);
//                            mActivity.finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getPlaystoreUrl()));
                    mActivity.startActivity(intent);
                }
            } else {
                if (item.getTarget() != null) {
                    List<String> playlist = new ArrayList<>();
                    try {
                        JSONArray jsonArray = new JSONArray(item.getTarget());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            playlist.add(jsonArray.getString(i));
                        }
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(playlist.get(0)));
                        mActivity.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            /* Non youtube or universe tiles handling */
        } else {
            /*  - If type is START then we need to start the application
             *  - If app is not installed then we need to check if we need to install it via
             *    playstore or alternate url and use respective apps to open.
             *  - If target is null then open the package as it is.
             *  - If target is not null then open the package with given target.
             */
            if (item.getType().compareToIgnoreCase("START") == 0) {
                List<String> targetArray = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(item.getTarget());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        targetArray.add(jsonArray.getString(i));
                    }

                    String boardModel = "TP.MT5510I.PB801";
                    boolean packageFound = false;
                    Intent intent = mActivity.getPackageManager().getLaunchIntentForPackage(item.getPackageName());
                    if(intent == null){
                        if(boardModel.contains("5510") || boardModel.contains("553") || boardModel.contains("358")){
                            if(mActivity.getPackageManager().getLeanbackLaunchIntentForPackage(item.getPackageName()) != null){
                                packageFound = true;
                            }
                        }
                    }else {
                        packageFound = true;
                    }

                    if(!packageFound){
                        if (item.getPackageName().compareToIgnoreCase("tv.cloudwalker.cwnxt.launcher.TermsAndConditionActivity")==0){
                            intent = new Intent();
                            intent.putExtra("tag", targetArray.get(0));
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setClassName(mActivity, item.getPackageName());
                            mActivity.startActivity(intent);
                        } else if (item.isUseAlternate()) {
                            String appUrl = item.getAlternateUrl();
                            Pattern pattern = Pattern.compile(".*\\.apk\\b");
                            Matcher matcher = pattern.matcher(appUrl);
                            boolean matches = matcher.matches();
                            if (matches) {
//                                if (mActivity instanceof MainActivity) {
//                                    ((MainActivity) mActivity).
//                                            downloadApk(appUrl);
//                                } else {
//                                    ((DetailActivity) mActivity).downloadApk(appUrl);
//                                }
                            } else {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getAlternateUrl()));
                                mActivity.startActivity(intent);
                            }
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + item.getPackageName()));
                            // find all applications handle our Deeplink
                            final List<ResolveInfo> otherApps = mActivity.getPackageManager().queryIntentActivities(intent, 0);
                            for (ResolveInfo otherApp : otherApps) {
                                if (otherApp.activityInfo.applicationInfo.packageName.equals(item.getPlayStorePackage())) {
                                    ActivityInfo otherAppActivity = otherApp.activityInfo;
                                    ComponentName componentName = new ComponentName(
                                            otherAppActivity.applicationInfo.packageName,
                                            otherAppActivity.name
                                    );
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.setComponent(componentName);
                                    break;

                                }
                            }
                            try{
                                mActivity.startActivity(intent);
                            }catch (ActivityNotFoundException e){
                                Toast.makeText(mActivity , "App Store not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (targetArray.get(0).compareToIgnoreCase("null") == 0) {
                            if (intent == null) {
                                intent = mActivity.getPackageManager().getLeanbackLaunchIntentForPackage(item.getPackageName());
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mActivity.startActivity(intent);
                        } else if (item.getPackageName().equalsIgnoreCase("in.startv.hotstar")){
                            try {
                                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetArray.get(0)));
                                intent.setPackage(item.getPackageName());
                                mActivity.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                                intent = mActivity.getPackageManager().getLeanbackLaunchIntentForPackage(item.getPackageName());
                                intent.setData(Uri.parse(targetArray.get(0).replace("http://www.hotstar.com", "hotstar://content")));
                                mActivity.startActivity(intent);
                            }
                            mActivity.startActivity(intent);
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(targetArray.get(0)));
                            intent.setPackage(item.getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                mActivity.startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                Toast.makeText(mActivity.getApplicationContext(), "Could not load the application", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //The content target is not give as an array!
                    if (item.getTarget().size() > 0 &&
                            item.getTarget().get(0).compareToIgnoreCase("null") != 0) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getTarget().get(0)));
                        mActivity.startActivity(intent);
                    }
                }
            }else if (item.getType().compareToIgnoreCase("KEYCODE") == 0){
                try
                {
                    final List<String> targetArray = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(item.getTarget());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        targetArray.add(jsonArray.getString(i));
                    }
                    if(targetArray.get(0) != null){
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Instrumentation inst = new Instrumentation();
                                    inst.sendKeyDownUpSync(Integer.parseInt(targetArray.get(0)));
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public static boolean appInstalledOrNot(String uri, Activity mActivity) {
        PackageManager pm = mActivity.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }



    public static boolean startYoutube(String type, Activity mActivity, String target) {
        if (type.compareToIgnoreCase("PLAY_VIDEO") == 0 || type.compareToIgnoreCase("CWYT_VIDEO") == 0) {
            Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(mActivity, target, true, true);
            intent.setPackage("com.google.android.youtube.tv");
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_PLAYLIST") == 0) {
            Intent intent = YouTubeIntents.createOpenPlaylistIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("PLAY_PLAYLIST") == 0 || type.compareToIgnoreCase("CWYT_PLAYLIST") == 0) {
            Intent intent = YouTubeIntents.createPlayPlaylistIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_CHANNEL") == 0) {
            Intent intent = YouTubeIntents.createChannelIntent(mActivity, target);
            intent.setPackage("com.google.android.youtube.tv");
            intent.putExtra("finish_on_ended", true);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_USER") == 0) {
            Intent intent = YouTubeIntents.createUserIntent(mActivity, target);
            mActivity.startActivity(intent);
        } else if (type.compareToIgnoreCase("OPEN_SEARCH") == 0) {
            Intent intent = YouTubeIntents.createSearchIntent(mActivity, target);
            mActivity.startActivity(intent);
        } else {
            return false;
        }
        return true;
    }

}
