package com.youth.banner.loader;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;


public abstract class ImageLoader implements ImageLoaderInterface<View> {

    @Override
    public View createImageView(Context context) {
        View imageView = new ImageView(context);
        return imageView;
    }

}
