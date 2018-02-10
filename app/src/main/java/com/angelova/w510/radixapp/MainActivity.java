package com.angelova.w510.radixapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.angelova.w510.radixapp.menuItems.ProfileActivity;
import com.angelova.w510.radixapp.menuItems.OfferActivity;
import com.angelova.w510.radixapp.menuItems.OrderActivity;
import com.angelova.w510.radixapp.menuItems.PricesActivity;

public class MainActivity extends BaseActivity {

    private LinearLayout mOfferItem;
    private LinearLayout mOrderItem;
    private LinearLayout mPricesItem;
    private LinearLayout mProfileItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeActivity();
    }

    private void initializeActivity() {
        mOfferItem = (LinearLayout) findViewById(R.id.offer_item);
        mOrderItem = (LinearLayout) findViewById(R.id.order_item);
        mPricesItem = (LinearLayout) findViewById(R.id.prices_item);
        mProfileItem = (LinearLayout) findViewById(R.id.profile_item);

        mOfferItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OfferActivity.class);
                startActivity(intent);
            }
        });

        mOrderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        mPricesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PricesActivity.class);
                startActivity(intent);
            }
        });

        mProfileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }
}
