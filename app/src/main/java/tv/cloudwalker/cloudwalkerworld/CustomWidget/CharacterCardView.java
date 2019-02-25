package tv.cloudwalker.cloudwalkerworld.CustomWidget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import tv.cloudwalker.cloudwalkerworld.R;
import tv.cloudwalker.cloudwalkerworld.module.SettingsModel;
import tv.cloudwalker.cloudwalkerworld.module.SourceInfo;

public class CharacterCardView extends BaseCardView {

    public CharacterCardView(Context context) {
        super(context, null, 0);

//        R.style.CharacterCardStyle

        LayoutInflater.from(getContext()).inflate(R.layout.character_card, this);
        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                View container = findViewById(R.id.container);
                if (hasFocus) {
                    container.setBackgroundResource(R.drawable.character_focused);
                } else {
                    container.setBackgroundResource(R.drawable.character_not_focused_padding);
                }
            }
        });
        setFocusable(true);
    }

    public void updateUi(Object card)
    {
        TextView primaryText =  findViewById(R.id.primary_text);
        final ImageView imageView =  findViewById(R.id.main_image);

        if(card instanceof SettingsModel)
        {
            primaryText.setText(((SettingsModel) card).getSettingsName());
            if (((SettingsModel) card).getSettings_icon_resId() != 0) {
                int resourceId = ((SettingsModel) card).getSettings_icon_resId();
                Bitmap bitmap = BitmapFactory
                        .decodeResource(getContext().getResources(), resourceId);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
                drawable.setAntiAlias(true);
                drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 5.5f);
                imageView.setImageDrawable(drawable);
            }
        }
        else if( card instanceof SourceInfo)
        {
            primaryText.setText(((SourceInfo) card).getSourceName());
            if (((SourceInfo) card).getSource_icon_id() != 0) {
                int resourceId = ((SourceInfo) card).getSource_icon_id();
                Bitmap bitmap = BitmapFactory
                        .decodeResource(getContext().getResources(), resourceId);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
                drawable.setAntiAlias(true);
                drawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()) / 5.5f);
                imageView.setImageDrawable(drawable);
                imageView.setPadding(40, 40, 40, 40);
            }
        }
    }
}
