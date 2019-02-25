/*
 * Copyright (C) 2014 The Android Open Source Project
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

package tv.cloudwalker.cloudwalkerworld.CustomPresenter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition;

import java.util.ArrayList;

import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.module.MovieTile;

import static tv.cloudwalker.cloudwalkerworld.Utils.Utils.isPackageInstalled;

/*
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an Image CardView
 */
public class CardPresenter extends Presenter {

    private static final int CARD_WIDTH = 313;
    private static final int CARD_HEIGHT = 176;
    private static int sSelectedBackgroundColor;
    private static int sDefaultBackgroundColor;
    private Drawable mDefaultCardImage;
    private RequestOptions requestOptions = new RequestOptions().centerCrop();

    private static void updateCardBackgroundColor(ImageCardView view, boolean selected) {
        int color = selected ? sSelectedBackgroundColor : sDefaultBackgroundColor;
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color);
        view.findViewById(R.id.info_field).setBackgroundColor(color);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {

//        sDefaultBackgroundColor =
//                ContextCompat.getColor(parent.getContext(), R.color.default_background);
//        sSelectedBackgroundColor =
//                ContextCompat.getColor(parent.getContext(), R.color.selected_background);
//        /*
//         * This template uses a default image in res/drawable, but the general case for Android TV
//         * will require your resources in xhdpi. For more information, see
//         * https://developer.android.com/training/tv/start/layouts.html#density-resources
//         */
//        mDefaultCardImage = ContextCompat.getDrawable(parent.getContext(), R.drawable.movie);

        ImageCardView cardView = new ImageCardView(parent.getContext());
        cardView.setInfoAreaBackground(ContextCompat.getDrawable(parent.getContext(),R.drawable.tile_gradient));
        cardView.setCardType(ImageCardView.CARD_TYPE_INFO_OVER);
        cardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_SELECTED);

//        ImageCardView cardView =
//                new ImageCardView(parent.getContext()) {
//                    @Override
//                    public void setSelected(boolean selected) {
//                        updateCardBackgroundColor(this, selected);
//                        super.setSelected(selected);
//                    }
//                };
//
//        cardView.setFocusable(true);
//        cardView.setFocusableInTouchMode(true);
//        updateCardBackgroundColor(cardView, false);


        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        MovieTile movie = (MovieTile) item;
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        if (movie.getPoster() != null) {
            cardView.setTitleText(movie.getTitle());
            cardView.setContentText(movie.getTileContentText());
            cardView.setBadgeImage(movie.getTileBadgeIcon());
            if(movie.getRowLayout() == null || movie.getRowLayout().equalsIgnoreCase("landscape"))
            {
                cardView.setMainImageDimensions(
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tileLandScapeWidth)),
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tileLandScapeHeight)));
                Glide.with(viewHolder.view.getContext())
                        .load(movie.getPoster())
                        .into(cardView.getMainImageView());

            }else if(movie.getRowLayout().equalsIgnoreCase("portrait"))
            {
                cardView.setMainImageDimensions(
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tilePotraitWidth)),
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tilePotraitHeight)));
                Glide.with(viewHolder.view.getContext())
                        .load(movie.getPortrait())
                        .into(cardView.getMainImageView());

            }else if(movie.getRowLayout().equalsIgnoreCase("square"))
            {
                cardView.setMainImageDimensions(
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tileSquareWidth)),
                        convertDpToPixel(viewHolder.view.getContext(), viewHolder.view.getContext().getResources().getInteger(R.integer.tileSquareHeight)));
                Glide.with(viewHolder.view.getContext())
                        .load(movie.getPortrait())
                        .into(cardView.getMainImageView());
            }

        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ImageCardView cardView = (ImageCardView) viewHolder.view;
        // Remove references to images so that the garbage collector can free up memory
        cardView.setBadgeImage(null);
        cardView.setMainImage(null);
    }

    private int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
