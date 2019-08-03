package com.angelova.w510.radixapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
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
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    public void handleSuccessfulOrdersDownload(JSONArray receivedData) {
        List<Order> orders = new ArrayList<>();
        try {
            for(int i = 0; i < receivedData.length(); i++) {
                JSONObject data = receivedData.getJSONObject(i);
                Order order = new Order();
                order.setId(data.getString("consecutiveID"));
                order.setPhone(data.getString("phone"));
                order.setEmail(data.getString("email"));
                order.setOrderType(data.getString("orderType"));
                order.setTranslationType(data.getString("translationType"));
                order.setNotes(data.getString("notes"));
                order.setFromLanguage(data.getString("fromLanguage"));
                order.setToLanguage(data.getString("toLanguage"));
                order.setName(data.getString("fullName"));

                String myFormat = "yyyy-MM-dd'T'HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date desiredDeliveryDateInUTC = sdf.parse(data.getString("desiredDeliveryDate"));
                    sdf.setTimeZone(Calendar.getInstance().getTimeZone());
                    order.setDesiredDeliveryDate(sdf.format(desiredDeliveryDateInUTC));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                order.setGotResponse(data.getBoolean("gotResponse"));
                JSONArray files = data.getJSONArray("file");
                List<String> fileNames = new ArrayList<>();
                for(int j = 0; j < files.length(); j++) {
                    fileNames.add(files.getString(j));
                }
                JSONArray existingFiles = data.getJSONArray("existingFiles");
                for(int j = 0; j < existingFiles.length(); j++) {
                    fileNames.add(existingFiles.getString(j));
                }
                order.setAllFileNames(fileNames);
                order.setPickUpMethod(data.getString("pickupMethod"));
                order.setAnticipatedPrice(data.getString("anticipatedPrice"));
                order.setAnticipatedPriceByAdmin(data.getString("anticipatedPriceByAdmin"));

                if(data.has("expectedDeliveryDate") && !data.getString("expectedDeliveryDate").isEmpty()) {
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date expectedDeliveryDateInUTC = sdf.parse(data.getString("expectedDeliveryDate"));
                        sdf.setTimeZone(Calendar.getInstance().getTimeZone());
                        order.setExpectedDeliveryDate(sdf.format(expectedDeliveryDateInUTC));
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                }

                String createdOn = data.getString("createdAt");
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                targetFormat.setTimeZone(Calendar.getInstance().getTimeZone());
                try {
                    Date date = originalFormat.parse(createdOn);
                    String formattedDate = targetFormat.format(date);
                    order.setCreatedOn(formattedDate);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                orders.add(order);
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (currentFragment instanceof AllOrdersFragment) {
                ((AllOrdersFragment) currentFragment).handleOrdersLoaded(orders);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }
}
