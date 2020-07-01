package naillibip.firstapp.weather.activity;

import android.os.Bundle;

import naillibip.firstapp.weather.fragments.TempScreenFragment;
import naillibip.firstapp.weather.R;

public class TempScreenActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_screen);

        if (savedInstanceState == null) {
            // Если эта activity запускается первый раз
            // то перенаправим параметр фрагменту
            TempScreenFragment details = new TempScreenFragment();
            details.setArguments(getIntent().getExtras());
            // Добавим фрагмент на activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, details)
                    .commit();
        }
    }
}
