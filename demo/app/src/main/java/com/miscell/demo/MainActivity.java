package com.miscell.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button_A).setOnClickListener(this);
        findViewById(R.id.button_B).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_A:
                startActivity(new Intent(this, CirclePositionActivity.class));
                break;
            case R.id.button_B:
                startActivity(new Intent(this, WeatherActivity.class));
                break;
        }
    }
}
