package com.samcrm.bannerslider.adapters;


import com.samcrm.bannerslider.SlideType;
import com.samcrm.bannerslider.viewholder.ImageSlideViewHolder;

public abstract class SliderAdapter {
    public abstract int getItemCount();

    public final SlideType getSlideType(int position) {
        return SlideType.IMAGE;
    }

    public abstract void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder);
}
