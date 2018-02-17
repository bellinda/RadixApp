package com.angelova.w510.radixapp.list_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.MainActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OffersAdapter;
import com.angelova.w510.radixapp.menu_items.OfferActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

public class AllOffersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private ListView mListView;
    private FloatingActionButton mAddNewOfferBtn;
    private OffersAdapter mOffersAdapter;
    private TextView mPendingOffersCount;
    private TextView mOffersWithResponseCount;
    private CoordinatorLayout mMainContentLayout;

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        initializeActivity();
    }

    private void initializeActivity() {
        mMainContentLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mListView = (ListView) findViewById(R.id.listView);
        mAddNewOfferBtn = (FloatingActionButton) findViewById(R.id.add_new_offer_btn);
        mPendingOffersCount = (TextView) findViewById(R.id.pending_offers_count);
        mOffersWithResponseCount = (TextView) findViewById(R.id.answered_offers_count);
        mProfile = getProfile();

        mOffersAdapter = new OffersAdapter(this, mProfile.getOffers());
        mListView.setAdapter(mOffersAdapter);

        mAddNewOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOffersActivity.this, OfferActivity.class);
                startActivity(intent);
                finish();
//                Snackbar snackbar = Snackbar
//                        .make(mMainContentLayout, "Clicked on the floating button", Snackbar.LENGTH_LONG);
//                snackbar.show();
            }
        });

        mPendingOffersCount.setText(getPendingOffersCount() + "");
        mOffersWithResponseCount.setText(getOffersWithResponseCount() + "");
    }

    private Profile getProfile() {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
    }

    private int getPendingOffersCount() {
        int pendingOffersCount = 0;
        for(Offer offer : mProfile.getOffers()) {
            if(offer.getOfferResponse() == null) {
                pendingOffersCount++;
            }
        }

        return pendingOffersCount;
    }

    private int getOffersWithResponseCount() {
        int offersWithResponseCount = 0;
        for(Offer offer : mProfile.getOffers()) {
            if(offer.getOfferResponse() != null) {
                offersWithResponseCount++;
            }
        }
        return offersWithResponseCount;
    }
}
