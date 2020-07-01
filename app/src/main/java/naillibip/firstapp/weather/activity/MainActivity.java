package naillibip.firstapp.weather.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import naillibip.firstapp.weather.R;
import naillibip.firstapp.weather.callBackInterfaces.IAddCityCallback;
import naillibip.firstapp.weather.fragments.CitiesListScreenFragment;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    IAddCityCallback addCityCallback;
    DrawerLayout drawerLayout;
    CitiesListScreenFragment fr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        fr = new CitiesListScreenFragment();

        setSupportActionBar(toolbar);
        setFloatingButton();
        setNavigationViewListener();
        drawerLayout = findViewById(R.id.drawer);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fr).commit();
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.id1: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fr).commit();
                break;
            }

            case R.id.settings: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingsFragment.newInstance()).commit();
                break;
            }
            case R.id.obr: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, FeedbackFragment.newInstance()).commit();
                break;
            }
            case R.id.about: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AboutFragment.newInstance()).commit();
                break;
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFloatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.requireNonNull(fr).addCityToList()) {
                    Snackbar.make(view, "Город добавлен", Snackbar.LENGTH_LONG)
                            .setAction("Отмена", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fr.deleteCityFromList();
                                }
                            }).show();
                }
            }
        });
    }
}