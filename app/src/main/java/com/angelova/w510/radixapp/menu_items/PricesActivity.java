package com.angelova.w510.radixapp.menu_items;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;

import static android.view.View.GONE;

public class PricesActivity extends BaseActivity {

    private TextView mStandardPriceText;
    private TextView mSpecPriceText;
    private TextView mExpressPriceText;

    private LinearLayout mStandardPriceNoDiscount;
    private TextView mStandardPriceNoDiscountTextView;
    private LinearLayout mDiscountStandardPriceLayout;
    private LinearLayout mStandardPriceLayoutWithDiscount;
    private TextView mStandardPriceBeforeDiscountText;
    private TextView mStandardPriceAfterDiscountText;

    private LinearLayout mSpecPriceNoDiscount;
    private TextView mSpecPriceNoDiscountTextView;
    private LinearLayout mSpecPriceWithDiscount;
    private TextView mSpecPriceAfterDiscountText;
    private LinearLayout mSpecPriceLayoutWithDiscount;
    private TextView mSpecPriceBeforeDiscountText;

    private LinearLayout mExpPriceNoDiscount;
    private TextView mExpPriceNoDiscountTextView;
    private LinearLayout mExpPriceWithDiscount;
    private TextView mExpPriceAfterDiscountText;
    private LinearLayout mExpPriceLayoutWithDiscount;
    private TextView mExpPriceBeforeDiscountText;

    private static final boolean isStandardPriceWithDiscount = false;
    private static final boolean isSpecPriceWithDiscount = true;
    private static final boolean isExpPriceWithDiscount = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);

        initializeActiity();
    }

    private void initializeActiity() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.prices_toolbar);
        myToolbar.setTitle("Prices and Discounts");
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        mStandardPriceText = (TextView) findViewById(R.id.standard_price_text);
        mSpecPriceText = (TextView) findViewById(R.id.spec_price_text);
        mExpressPriceText = (TextView) findViewById(R.id.express_price_text);

        mStandardPriceNoDiscount = (LinearLayout) findViewById(R.id.standard_price_no_discount);
        mStandardPriceNoDiscountTextView = (TextView) findViewById(R.id.no_discount_standard_price_lower_layout);
        mDiscountStandardPriceLayout = (LinearLayout) findViewById(R.id.discount_standard_price_layout);
        mStandardPriceLayoutWithDiscount = (LinearLayout) findViewById(R.id.standard_price_layout_with_discount);
        mStandardPriceAfterDiscountText = (TextView) findViewById(R.id.standard_price_after_discount_text);
        mStandardPriceBeforeDiscountText = (TextView) findViewById(R.id.standard_price_before_discount_text);

        if(isStandardPriceWithDiscount) {
            mStandardPriceNoDiscount.setVisibility(GONE);
            mStandardPriceNoDiscountTextView.setVisibility(GONE);
            mDiscountStandardPriceLayout.setVisibility(View.VISIBLE);
            mStandardPriceLayoutWithDiscount.setVisibility(View.VISIBLE);
        } else {
            mDiscountStandardPriceLayout.setVisibility(GONE);
            mStandardPriceLayoutWithDiscount.setVisibility(GONE);
            mStandardPriceNoDiscount.setVisibility(View.VISIBLE);
            mStandardPriceNoDiscountTextView.setVisibility(View.VISIBLE);
        }
        //TODO: set dynamically the prices from server

        mSpecPriceNoDiscount = (LinearLayout) findViewById(R.id.spec_price_no_discount_layout);
        mSpecPriceNoDiscountTextView = (TextView) findViewById(R.id.no_discount_spec_price_lower_layout);
        mSpecPriceWithDiscount = (LinearLayout) findViewById(R.id.spec_price_layout_with_discount);
        mSpecPriceAfterDiscountText = (TextView) findViewById(R.id.spec_price_after_discount_text);
        mSpecPriceLayoutWithDiscount = (LinearLayout) findViewById(R.id.discount_spec_price_layout);
        mSpecPriceBeforeDiscountText = (TextView) findViewById(R.id.spec_price_before_discount_text);

        if(isSpecPriceWithDiscount) {
            mSpecPriceNoDiscount.setVisibility(GONE);
            mSpecPriceNoDiscountTextView.setVisibility(GONE);
            mSpecPriceWithDiscount.setVisibility(View.VISIBLE);
            mSpecPriceLayoutWithDiscount.setVisibility(View.VISIBLE);
        } else {
            mSpecPriceWithDiscount.setVisibility(View.GONE);
            mSpecPriceLayoutWithDiscount.setVisibility(View.GONE);
            mSpecPriceNoDiscount.setVisibility(View.VISIBLE);
            mSpecPriceNoDiscountTextView.setVisibility(View.VISIBLE);
        }

        //TODO: set dynamically the prices from server

        mExpPriceNoDiscount = (LinearLayout) findViewById(R.id.exp_price_no_discount_layout);
        mExpPriceNoDiscountTextView = (TextView) findViewById(R.id.no_discount_exp_price_lower_layout);
        mExpPriceWithDiscount = (LinearLayout) findViewById(R.id.exp_price_layout_with_discount);
        mExpPriceAfterDiscountText = (TextView) findViewById(R.id.exp_price_after_discount_text);
        mExpPriceLayoutWithDiscount = (LinearLayout) findViewById(R.id.discount_exp_price_layout);
        mExpPriceBeforeDiscountText = (TextView) findViewById(R.id.exp_price_before_discount_text);

        if(isExpPriceWithDiscount) {
            mExpPriceNoDiscount.setVisibility(GONE);
            mExpPriceNoDiscountTextView.setVisibility(GONE);
            mExpPriceWithDiscount.setVisibility(View.VISIBLE);
            mExpPriceLayoutWithDiscount.setVisibility(View.VISIBLE);
        } else {
            mExpPriceWithDiscount.setVisibility(View.GONE);
            mExpPriceLayoutWithDiscount.setVisibility(View.GONE);
            mExpPriceNoDiscount.setVisibility(View.VISIBLE);
            mExpPriceNoDiscountTextView.setVisibility(View.VISIBLE);
        }

        //TODO: set dynamically the prices from server
    }
}