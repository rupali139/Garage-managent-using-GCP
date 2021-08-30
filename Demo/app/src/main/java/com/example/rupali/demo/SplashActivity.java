package com.example.rupali.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_demo);
        getSupportActionBar().hide();
             Launcher launcher=new Launcher();
             launcher.start();
    }

    private class Launcher extends Thread
    {
        public void run()
        {
            try
            {
                sleep(1000);

            }catch(InterruptedException e)
            {
                e.printStackTrace();
            }
            Intent loginIntent=new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            SplashActivity.this.finish();
        }
    }

}


