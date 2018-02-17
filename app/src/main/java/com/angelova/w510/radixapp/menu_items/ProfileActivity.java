package com.angelova.w510.radixapp.menu_items;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.MainActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.list_fragments.AllOffersActivity;
import com.angelova.w510.radixapp.list_fragments.AllOrdersActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllOffersTask;
import com.angelova.w510.radixapp.tasks.GetAllOrdersTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends BaseActivity {

    private TextView mOffersCount;
    private TextView mOrdersCount;
    private TextView mProfileInitials;
    private LinearLayout mMyOffersItem;
    private LinearLayout mMyOrdersItem;
    private LinearLayout mEditProfileItem;

    private ProgressDialog loadingDialog;

    public static final String SHARED_PROFILE_KEY = "profile";

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeActivity();
    }

    private void initializeActivity() {
        mOffersCount = (TextView) findViewById(R.id.offers_count);
        mOrdersCount = (TextView) findViewById(R.id.orders_count);
        mProfileInitials = (TextView) findViewById(R.id.profile_initials);
        mMyOffersItem = (LinearLayout) findViewById(R.id.my_offers);
        mMyOrdersItem = (LinearLayout) findViewById(R.id.my_orders);
        mEditProfileItem = (LinearLayout) findViewById(R.id.edit_profile);

        loadingDialog = ProgressDialog.show(ProfileActivity.this, "",
                getString(R.string.loader_dialog_text), true);

        mProfile = getProfile();

        mProfileInitials.setText(getNameInitials(mProfile.getName()));

        mMyOffersItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AllOffersActivity.class);
                startActivity(intent);
            }
        });

        mMyOrdersItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, AllOrdersActivity.class);
                startActivity(intent);
            }
        });

        mEditProfileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        new GetAllOffersTask(ProfileActivity.this, "inquiries/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
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

    private void saveProfile(Profile profile) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appPreferences.edit();
        Gson gson = new Gson();

        String updatedJson = gson.toJson(profile);
        prefsEditor.putString(SHARED_PROFILE_KEY, updatedJson);
        prefsEditor.apply();
    }

    private String getNameInitials(String name) {
        String initials = "";
        String[] nameParts = name.split(" ");
        for(String part : nameParts) {
            initials += part.substring(0, 1);
        }
        return initials;
    }

    public void handleSuccessfulOffersDownload(JSONArray receivedData) {
        List<Offer> offers = new ArrayList<>();
        try {
            for(int i = 0; i < receivedData.length(); i++) {
                JSONObject data = receivedData.getJSONObject(i);
                Offer offer = new Offer();
                offer.setPhone(data.getString("phone"));
                offer.setEmail(data.getString("email"));
                offer.setOrderType(data.getString("orderType"));
                offer.setTranslationType(data.getString("translationType"));
                offer.setNotes(data.getString("notes"));
                offer.setFromLanguage(data.getString("fromLanguage"));
                offer.setToLanguage(data.getString("toLanguage"));
                offer.setName(data.getString("fullName"));
                offer.setDesiredDeliveryDate(data.getString("desiredDeliveryDate"));
                JSONArray files = data.getJSONArray("file");
                List<String> fileNames = new ArrayList<>();
                for(int j = 0; j < files.length(); j++) {
                    fileNames.add(files.getString(j));
                }
                offer.setFileNames(fileNames);
                String createdOn = data.getString("createdAt");
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                try {
                    Date date = originalFormat.parse(createdOn);
                    String formattedDate = targetFormat.format(date);

                    offer.setCreatedOn(formattedDate);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                offers.add(offer);
            }
            mProfile.setOffers(offers);
            saveProfile(mProfile);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }

        new GetAllOrdersTask(ProfileActivity.this, "orders/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
    }

    public void handleSuccessfulOrdersDownload(JSONArray receivedData) {
        List<Order> orders = new ArrayList<>();
        try {
            for(int i = 0; i < receivedData.length(); i++) {
                JSONObject data = receivedData.getJSONObject(i);
                Order order = new Order();
                order.setPhone(data.getString("phone"));
                order.setEmail(data.getString("email"));
                order.setOrderType(data.getString("orderType"));
                order.setTranslationType(data.getString("translationType"));
                order.setNotes(data.getString("notes"));
                order.setFromLanguage(data.getString("fromLanguage"));
                order.setToLanguage(data.getString("toLanguage"));
                order.setName(data.getString("fullName"));
                order.setDesiredDeliveryDate(data.getString("desiredDeliveryDate"));
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
                order.setExpectedDeliveryDate(data.getString("expectedDeliveryDate"));
                orders.add(order);
            }
            mProfile.setOrders(orders);
            saveProfile(mProfile);
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
        //everything is downloaded => show data + hide the loading message
        mOffersCount.setText(mProfile.getOffers().size() + "");
        mOrdersCount.setText(mProfile.getOrders().size() + "");
        loadingDialog.hide();
    }

    public void showErrorMessage(String message) {
        loadingDialog.hide();

        showAlertDialogNow(message, "Warning");
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
    }
}