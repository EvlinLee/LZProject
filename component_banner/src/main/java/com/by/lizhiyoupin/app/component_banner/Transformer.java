package com.by.lizhiyoupin.app.component_banner;


import com.by.lizhiyoupin.app.component_banner.transformer.AccordionTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.BackgroundToForegroundTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.CubeInTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.CubeOutTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.DefaultTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.DepthPageTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.FlipHorizontalTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.FlipVerticalTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.ForegroundToBackgroundTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.RotateDownTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.RotateUpTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.ScaleInOutTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.StackTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.TabletTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.ZoomInTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.ZoomOutSlideTransformer;
import com.by.lizhiyoupin.app.component_banner.transformer.ZoomOutTranformer;

import androidx.viewpager.widget.ViewPager;

public class Transformer {
    public static Class<? extends ViewPager.PageTransformer> Default = DefaultTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Accordion = AccordionTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> BackgroundToForeground = BackgroundToForegroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ForegroundToBackground = ForegroundToBackgroundTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeIn = CubeInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> CubeOut = CubeOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> DepthPage = DepthPageTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipHorizontal = FlipHorizontalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> FlipVertical = FlipVerticalTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateDown = RotateDownTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> RotateUp = RotateUpTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ScaleInOut = ScaleInOutTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Stack = StackTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> Tablet = TabletTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomIn = ZoomInTransformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOut = ZoomOutTranformer.class;
    public static Class<? extends ViewPager.PageTransformer> ZoomOutSlide = ZoomOutSlideTransformer.class;
}
