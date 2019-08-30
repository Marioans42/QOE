package mg.telma.qoe;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import mg.telma.qoe.adapter.MainPagerAdapter;
import mg.telma.qoe.enums.MainScreen;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private MainPagerAdapter mainPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainPagerAdapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        List<MainScreen> screens = new ArrayList<>();
        screens.add(MainScreen.MAIN);
        screens.add(MainScreen.INTERNET);
        screens.add(MainScreen.VIDEO);


        mainPagerAdapter.setItems(screens);

        MainScreen defaultScreen = MainScreen.MAIN;
        getSupportActionBar().setTitle(defaultScreen.getTitleStringId());



        selectBottomNavigationViewMenuItem(defaultScreen.getMenuItemId());
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                MainScreen mainScreen = mainPagerAdapter.getItems().get(position);
                selectBottomNavigationViewMenuItem(mainScreen.getMenuItemId());
                getSupportActionBar().setTitle(mainScreen.getTitleStringId());
            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        MainScreen screen = MainScreen.getMainScreenForMenuItem(menuItem.getItemId());
        if(screen != null) {
            scrollToScreen(screen);
            getSupportActionBar().setTitle(screen.getTitleStringId());
            return true;
        }
        return false;
    }

    private void selectBottomNavigationViewMenuItem (@IdRes int menuItemId) {
        bottomNavigationView.setSelectedItemId(menuItemId);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void scrollToScreen(MainScreen mainScreen) {
        int screenPosition = mainPagerAdapter.getItems().indexOf(mainScreen);
        if(screenPosition != viewPager.getCurrentItem()) {
           viewPager.setCurrentItem(screenPosition);
        }
    }
}
