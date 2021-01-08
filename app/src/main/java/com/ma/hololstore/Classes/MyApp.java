package com.ma.hololstore.Classes;

import android.app.Application;
import android.content.Context;

import com.ma.hololstore.R;

import androidx.appcompat.widget.AppCompatTextView;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/font2.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

     }


}
