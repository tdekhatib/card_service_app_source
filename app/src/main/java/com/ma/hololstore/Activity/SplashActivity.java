package com.ma.hololstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ma.hololstore.Activity.LoginActivity;
import com.ma.hololstore.Classes.Config;
import com.ma.hololstore.R;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private static final long SPLASH_SCREEN_TIME_IN_MILLIS =3000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        Config.CreateDB(this);
     //   startService(new Intent(SplashActivity.this,ISMService.class));
        handler = new Handler() ;
    }

    Handler handler;
    @Override
    protected void onResume() {
        super.onResume();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(SPLASH_SCREEN_TIME_IN_MILLIS);
                    handler.post(new Runnable() {
                        public void run() {
                            goToNextScreen();
                        }
                    });
                } catch (InterruptedException e) {}
            }
        };
        thread.start();
    }

    protected void goToNextScreen() {

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);


        finish();
    }
}
