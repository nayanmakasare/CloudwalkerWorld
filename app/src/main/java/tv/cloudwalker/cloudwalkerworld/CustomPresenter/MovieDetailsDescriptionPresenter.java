package tv.cloudwalker.cloudwalkerworld.CustomPresenter;

import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.module.MovieTile;

/**
 * Created by cognoscis on 8/1/18.
 */

public class MovieDetailsDescriptionPresenter extends Presenter {

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_details, parent, false);
        return new MovieDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        MovieTile movie = (MovieTile) item;
        MovieDetailsViewHolder holder = (MovieDetailsViewHolder) viewHolder;
        holder.bind(movie);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
