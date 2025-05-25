package com.samcrm.bannerslider;

import android.support.annotation.DrawableRes;
import android.widget.ImageView;


public interface ImageLoadingService {
    void loadImage(String url, ImageView imageView);

    void loadImage(@DrawableRes int resource, ImageView imageView);

    void loadImage(String url, @DrawableRes int placeHolder, @DrawableRes int errorDrawable, ImageView imageView);
}
