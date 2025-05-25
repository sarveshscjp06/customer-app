package com.samcrm.adpater;

import com.samcrm.R;
import com.samcrm.bannerslider.adapters.SliderAdapter;
import com.samcrm.bannerslider.viewholder.ImageSlideViewHolder;


public class BannerSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide(R.drawable.slide1);
                break;
            case 1:
                viewHolder.bindImageSlide(R.drawable.slide1);
                break;
            case 2:
                viewHolder.bindImageSlide(R.drawable.slide1);
                break;
        }
    }

}
