package tv.cloudwalker.cloudwalkerworld.Utils;

import android.support.v17.leanback.widget.ObjectAdapter;
import android.util.Log;

public class MovieDataObserver extends ObjectAdapter.DataObserver
{
    public static final String TAG = MovieDataObserver.class.getSimpleName();
    public MovieDataObserver() {
        super();
        Log.d(TAG, "MovieDataObserver: ");
    }

    @Override
    public void onChanged() {
        super.onChanged();
        Log.d(TAG, "onChanged: ");
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        Log.d(TAG, "onItemRangeChanged: ps"+positionStart+" ic "+itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        Log.d(TAG, "onItemRangeChanged: ps"+positionStart+" ic "+itemCount +" pl "+payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        Log.d(TAG, "onItemRangeInserted: "+positionStart+" ic "+itemCount);
    }

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        super.onItemMoved(fromPosition, toPosition);
        Log.d(TAG, "onItemMoved: "+fromPosition+" "+toPosition);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        Log.d(TAG, "onItemRangeRemoved: "+positionStart+ " "+itemCount);
    }
}
