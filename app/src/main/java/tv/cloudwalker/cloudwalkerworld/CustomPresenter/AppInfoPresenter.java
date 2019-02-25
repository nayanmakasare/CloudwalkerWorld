package tv.cloudwalker.cloudwalkerworld.CustomPresenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v17.leanback.widget.Presenter;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tv.cloudwalker.cloudwalkerworld.AllAppsActivity;
import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.module.AppInfo;

/**
 * Created by cognoscis on 5/1/18.
 */

public class AppInfoPresenter extends Presenter {
    private Context myContext;

    public AppInfoPresenter(Context context) {
        myContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_info_presenter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final Presenter.ViewHolder viewHolder, final Object item) {
        if (item instanceof AppInfo) {

            AppInfo appInfo = (AppInfo) item;
            ViewHolder holder = (ViewHolder) viewHolder;

            if (((AppInfo) item).isIconAvailable() || ((AppInfo) item).getAppName().compareToIgnoreCase("More Apps") == 0) {
                holder.view.setBackground(appInfo.getAppIcon());
            } else {
                holder.view.setBackgroundColor(holder.view.getContext().getResources().getColor(R.color.app_tile_background_color));
                holder.appNameTextView.setText(appInfo.getAppName());
                holder.appIconImageView.setImageDrawable(appInfo.getAppIcon());
            }

            if (appInfo.isApp()) {

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String packname = ((AppInfo) item).getPackageName();
                        Intent launchIntent = viewHolder.view.getContext().getPackageManager().getLaunchIntentForPackage(packname);
                        if (launchIntent != null) {
                            viewHolder.view.getContext().startActivity(launchIntent);//null pointer check in case package name was not found
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                launchIntent = viewHolder.view.getContext().getPackageManager().getLeanbackLaunchIntentForPackage(packname);
                                if (launchIntent != null) {
                                    viewHolder.view.getContext().startActivity(launchIntent);//null pointer check in case package name was not found
                                }
                            }
                        }
                    }
                });

                holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (myContext instanceof AllAppsActivity) {
                            AppInfo appInfo = (AppInfo) item;
                            Intent intent = new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
                            myContext.startActivity(intent);
                        }
                        return true;
                    }
                });
            }

            holder.view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if(hasFocus) {
                        view.findViewById(R.id.app_tile_focus_layout).setVisibility(View.VISIBLE);
                    }else {
                        view.findViewById(R.id.app_tile_focus_layout).setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;
        viewHolder.view.setBackground(null);
        holder.appNameTextView.setText(null);
        holder.appIconImageView.setImageDrawable(null);
    }

    static class ViewHolder extends Presenter.ViewHolder {
        private AppCompatImageView appIconImageView;
        private TextView appNameTextView;

        ViewHolder(View view) {
            super(view);

            appIconImageView = view.findViewById(R.id.appIconImageView);
            appNameTextView = view.findViewById(R.id.appNameTextView);
        }

    }

}
