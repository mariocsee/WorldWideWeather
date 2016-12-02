package mariocsee.android.worldwideweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import mariocsee.android.worldwideweather.adapter.WeatherPagerAdapter;
import mariocsee.android.worldwideweather.api.WeatherApi;
import mariocsee.android.worldwideweather.model.WeatherResult;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mariocsee on 12/1/16.
 */

public class ViewWeatherActivity extends AppCompatActivity {

    private ViewPager wPager;
    private PagerAdapter wPagerAdapter;
    private String cityName;
    public Call<WeatherResult> weatherCall;
    public String apiid = "9353f19bd883de927bf3dfdc5c141178";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_weather);

        wPager = (ViewPager) findViewById(R.id.pagerWeather);
        wPagerAdapter = new WeatherPagerAdapter(getSupportFragmentManager());
        wPager.setAdapter(wPagerAdapter);

        cityName = getIntent().getStringExtra(MainActivity.CITY_NAME);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        String cityQuery = cityName.replaceAll(" ", "");
        Log.d("TAG_API", "Querying with city name: " + cityQuery);
        weatherCall = weatherApi.getCityWeather(cityQuery, "metric", apiid);

        setUpToolBar();
    }


    @Override
    public void onBackPressed() {
        if (wPager.getCurrentItem() == 0) {
            // If the user is looking at first page,
            // Back button finishes activity and returns to main
            weatherCall.isCanceled();
            super.onBackPressed();
        } else {
            // Otherwise, go to previous page
            wPager.setCurrentItem(wPager.getCurrentItem() - 1);
        }
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarShow);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(cityName);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weatherCall.isCanceled();
                finish();
            }
        });
    }

    public Call<WeatherResult> getWeatherCall() {
        return weatherCall.clone();
    }

    public String getCityName() {
        return cityName;
    }

}