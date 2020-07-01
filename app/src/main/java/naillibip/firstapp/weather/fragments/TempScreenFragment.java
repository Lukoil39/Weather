package naillibip.firstapp.weather.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import naillibip.firstapp.weather.adapters.TempHistoryRecyclerViewAdapter;
import naillibip.firstapp.weather.R;
import naillibip.firstapp.weather.TempHistoryCard;
import naillibip.firstapp.weather.WeatherDataLoader;

public class TempScreenFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tempTodayTopTextView, humidityTextView,
            overcastTextView, cityTextView, todayDateTextView;
    private ImageButton toWikipediaBtn, backArrowBtn;
    private int humidityValue;
    private String iconeCode;
    private int weatherPhoto  = R.drawable.clear;
    private ImageView imageWeatherPhoto;
    private double tempToday;
    private ImageView imageView;
    private String cityName, overcastValue, yesterdayDateString, twoDaysAgoDateString,
            threeDaysAgoDateString;
    private final String HUMIDITY_VALUE_KEY = "HUMIDITY_VALUE_KEY", OVERCAST_VALUE_KEY = "overcast",
            TEMP_TODAY_KEY = "TEMP_TODAY_KEY", CITY_NAME_KEY = "cityNameKey";

    private final Handler handler = new Handler();
    private static final String LOG_TAG = "WeatherFragment";
    private NestedScrollView nestedScrollView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            humidityValue = savedInstanceState.getInt(HUMIDITY_VALUE_KEY);
            overcastValue = savedInstanceState.getString(OVERCAST_VALUE_KEY);
            tempToday = savedInstanceState.getDouble(TEMP_TODAY_KEY);
            cityName = savedInstanceState.getString(CITY_NAME_KEY);
            setValues();
;

        }
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_temp_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setDays();
        initRecyclerView();
        showBackButton();
        onBackArrowBtnClicked();
        Glide.with(this).load(weatherPhoto).into(imageWeatherPhoto);
        //При создании фрагмента, экран фокусировался на RecyclerView, только это помогло:
        if (nestedScrollView.getParent() != null) nestedScrollView.getParent().requestChildFocus(nestedScrollView, nestedScrollView);


    }

    private void showBackButton() {
         backArrowBtn.setVisibility(View.VISIBLE);
    }

    private void onBackArrowBtnClicked() {
        backArrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initViews(View view) {
        tempTodayTopTextView = view.findViewById(R.id.temperatureValue);
        humidityTextView = view.findViewById(R.id.humidityValue);
        overcastTextView = view.findViewById(R.id.overcastValue);
        cityTextView = view.findViewById(R.id.cityTextView);
        imageView = (ImageView) view.findViewById(R.id.weatherImage);
        todayDateTextView = view.findViewById(R.id.todayDate);
        recyclerView = view.findViewById(R.id.recycleViewTempHistory);
        nestedScrollView = view.findViewById(R.id.ScrollView);
        backArrowBtn = view.findViewById(R.id.back_arrow);
        imageWeatherPhoto = view.findViewById(R.id.weatherPhoto);
    }

    private void setDays() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date todayDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterdayDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date twoDaysAgoDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date threeDaysAgoDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        //Даты в RecyclerView
        yesterdayDateString = dateFormat.format(yesterdayDate);
        twoDaysAgoDateString = dateFormat.format(twoDaysAgoDate);
        threeDaysAgoDateString = dateFormat.format(threeDaysAgoDate);
        //Дата под городом
        String todayDateString = dateFormat.format(todayDate);
        todayDateTextView.setText(todayDateString);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(HUMIDITY_VALUE_KEY, humidityValue);
        savedInstanceState.putString(OVERCAST_VALUE_KEY, overcastValue);
        savedInstanceState.putDouble(TEMP_TODAY_KEY, tempToday);
        savedInstanceState.putString(CITY_NAME_KEY, cityName);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void setValues() {
        cityTextView.setText(cityName);
        tempTodayTopTextView.setText(String.valueOf(tempToday));
        overcastTextView.setText(overcastValue);
        humidityTextView.setText(String.valueOf(humidityValue).concat("%"));

        iconeCode = "01d.png";
        switch (overcastValue.toLowerCase()){
            case "drizzle" : {
                iconeCode = "09d.png";
                weatherPhoto = R.drawable.drizzle;
                break;
            }
            case "thunderstorm" : {
                iconeCode = "11d.png";
                weatherPhoto = R.drawable.thunder;
                break;
            }
            case "clear" : {
                iconeCode = "01d.png";
                weatherPhoto = R.drawable.clear;
                break;
            }
            case "rain" : {
                iconeCode = "10d.png";
                weatherPhoto = R.drawable.rain;
                break;
            }
            case "snow" : {
                iconeCode = "13d.png";
                weatherPhoto = R.drawable.snow;
                break;
            }
            case "clouds" : {
                iconeCode = "03d.png";
                weatherPhoto = R.drawable.clouds;
                break;
            }
        }
        Glide.with(this).load(weatherPhoto).into(imageWeatherPhoto);
        Glide.with(this).load("http://openweathermap.org/img/wn/" + iconeCode).into(imageView);
    }

    private void updateWeatherOnScreen(){
        if (getArguments()!= null) {
            updateWeatherData(getArguments().getString("index"));
        }
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = WeatherDataLoader.getJSONData(Objects.requireNonNull(getActivity()), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), getString(R.string.city_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else handler.post(new Runnable() {
                    @Override
                    public void run() {
                        renderWeather(json);
                    }
                });
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        Log.d(LOG_TAG, "json " + json.toString());
        try {
            cityName = json.getString("name");
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            tempToday = main.getDouble("temp");
            humidityValue = main.getInt("humidity");
            overcastValue = details.getString("main");
            setValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    static TempScreenFragment newInstance(String city) {
        Bundle args = new Bundle();
        TempScreenFragment fragment = new TempScreenFragment();
        args.putString("index", city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        updateWeatherOnScreen();
        super.onAttach(context);
    }

    String getCityName() {
        cityName = Objects.requireNonNull(getArguments()).getString("index");
        try {
            return cityName;
        } catch (Exception e) {
            return "";
        }
    }

    private void initRecyclerView() {
        TempHistoryCard[] data = new TempHistoryCard[] {
                new TempHistoryCard(yesterdayDateString, 0),
                new TempHistoryCard(twoDaysAgoDateString, 1),
                new TempHistoryCard(threeDaysAgoDateString, 2),
        };
        ArrayList<TempHistoryCard> list = new ArrayList<>(data.length);
        list.addAll(Arrays.asList(data));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        TempHistoryRecyclerViewAdapter adapter = new TempHistoryRecyclerViewAdapter(list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);
    }
}

