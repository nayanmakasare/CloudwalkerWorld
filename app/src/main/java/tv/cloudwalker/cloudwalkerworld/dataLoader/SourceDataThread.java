package tv.cloudwalker.cloudwalkerworld.dataLoader;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;

import java.util.ArrayList;

import tv.cloudwalker.cloudwalkerworld.CustomPresenter.CharacterCardPresenter;
import tv.cloudwalker.cloudwalkerworld.Utils.OttoBus;
import tv.cloudwalker.cloudwalkerworld.module.SourceInfo;

public class SourceDataThread extends Thread {

    public SourceDataThread() {
        OttoBus.getBus().register(this);
    }


    @Override
    public void run() {
        loadSourceData();
    }

    private void loadSourceData() {
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
        ListRow listRow = new ListRow(new HeaderItem(3, "Source"), sourceAdapter);
        OttoBus.getBus().post(listRow);
    }
}
