package com.example.soham.hacker_news_app.activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.soham.hacker_news_app.R;
import com.example.soham.hacker_news_app.fragment.AskHNFragment;
import com.example.soham.hacker_news_app.fragment.BestFragment;
import com.example.soham.hacker_news_app.fragment.JobsFragment;
import com.example.soham.hacker_news_app.fragment.NewFragment;
import com.example.soham.hacker_news_app.fragment.ShowHNFragment;
import com.example.soham.hacker_news_app.fragment.TopFragment;


public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    public static int navItemIndex = 0;

    private static final String TAG_TOP = "Top Stories";
    private static final String TAG_NEW = "New Stories";
    private static final String TAG_BEST = "Best Stories";
    private static final String TAG_SHOW = "Show HN";
    private static final String TAG_ASK = "Ask HN";
    private static final String TAG_JOB = "JOBS";
    public static String CURRENT_TAG = TAG_TOP;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
/*
        fab = (FloatingActionButton) findViewById(R.id.fab);
*/

        navHeader = navigationView.getHeaderView(0);

        txtName = (TextView) navHeader.findViewById(R.id.name);
        //txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        //imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        loadNavHeader();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_TOP;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        //txtName.setText("txtName");
        //txtWebsite.setText("txtWebsite");

        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            toggleFab();
            return;
        }

        Runnable pendingRunnable = new Runnable() {
            @Override
            public void run() {
                android.support.v4.app.Fragment fragment = getHomeFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                ft.replace(R.id.frame, fragment, CURRENT_TAG);
                ft.commitAllowingStateLoss();
            }
        };

        if (pendingRunnable != null) {
            handler.post(pendingRunnable);
        }

        toggleFab();

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private android.support.v4.app.Fragment getHomeFragment() {
        switch(navItemIndex) {
            case 0:
                TopFragment topFragment = new TopFragment();
                return topFragment;
            case 1:
                NewFragment newFragment = new NewFragment();
                return newFragment;
            case 2:
                BestFragment bestFragment = new BestFragment();
                return bestFragment;
            case 3:
                ShowHNFragment showFragment = new ShowHNFragment();
                return showFragment;
            case 4:
                AskHNFragment askFragment = new AskHNFragment();
                return askFragment;
            case 5:
                JobsFragment jobFragment = new JobsFragment();
                return jobFragment;
            default:
                    return new TopFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.nav_top_stories:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_TOP;
                        break;
                    case R.id.nav_new_stories:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_NEW;
                        break;
                    case R.id.nav_best_stories:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_BEST;
                        break;
                    case R.id.nav_show_hn:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SHOW;
                        break;
                    case R.id.nav_ask_hn:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_ASK;
                        break;
                    case R.id.nav_job:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_JOB;
                        break;
                    default:
                        navItemIndex = 0;
                }

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View d) {
                super.onDrawerClosed(d);
            }

            @Override
            public void onDrawerOpened(View d) {
                super.onDrawerOpened(d);
            }
        };

        //noinspection deprecation
        drawer.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_TOP;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFab() {
        if (navItemIndex == 0) {
            //fab.show();
        } else {
            //fab.hide();
        }
    }
}
