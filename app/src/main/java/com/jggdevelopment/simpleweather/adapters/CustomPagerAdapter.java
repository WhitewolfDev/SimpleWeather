package com.jggdevelopment.simpleweather.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import com.jggdevelopment.simpleweather.R;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.jggdevelopment.simpleweather.fragments.DailyWeatherFragment;
import com.jggdevelopment.simpleweather.fragments.HourlyWeatherFragment;
import com.jggdevelopment.simpleweather.fragments.NowWeatherFragment;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public CustomPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NowWeatherFragment();
            case 1:
                return new HourlyWeatherFragment();
            case 2:
                return new DailyWeatherFragment();
            default:
                return new NowWeatherFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.now);
            case 1:
                return context.getString(R.string.hourly);
            case 2:
                return context.getString(R.string.daily);
            default:
                return null;
        }
    }
}
