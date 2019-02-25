package tv.cloudwalker.cloudwalkerworld;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.VerticalGridPresenter;

import java.util.List;

import tv.cloudwalker.cloudwalkerworld.CustomPresenter.AppInfoPresenter;
import tv.cloudwalker.cloudwalkerworld.dataLoader.AppListLoader;
import tv.cloudwalker.cloudwalkerworld.module.AppInfo;


/**
 * Created by nayan_makasare on 3/4/18.
 */

public class AppDrawerFragment extends android.support.v17.leanback.app.VerticalGridFragment implements LoaderManager.LoaderCallbacks<List<AppInfo>>{

    private ArrayObjectAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int LOADER_ID = 1;
        mAdapter = new ArrayObjectAdapter(new AppInfoPresenter(getActivity()));
        getLoaderManager().initLoader(LOADER_ID, null, this);
        setAdapter(mAdapter);
        setUpGridUI();
    }

    private void setUpGridUI() {
        final int NUM_COLUMNS = 4;
        setTitle("All Application");
        VerticalGridPresenter gridPresenter = new VerticalGridPresenter(FocusHighlight.ZOOM_FACTOR_NONE, false);
        gridPresenter.enableChildRoundedCorners(true);
        gridPresenter.setKeepChildForeground(true);
        gridPresenter.setShadowEnabled(false);
        gridPresenter.setNumberOfColumns(NUM_COLUMNS);
        setGridPresenter(gridPresenter);
    }

    @Override
    public Loader<List<AppInfo>> onCreateLoader(int i, Bundle bundle) {
        return new AppListLoader(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onLoadFinished(Loader<List<AppInfo>> loader, List<AppInfo> appInfos) {
        mAdapter.clear();
        mAdapter.addAll(0, appInfos);
    }

    @Override
    public void onLoaderReset(Loader<List<AppInfo>> loader) {
        mAdapter.clear();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.clear();
        mAdapter = null;
    }
}
