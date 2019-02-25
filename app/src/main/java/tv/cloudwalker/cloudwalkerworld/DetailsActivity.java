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

package tv.cloudwalker.cloudwalkerworld;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import tv.cloudwalker.cloudwalkerworld.module.MovieTile;

/*
 * Details activity class that loads LeanbackDetailsFragment class
 */
public class DetailsActivity extends Activity {
    public static final String SHARED_ELEMENT_NAME = "hero";
    public static final String MOVIE = "Movie";
    private Drawable detailDrawable;
    private BackgroundManager mBackgroundManager ;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mBackgroundManager = BackgroundManager.getInstance(DetailsActivity.this);
        mBackgroundManager.attach(getWindow());
        getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());
    }

    private void loadBackground(String backgroundUrl){
        Glide.with(this)
                .load(backgroundUrl)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        detailDrawable = resource;
                        mBackgroundManager.setDrawable(resource);
                    }
                });
    }

    @Override
    protected void onStart() {
        if (detailDrawable == null) {
            MovieTile movieTile = this.getIntent().getParcelableExtra(DetailsActivity.MOVIE);
            if (movieTile != null && movieTile.getBackground() != null) {
                loadBackground(movieTile.getBackground());
            } else {
                mBackgroundManager.setDrawable(ContextCompat.getDrawable(this, R.drawable.movie));
            }
        } else {
            mBackgroundManager.setDrawable(detailDrawable);
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        mBackgroundManager.clearDrawable();
        super.onStop();
    }
}
