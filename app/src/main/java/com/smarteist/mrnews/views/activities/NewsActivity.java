package com.smarteist.mrnews.views.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.smarteist.mrnews.BaseActivity;
import com.smarteist.mrnews.R;
import com.smarteist.mrnews.util.ActivityUtils;
import com.smarteist.mrnews.util.Constants;
import com.smarteist.mrnews.views.fragments.NewsFragment;
import com.smarteist.mrnews.views.fragments.NewsPresenter;

import javax.inject.Inject;

/**
 * Lightweight Activity in which NewsFragment emerges from
 * In this case we don't need presenter and contracts for this
 * activity.
 */

public class NewsActivity extends BaseActivity {

    @Inject
    NewsPresenter mNewsPresenter;

    @Inject
    NewsFragment injectedFragment;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setUpActionBar(toolbar);

        // Set up the navigation drawer.
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Set up fragment
        NewsFragment fragment = (NewsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = injectedFragment;
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setCheckedItem(R.id.top_navigation_menu_item);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.top_navigation_menu_item:
                                mNewsPresenter.loadNews(Constants.NEWS_CATEGORY_LATEST);
                                break;
                            case R.id.tech_navigation_menu_item:
                                mNewsPresenter.loadNews(Constants.NEWS_CATEGORY_TECHNOLOGY);
                                break;
                            case R.id.business_navigation_menu_item:
                                mNewsPresenter.loadNews(Constants.NEWS_CATEGORY_BUSINESS);
                                break;
                            case R.id.saved_navigation_menu_item:
                                mNewsPresenter.loadSavedNews();
                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
