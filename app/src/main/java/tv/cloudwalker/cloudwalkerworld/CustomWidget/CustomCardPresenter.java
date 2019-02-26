package tv.cloudwalker.cloudwalkerworld.CustomWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.module.MovieTile;

import static android.graphics.Bitmap.Config.RGB_565;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by cognoscis on 19/12/17.
 */

public class CustomCardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    public CustomCardPresenter(Context context) {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        final View infoView = v.findViewById(R.id.infoView);
        v.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    infoView.setVisibility(View.VISIBLE);
                }else {
                    infoView.setVisibility(View.INVISIBLE);
                }
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ImageView posterImageView, iconImageView;
        TextView titleTextView , contentTextView;
        posterImageView = (ImageView) viewHolder.view.findViewById(R.id.posterImageView);
        iconImageView = (ImageView) viewHolder.view.findViewById(R.id.iconImageView);
        titleTextView = (TextView) viewHolder.view.findViewById(R.id.titleText);
        contentTextView = (TextView) viewHolder.view.findViewById(R.id.contentText);

        if(item instanceof MovieTile)
        {
            MovieTile movieTile = (MovieTile) item;
            titleTextView.setText(movieTile.getTitle());
            contentTextView.setText(movieTile.getTileContentText());

//            if(movieTile.getTileBadgeIcon() != null){
//                iconImageView.setImageDrawable(movieTile.getTileBadgeIcon());
//            }
//            if(movieTile.getTileWidth() != null && movieTile.getTileHeight() != null){
//                Glide.with(viewHolder.view.getContext())
//                        .load(movieTile.getPortrait())
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .override(Integer.parseInt(movieTile.getTileWidth()), Integer.parseInt(movieTile.getTileHeight()))
//                        .error(R.drawable.movie)
//                        .skipMemoryCache(true)
//                        .into(posterImageView);
//            }else
                {
                setLayoutOfTile(movieTile,viewHolder.view.getContext(),viewHolder.view, posterImageView);
            }
        }
//        else if(item instanceof FavouritesObject) {
//            FavouritesObject favouritesObject = (FavouritesObject) item;
//            titleTextView.setText(favouritesObject.getTitle());
//            if(favouritesObject.getImageUri() != null){
//                Glide.with(viewHolder.view.getContext())
//                        .load(favouritesObject.getImageUri())
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .override(viewHolder.view.getResources().getInteger(R.integer.tileLandScapeWidth), viewHolder.view.getResources().getInteger(R.integer.tileLandScapeHeight))
//                        .error(R.drawable.movie)
//                        .skipMemoryCache(true)
//                        .into(posterImageView);
//                ViewGroup.LayoutParams layoutParams = viewHolder.view.getLayoutParams();
//                layoutParams.width = viewHolder.view.getResources().getInteger(R.integer.tileLandScapeWidth);
//                layoutParams.height = viewHolder.view.getResources().getInteger(R.integer.tileLandScapeHeight);
//                viewHolder.view.setLayoutParams(layoutParams);
//            }
//        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        ImageView posterImageView, iconImageView;
        TextView titleTextView , contentTextView;
        posterImageView = (ImageView) viewHolder.view.findViewById(R.id.posterImageView);
        iconImageView = (ImageView) viewHolder.view.findViewById(R.id.iconImageView);
        titleTextView = (TextView) viewHolder.view.findViewById(R.id.titleText);
        contentTextView = (TextView) viewHolder.view.findViewById(R.id.contentText);
        posterImageView.setImageDrawable(null);
        iconImageView.setImageDrawable(null);
        titleTextView.setText(null);
        contentTextView.setText(null);
    }

    private void setLayoutOfTile(MovieTile movie, final Context context, View view, final ImageView imageView)
    {
        if(movie != null && movie.getRowLayout() != null)
        {
            switch (movie.getRowLayout())
            {
                case"portrait" :
                {
                    if (movie.getTitle().equals("Refresh")) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    } else {
                        int width = dpToPx(context , context.getResources().getInteger(R.integer.tilePotraitWidth));
                        int height = dpToPx(context , context.getResources().getInteger(R.integer.tilePotraitHeight));
                        Glide.with(context)
                                .asBitmap()
                                .load(movie.getPortrait())
                                .into(new SimpleTarget<Bitmap>(width, height){
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Glide.get(context).getBitmapPool().put(resource);
                                        imageView.setImageBitmap(resource);
                                    }
                                });
                    }
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = dpToPx(context , context.getResources().getInteger(R.integer.tilePotraitWidth))  ;
                    layoutParams.height =  dpToPx(context , context.getResources().getInteger(R.integer.tilePotraitHeight));
                    view.setLayoutParams(layoutParams);
                }
                break;

                case "square":
                {
                    if (movie.getTitle().equals("Refresh")) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    } else {
                        int width = dpToPx(context , context.getResources().getInteger(R.integer.tileSquareWidth));
                        int height = dpToPx(context , context.getResources().getInteger(R.integer.tileSquareHeight));
                        Glide.with(context)
                                .asBitmap()
                                .load(movie.getPortrait())
                                .into(new SimpleTarget<Bitmap>(width, height){
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Glide.get(context).getBitmapPool().put(resource);
                                        imageView.setImageBitmap(resource);
                                    }
                                });
                    }
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = dpToPx(context , context.getResources().getInteger(R.integer.tileSquareWidth)) ;
                    layoutParams.height =  dpToPx(context , context.getResources().getInteger(R.integer.tileSquareHeight));
                    view.setLayoutParams(layoutParams);
                }
                break;

                case "landscape":
                {
                    if (movie.getTitle().equals("Refresh")) {
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    } else {
                        final int width = dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeWidth));
                        final int height = dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeHeight));
                        Glide.with(context)
                                .asBitmap()
                                .load(movie.getPoster())
                                .into(new SimpleTarget<Bitmap>(width, height){
                                    @Override
                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                        Glide.get(context).getBitmapPool().put(resource);
                                        imageView.setImageBitmap(Glide.get(context).getBitmapPool().get(width,height, RGB_565));
                                    }
                                });
                    }
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeWidth))  ;
                    layoutParams.height =  dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeHeight));
                    view.setLayoutParams(layoutParams);
                }
                break;
            }
        }
        else
        {
            if (movie.getTitle().equals("Refresh")) {
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } else {
                final int width = dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeWidth));
                final int height = dpToPx(context , context.getResources().getInteger(R.integer.tileLandScapeHeight));
                Glide.with(context)
                        .asBitmap()
                        .load(movie.getPoster())
                        .into(new SimpleTarget<Bitmap>(width, height){
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                Glide.get(context).getBitmapPool().put(resource);
                                imageView.setImageBitmap(Glide.get(context).getBitmapPool().get(width,height, RGB_565));
                            }
                        });
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = dpToPx(context , context.getResources().getInteger(R.integer.defaulttileWidth))  ;
            layoutParams.height =  dpToPx(context , context.getResources().getInteger(R.integer.deafulttileHeight));
            view.setLayoutParams(layoutParams);
        }
    }

    private int dpToPx(Context ctx , int dp) {
        float density = ctx.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
