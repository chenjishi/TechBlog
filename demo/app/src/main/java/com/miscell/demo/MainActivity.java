package com.miscell.demo;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_constraint_layout);

        FrameLayout container = (FrameLayout) findViewById(R.id.container);
        container.removeAllViews();

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, dp2px(44));
        lp.topMargin = dp2px(4);
        lp.bottomMargin = dp2px(10);
        container.addView(getWeatherLayout(), lp);
    }

    private ConstraintLayout getWeatherLayout() {
        int parentId = R.id.weather_layout;

        ConstraintLayout layout = new ConstraintLayout(this);
        layout.setPadding(dp2px(18), 0, dp2px(5), 0);
        layout.setId(parentId);

        ImageView icon = new ImageView(this);
        icon.setId(R.id.weather_icon);
        icon.setImageResource(R.drawable.weather_unknown);
        icon.setScaleType(ImageView.ScaleType.FIT_XY);
        layout.addView(icon);

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        set.connect(icon.getId(), ConstraintSet.LEFT, parentId, ConstraintSet.LEFT);
        set.connect(icon.getId(), ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        set.connect(icon.getId(), ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        set.constrainWidth(icon.getId(), dp2px(44));
        set.constrainHeight(icon.getId(), 0);

        TextView error = new TextView(this);
        error.setId(R.id.weather_error);
        error.setText("网络异常");
        error.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        error.setTextColor(Color.WHITE);
        layout.addView(error);

        set.connect(R.id.weather_error, ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        set.connect(R.id.weather_error, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        set.connect(R.id.weather_error, ConstraintSet.LEFT, R.id.weather_icon, ConstraintSet.RIGHT, dp2px(4));
        set.constrainWidth(R.id.weather_error, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.weather_error, ConstraintSet.WRAP_CONTENT);
        set.setVisibility(R.id.weather_error, View.GONE);

        TextView temperature = new TextView(this);
        temperature.setId(R.id.temperature);
        temperature.setText("17°");
        temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 39);
        temperature.setTextColor(Color.WHITE);
        temperature.setTranslationY(-dp2px(5));
        temperature.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
        layout.addView(temperature);

        set.connect(R.id.temperature, ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        set.connect(R.id.temperature, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        set.connect(R.id.temperature, ConstraintSet.LEFT, R.id.weather_icon, ConstraintSet.RIGHT, dp2px(4));
        set.constrainWidth(R.id.temperature, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.temperature, ConstraintSet.WRAP_CONTENT);

        ImageView slash = new ImageView(this);
        slash.setId(R.id.slash);
        slash.setImageResource(R.drawable.home_weather_slash);
        layout.addView(slash);
        set.constrainWidth(R.id.slash, dp2px(12));
        set.constrainHeight(R.id.slash, dp2px(29));
        set.connect(R.id.slash, ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        set.connect(R.id.slash, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        set.connect(R.id.slash, ConstraintSet.LEFT, R.id.temperature, ConstraintSet.RIGHT);

        TextView city = new TextView(this);
        city.setId(R.id.city);
        city.setText("北京");
        city.setTextColor(Color.WHITE);
        city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        layout.addView(city);
        set.constrainWidth(R.id.city, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.city, ConstraintSet.WRAP_CONTENT);
        set.connect(R.id.city, ConstraintSet.LEFT, R.id.slash, ConstraintSet.RIGHT, dp2px(7));
        set.connect(R.id.city, ConstraintSet.BOTTOM, R.id.air_condition, ConstraintSet.TOP);
        set.connect(R.id.city, ConstraintSet.TOP, parentId, ConstraintSet.TOP);
        set.setVerticalChainStyle(R.id.city, ConstraintSet.CHAIN_PACKED);

        TextView airCondition = new TextView(this);
        airCondition.setText("良");
        airCondition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        airCondition.setTextColor(Color.WHITE);
        airCondition.setId(R.id.air_condition);
        layout.addView(airCondition);
        set.constrainWidth(R.id.air_condition, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.air_condition, ConstraintSet.WRAP_CONTENT);
        set.connect(R.id.air_condition, ConstraintSet.BOTTOM, parentId, ConstraintSet.BOTTOM);
        set.connect(R.id.air_condition, ConstraintSet.TOP, R.id.city, ConstraintSet.BOTTOM, dp2px(1));
        set.connect(R.id.air_condition, ConstraintSet.LEFT, R.id.city, ConstraintSet.LEFT);

        TextView weather = new TextView(this);
        weather.setText("小雨");
        weather.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        weather.setTextColor(Color.WHITE);
        weather.setId(R.id.weather);
        layout.addView(weather);

        set.constrainWidth(R.id.weather, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.weather, ConstraintSet.WRAP_CONTENT);
        set.connect(R.id.weather, ConstraintSet.LEFT, R.id.city, ConstraintSet.RIGHT, dp2px(9));
        set.connect(R.id.weather, ConstraintSet.TOP, R.id.city, ConstraintSet.TOP);

        TextView airIndex = new TextView(this);
        airIndex.setText("94");
        airIndex.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        airIndex.setTextColor(Color.WHITE);
        airIndex.setId(R.id.air_index);
        layout.addView(airIndex);
        set.constrainWidth(R.id.air_index, ConstraintSet.WRAP_CONTENT);
        set.constrainHeight(R.id.air_index, ConstraintSet.WRAP_CONTENT);
        set.connect(R.id.air_index, ConstraintSet.LEFT, R.id.air_condition, ConstraintSet.RIGHT, dp2px(5));
        set.connect(R.id.air_index, ConstraintSet.TOP, R.id.air_condition, ConstraintSet.TOP);

        set.applyTo(layout);
        return layout;
    }

    private int dp2px(int dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics()));
    }
}
