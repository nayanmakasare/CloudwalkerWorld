/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package tv.cloudwalker.cloudwalkerworld;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import tv.cloudwalker.cloudwalkerworld.CustomPresenter.CardPresenter;
import tv.cloudwalker.cloudwalkerworld.CustomPresenter.CharacterCardPresenter;
import tv.cloudwalker.cloudwalkerworld.Utils.OttoBus;
import tv.cloudwalker.cloudwalkerworld.api.ApiClient;
import tv.cloudwalker.cloudwalkerworld.api.ApiInterface;
import tv.cloudwalker.cloudwalkerworld.module.MovieResponse;
import tv.cloudwalker.cloudwalkerworld.module.MovieRow;
import tv.cloudwalker.cloudwalkerworld.module.MovieTile;
import tv.cloudwalker.cloudwalkerworld.module.SettingsModel;
import tv.cloudwalker.cloudwalkerworld.module.SourceInfo;

import static tv.cloudwalker.cloudwalkerworld.Utils.Utils.isPackageInstalled;

public class MainFragment extends BrowseFragment {
    private static final String TAG = "MainFragment";

    private static final int BACKGROUND_UPDATE_DELAY = 300;
    private static final int GRID_ITEM_WIDTH = 200;
    private static final int GRID_ITEM_HEIGHT = 200;
    private static final int NUM_ROWS = 6;
    private static final int NUM_COLS = 15;

    private final Handler mHandler = new Handler();
    private Drawable mDefaultBackground;
    private DisplayMetrics mMetrics;
    private Timer mBackgroundTimer;
    private String mBackgroundUri;
    private BackgroundManager mBackgroundManager;
    private ArrayObjectAdapter rowsAdapter ;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ApiInterface apiService;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate "+Thread.currentThread().getId());
        super.onActivityCreated(savedInstanceState);

//       apiService =  new Retrofit.Builder()
//                .baseUrl("http://192.168.1.143:9876/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build().create(ApiInterface.class);


        apiService = ApiClient.getClient(getActivity()).create(ApiInterface.class);
        OttoBus.getBus().register(this);
        setupUIElements();

        catsLoad();

//        Thread sourceThread = new SourceDataThread();
//        sourceThread.start();

        setupEventListeners();
    }


    private void settingsLoad()
    {
        Log.d(TAG, "settingsLoad: ");
        Observable<ListRow> settingsListrowObservable = getSettingsObservable();
//        Observable<ListRow> sourceListRowObservable = getSourceObservable();


        disposable.add(
                settingsListrowObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<ListRow>() {
                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(ListRow listRow) {
                                rowsAdapter.add(listRow);
                                rowsAdapter.notifyItemRangeChanged(rowsAdapter.size() -1 , 1);
                            }

                            @Override
                            public void onComplete() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
        );
    }

    private Observable<ListRow> getSettingsObservable() {
        final ListRow listrow = loadSettingsData();

        return Observable.create(new ObservableOnSubscribe<ListRow>() {
            @Override
            public void subscribe(ObservableEmitter<ListRow> emitter) throws Exception {
                         if(!emitter.isDisposed())
                         {
                             Log.d(TAG, "subscribe: settings ");
                             emitter.onNext(listrow);
                         }

                         if(!emitter.isDisposed())
                         {
                             emitter.onComplete();
                         }
            }
        });
    }

    private Observable<ListRow> getSourceObservable() {
        final ListRow listrow = loadSourceData();

        return Observable.create(new ObservableOnSubscribe<ListRow>() {
            @Override
            public void subscribe(ObservableEmitter<ListRow> emitter) throws Exception {
                if(!emitter.isDisposed())
                {
                    Log.d(TAG, "subscribe: Source ");
                    emitter.onNext(listrow);
                }

                if(!emitter.isDisposed()) {
                    emitter.onComplete();
                }
            }
        });
    }

    private ListRow loadSourceData() {
        ArrayList<SourceInfo> sourceInfos = new ArrayList<>();
        String[] sourceName = new String[]{"HDMI 1", "HDMI 2", "HDMI 3", "TV", "VGA", "AV", "YPBPR"};
        Integer[] sourceId = new Integer[]{
                2131230850, 2131230850, 2131230850, 2131230965, 2131230967, 2131230829, 2131230968
        };

        for (int i = 0; i < sourceName.length; i++) {
            SourceInfo sourceInfo = new SourceInfo(sourceName[i], null, sourceId[i], 0);
            sourceInfos.add(sourceInfo);
        }
        ArrayObjectAdapter sourceAdapter = new ArrayObjectAdapter(new CharacterCardPresenter());
        sourceAdapter.addAll(0, sourceInfos);
        return new ListRow(new HeaderItem(3, "Source"), sourceAdapter);
    }


    @Subscribe
    public void OttoMessage(ListRow listRow){
        rowsAdapter.add(listRow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
//        if (null != mBackgroundTimer) {
//            Log.d(TAG, "onDestroy: " + mBackgroundTimer.toString());
//            mBackgroundTimer.cancel();
//        }
    }


    private void catsLoad(){
        disposable.add(
                apiService.getHomeScreenData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<MovieResponse>() {

                            @Override
                            protected void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onNext(MovieResponse movieResponse) {
                                Log.d(TAG, "onNext: "+Thread.currentThread().getId());
                                for(MovieRow movieRow : movieResponse.getRows())
                                {
                                    ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(new CardPresenter());
                                    for(MovieTile movieTile : movieRow.getRowItems()){
                                        movieTile.setRowLayout(movieRow.getRowLayout());
                                        setTileContent(movieTile);
                                        setTileDrawable(movieTile , getActivity());
                                        cardRowAdapter.add(movieTile);
                                    }
                                    HeaderItem header = new HeaderItem(movieRow.getRowIndex(), movieRow.getRowHeader());
                                    rowsAdapter.add(new ListRow(header, cardRowAdapter));
                                }
                            }

                            @Override
                            public void onComplete() {
                                Log.d(TAG, "onComplete: ");
//                                settingsLoad();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d(TAG, "onError: ");
                            }
                        })
        );
    }


    private ListRow loadSettingsData()
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

        ArrayObjectAdapter settingsAdapter = new ArrayObjectAdapter(new CardPresenter());
        settingsAdapter.addAll(0, settingsList);
        return new ListRow(new HeaderItem(4, "Settings"), settingsAdapter);
    }


    private void setupUIElements() {
        
//        ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false);
//        listRowPresenter.enableChildRoundedCorners(true);
//        listRowPresenter.setKeepChildForeground(true);
//        listRowPresenter.setShadowEnabled(false);
//        listRowPresenter.setSelectEffectEnabled(false);

        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setAdapter(rowsAdapter);
        setBadgeDrawable(getActivity().getResources().getDrawable(R.drawable.brand_logo));
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);
        setBrandColor(ContextCompat.getColor(getActivity(), R.color.fastlane_background));
        setSearchAffordanceColor(ContextCompat.getColor(getActivity(), R.color.search_opaque));

        BackgroundManager mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
        mBackgroundManager.setColor(ContextCompat.getColor(getActivity(), R.color.default_bg));

    }

    private void setupEventListeners() {
        setOnSearchClickedListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AllAppsActivity.class);
                startActivity(intent);
            }
        });

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof MovieTile) {
                MovieTile movie = (MovieTile) item;
//                Map<String , String> tileClickedMap = new HashMap<>();
//                tileClickedMap.put("tileName", movie.getTitle());
//                tileClickedMap.put("rowPosition", String.valueOf(((ArrayObjectAdapter)((ListRow)row).getAdapter()).indexOf(item)));
//                FlurryAgent.logEvent(getString(R.string.movie_tile_clicked), tileClickedMap, true);
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME)
                        .toBundle();
                getActivity().startActivity(intent, bundle);
            }
            else if (item instanceof String) {
                if (((String) item).contains(getString(R.string.error_fragment))) {
                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), ((String) item), Toast.LENGTH_SHORT).show();
                }
            }
            else if (item instanceof SettingsModel) {
                SettingsModel settingsModel = (SettingsModel) item;
                if (settingsModel.isSetting()) {
                    Intent intent = new Intent();
//                    intent.setClassName("com.cvte.tv.androidsetting", "com.cvte.tv.androidsetting.GlobalSettingActivity");
                    intent.setClassName("com.cvte.settings", ((SettingsModel) item).getClassName());
                    if (settingsModel.getAction().isEmpty()) {
                        intent.putExtra("FlagToOpenNetworkSetting", true);
                    } else {
                        intent.setAction(settingsModel.getAction());
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        getActivity().startActivity(intent);
                    }catch (ActivityNotFoundException e) {
                        if(settingsModel.getAction().isEmpty()){
                            intent.setAction(Settings.ACTION_WIFI_SETTINGS);
                        }else {
                            intent.setAction(settingsModel.getAction());
                        }
                        getActivity().startActivity(intent);
                    }
                }
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(
                Presenter.ViewHolder itemViewHolder,
                Object item,
                RowPresenter.ViewHolder rowViewHolder,
                Row row) {
//            if (item instanceof MovieTile) {
//                mBackgroundUri = ((MovieTile) item).getBackground();
//                startBackgroundTimer();
//            }
        }
    }

//    private class UpdateBackgroundTask extends TimerTask {
//
//        @Override
//        public void run() {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    updateBackground(mBackgroundUri);
//                }
//            });
//        }
//    }





    private void setTileContent(MovieTile movieTile){
        ArrayList<String> subText = new ArrayList<>();
        if (movieTile.getRuntime() != null && movieTile.getRuntime().length() > 0) {
            subText.add(movieTile.getRuntime());
        }
        if (movieTile.getYear() != null && movieTile.getYear().length() > 0) {
            subText.add(movieTile.getYear());
        }
        if (movieTile.getGenre() != null) {
            subText.addAll(movieTile.getGenre());
            movieTile.setTileContentText(getSeperatedValuesWithHeader(" | ", "", subText));
        }
    }

    private String getSeperatedValuesWithHeader(String seperator, String header, ArrayList<String> list) {
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


    private void setTileDrawable(MovieTile movie, Context context)
    {
        if (movie.getPackageName() != null && !movie.getPackageName().equalsIgnoreCase("cloudwalker.webview")) {
            try {
                Drawable icon;
                if (movie.getPackageName().equalsIgnoreCase("com.google.android.youtube")) {
                    if (isPackageInstalled(movie.getPackageName() + ".tv", context.getPackageManager())) {
                        icon = context.getPackageManager().getApplicationIcon(movie.getPackageName() + ".tv");
                    } else {
                        icon = context.getPackageManager().getApplicationIcon(movie.getPackageName());
                    }
                } else {
                    icon = context.getPackageManager().getApplicationIcon(movie.getPackageName());
                }
                movie.setTileBadgeIcon(icon);
            }catch (PackageManager.NameNotFoundException e) {
                e.getMessage();
            }catch (NullPointerException e) {
                e.getMessage();
            }
        }
    }
}






















//    private void loadRows() {
//        List<Movie> list = MovieList.setupMovies();
//
//        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
//        CardPresenter cardPresenter = new CardPresenter();
//
//        int i;
//        for (i = 0; i < NUM_ROWS; i++) {
//            if (i != 0) {
//                Collections.shuffle(list);
//            }
//            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
//            for (int j = 0; j < NUM_COLS; j++) {
//                listRowAdapter.add(list.get(j % 5));
//            }
//            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
//            rowsAdapter.add(new ListRow(header, listRowAdapter));
//        }
//
//        HeaderItem gridHeader = new HeaderItem(i, "PREFERENCES");
//
//        GridItemPresenter mGridPresenter = new GridItemPresenter();
//        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
//        gridRowAdapter.add(getResources().getString(R.string.grid_view));
//        gridRowAdapter.add(getString(R.string.error_fragment));
//        gridRowAdapter.add(getResources().getString(R.string.personal_settings));
//        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));
//
//        setAdapter(rowsAdapter);
//    }