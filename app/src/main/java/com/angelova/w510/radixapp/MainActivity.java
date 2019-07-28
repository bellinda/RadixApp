package com.angelova.w510.radixapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.angelova.w510.radixapp.list_fragments.AllOffersFragment;
import com.angelova.w510.radixapp.list_fragments.AllOrdersFragment;
import com.angelova.w510.radixapp.menu_items.ProfileActivity;
import com.angelova.w510.radixapp.menu_items.OfferActivity;
import com.angelova.w510.radixapp.menu_items.OrderActivity;
import com.angelova.w510.radixapp.menu_items.PricesActivity;

public class MainActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    private LinearLayout mOfferItem;
    private LinearLayout mOrderItem;
    private LinearLayout mPricesItem;
    private LinearLayout mProfileItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new AllOrdersFragment()).commit();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.profile_my_orders);
            //disable back button by passing false here:
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_orders:
                    transaction.replace(R.id.content, new AllOrdersFragment()).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.profile_my_orders);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_offers:
                    transaction.replace(R.id.content, new AllOffersFragment()).commit();
                    if(getSupportActionBar() != null) {
                        //getSupportActionBar().setTitle(R.string.title_documents);
                        getSupportActionBar().setTitle(R.string.profile_my_offers);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_prices:
//                    transaction.replace(R.id.content, new ArchiveFragment()).commit();
//                    if(getSupportActionBar() != null) {
//                        //getSupportActionBar().setTitle(R.string.title_reports);
//                        toolbar.setVisibility(View.VISIBLE);
//                        toolbarTitle.setText(R.string.title_archive);
//                        toolbarSearchBtn.setVisibility(View.VISIBLE);
//                        toolbarFilterBtn.setVisibility(View.VISIBLE);
//                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    }
                    return true;
            }
            return false;
        }

    };
}
