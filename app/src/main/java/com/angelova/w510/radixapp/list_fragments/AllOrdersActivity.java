package com.angelova.w510.radixapp.list_fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

public class AllOrdersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);
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
