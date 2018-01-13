package com.angelova.w510.radixapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.angelova.w510.radixapp.menuItems.AboutActivity;
import com.angelova.w510.radixapp.menuItems.LanguagesActivity;
import com.angelova.w510.radixapp.menuItems.OfferActivity;
import com.angelova.w510.radixapp.menuItems.OrderActivity;
import com.angelova.w510.radixapp.menuItems.PricesActivity;
import com.angelova.w510.radixapp.menuItems.PromotionsActivity;

public class MainActivity extends BaseActivity {

    private LinearLayout mOfferItem;
    private LinearLayout mOrderItem;
    private LinearLayout mPromotionsItem;
    private LinearLayout mPricesItem;
    private LinearLayout mLanguagesItem;
    private LinearLayout mAboutItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeActivity();
    }

    private void initializeActivity() {
        mOfferItem = (LinearLayout) findViewById(R.id.offer_item);
        mOrderItem = (LinearLayout) findViewById(R.id.order_item);
        mPromotionsItem = (LinearLayout) findViewById(R.id.promotions_item);
        mPricesItem = (LinearLayout) findViewById(R.id.prices_item);
        mLanguagesItem = (LinearLayout) findViewById(R.id.languages_item);
        mAboutItem = (LinearLayout) findViewById(R.id.about_item);

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

        mPromotionsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PromotionsActivity.class);
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

        mLanguagesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LanguagesActivity.class);
                startActivity(intent);
            }
        });

        mAboutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

    }
}
