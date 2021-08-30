package com.example.rupali.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    ImageButton btnMap;
    Bundle bundle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bundle=getIntent().getExtras();

    }

    public void openMap(View view)
    {
        Intent intent=new Intent(this,MapsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void openManualSearch(View view)
    {
        Intent intent=new Intent(this,ManualActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
