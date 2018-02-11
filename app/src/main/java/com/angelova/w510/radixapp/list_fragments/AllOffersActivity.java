package com.angelova.w510.radixapp.list_fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.widget.ListView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OffersAdapter;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

public class AllOffersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private ListView mListView;
    private FloatingActionButton mAddNewOfferBtn;
    private OffersAdapter mOffersAdapter;

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        initializeActivity();
    }

    private void initializeActivity() {
        mListView = (ListView) findViewById(R.id.listView);
        mAddNewOfferBtn = (FloatingActionButton) findViewById(R.id.add_new_offer_btn);
        mProfile = getProfile();

        mOffersAdapter = new OffersAdapter(this, mProfile.getOffers());
        mListView.setAdapter(mOffersAdapter);
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
}
