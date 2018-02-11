package com.angelova.w510.radixapp.menuItems;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

public class ProfileActivity extends BaseActivity {

    private TextView mProfileInitials;
    private LinearLayout mMyOffersItem;
    private LinearLayout mMyOrdersItem;
    private LinearLayout mEditProfileItem;

    public static final String SHARED_PROFILE_KEY = "profile";

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeActivity();
    }

    private void initializeActivity() {
        mProfileInitials = (TextView) findViewById(R.id.profile_initials);
        mMyOffersItem = (LinearLayout) findViewById(R.id.my_offers);
        mMyOrdersItem = (LinearLayout) findViewById(R.id.my_orders);
        mEditProfileItem = (LinearLayout) findViewById(R.id.edit_profile);

        mProfile = getProfile();

        mProfileInitials.setText(getNameInitials(mProfile.getName()));

        mMyOffersItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mMyOrdersItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mEditProfileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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

    private String getNameInitials(String name) {
        String initials = "";
        String[] nameParts = name.split(" ");
        for(String part : nameParts) {
            initials += part.substring(0, 1);
        }
        return initials;
    }
}
