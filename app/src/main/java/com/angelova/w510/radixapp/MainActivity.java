package com.angelova.w510.radixapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.fragments.AboutFragment;
import com.angelova.w510.radixapp.fragments.AllOffersFragment;
import com.angelova.w510.radixapp.fragments.AllOrdersFragment;
import com.angelova.w510.radixapp.fragments.InvoicesFragment;
import com.angelova.w510.radixapp.models.Invoice;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

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

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class MainActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private Profile mProfile;

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

        mProfile = getProfile();
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
                        getSupportActionBar().setTitle(R.string.profile_my_offers);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
                case R.id.navigation_invoices:
                    transaction.replace(R.id.content, new InvoicesFragment()).commit();
                    if(getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.fragment_invoices);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    }
                    return true;
            }
            return false;
        }

    };

    public void handleSuccessfulOrdersDownload(JSONArray receivedData) {
        List<Order> orders = new ArrayList<>();
        try {
            if (receivedData.length() > 0) {
                for (int i = 0; i < receivedData.length(); i++) {
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
                    for (int j = 0; j < files.length(); j++) {
                        fileNames.add(files.getString(j));
                    }
                    JSONArray existingFiles = data.getJSONArray("existingFiles");
                    for (int j = 0; j < existingFiles.length(); j++) {
                        fileNames.add(existingFiles.getString(j));
                    }
                    order.setAllFileNames(fileNames);
                    order.setPickUpMethod(data.getString("pickupMethod"));
                    order.setAnticipatedPrice(data.getString("anticipatedPrice"));
                    order.setAnticipatedPriceByAdmin(data.getString("anticipatedPriceByAdmin"));

                    if (data.has("expectedDeliveryDate") && !data.getString("expectedDeliveryDate").isEmpty()) {
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
                    order.setResponsesCount(data.getString("responsesCount"));
                    orders.add(order);
                }
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (currentFragment instanceof AllOrdersFragment) {
                ((AllOrdersFragment) currentFragment).handleOrdersLoaded(orders);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    public void handleErrorOnOrdersGet(String errorMessage) {
        showAlertDialogNow(errorMessage, "Warning");
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment instanceof AllOrdersFragment) {
            ((AllOrdersFragment) currentFragment).stopLoader();
        }
    }

    public void handleErrorOnOffersGet(String errorMessage) {
        showAlertDialogNow(errorMessage, "Warning");
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment instanceof AllOffersFragment) {
            ((AllOffersFragment) currentFragment).stopLoader();
        }
    }

    public void handleErrorOnInvoicesGet(String errorMessage) {
        showAlertDialogNow(errorMessage, "Warning");
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (currentFragment instanceof InvoicesFragment) {
            ((InvoicesFragment) currentFragment).stopLoader();
        }
    }

    public void handleSuccessfulOffersDownload(JSONArray receivedData) {
        List<Offer> offers = new ArrayList<>();
        try {
            for(int i = 0; i < receivedData.length(); i++) {
                JSONObject data = receivedData.getJSONObject(i);
                Offer offer = new Offer();
                offer.setId(data.getString("consecutiveID"));
                offer.setPhone(data.getString("phone"));
                offer.setEmail(data.getString("email"));
                offer.setOrderType(data.getString("orderType"));
                offer.setTranslationType(data.getString("translationType"));
                offer.setNotes(data.getString("notes"));
                offer.setFromLanguage(data.getString("fromLanguage"));
                offer.setToLanguage(data.getString("toLanguage"));
                offer.setName(data.getString("fullName"));

                String myFormat = "yyyy-MM-dd'T'HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date desiredDeliveryDateInUTC = sdf.parse(data.getString("desiredDeliveryDate"));
                    sdf.setTimeZone(Calendar.getInstance().getTimeZone());
                    offer.setDesiredDeliveryDate(sdf.format(desiredDeliveryDateInUTC));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

                offer.setGotResponse(data.getBoolean("gotResponse"));
                JSONArray files = data.getJSONArray("file");
                List<String> fileNames = new ArrayList<>();
                for(int j = 0; j < files.length(); j++) {
                    fileNames.add(files.getString(j));
                }
                offer.setFileNames(fileNames);
                String createdOn = data.getString("createdAt");
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
                targetFormat.setTimeZone(Calendar.getInstance().getTimeZone());
                try {
                    Date date = originalFormat.parse(createdOn);
                    String formattedDate = targetFormat.format(date);

                    offer.setCreatedOn(formattedDate);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                offer.setResponsesCount(data.getString("responsesCount"));
                offers.add(offer);
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (currentFragment instanceof AllOffersFragment) {
                ((AllOffersFragment) currentFragment).handleOffersLoaded(offers);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    public void handleSuccessfulInvoicesDownload(JSONObject response) {
        try {
            mProfile.setDeliveredInvoicesCount(response.getInt("deliveredInvoicesCount"));
            mProfile.setUnpaidInvoicesCount(response.getInt("unpaidInvoicesCount"));
            mProfile.setPaidInvoicesCount(response.getInt("paidInvoicesCount"));
            mProfile.setRequestedInvoicesCount(response.getInt("requestedInvoicesCount"));

            List<Invoice> invoices = new ArrayList<>();
            for (int i = 0; i < response.getJSONArray("invoices").length(); i++) {
                JSONObject responseItem = response.getJSONArray("invoices").getJSONObject(i);
                Invoice invoice = new Invoice();
                invoice.setPostedBy(responseItem.getString("postedBy"));
                invoice.setInvoiceCurrency(responseItem.getString("invoiceCurrency"));
                invoice.setInvoiceLanguage(responseItem.getString("invoiceLanguage"));
                invoice.setInvoicePaid(responseItem.getBoolean("invoicePaid"));
                invoice.setPartialPayment(responseItem.getBoolean("partialPayment"));
                invoice.setInvoicePaymentRejected(responseItem.getBoolean("invoicePaymentRejected"));
                invoice.setUploadedProofDocument(responseItem.getBoolean("uploadedProofDocument"));
                invoice.setHasUploadedTranslations(responseItem.getBoolean("hasUploadedTranslations"));
                invoice.setUserEmail(responseItem.getString("userEmail"));
                invoice.setInvoicedAmount(responseItem.getString("invoicedAmount"));
                invoice.setPayBefore(responseItem.getString("payBefore"));
                invoice.setInvoiceType(responseItem.getString("invoiceType"));
                invoice.setInvoiceTypeBG(responseItem.getString("invoiceTypeBG"));
                invoice.setAdminComment(responseItem.getString("adminComment"));
                invoice.setConsecutiveID(responseItem.getString("consecutiveID"));
                invoice.setOrderConsecutiveID(responseItem.getString("orderConsecutiveID"));
                invoice.setCreatedAt(responseItem.getString("createdAt"));
                invoice.setUpdatedAt(responseItem.getString("updatedAt"));
                List<String> files = new ArrayList<>();
                for (int j = 0; j < responseItem.getJSONArray("file").length(); j++) {
                    String file = responseItem.getJSONArray("file").getString(j);
                    files.add(file);
                }
                invoice.setFile(files);

                invoices.add(invoice);
            }

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.content);
            if (currentFragment instanceof InvoicesFragment) {
                ((InvoicesFragment) currentFragment).handleInvoicesLoaded(invoices);
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    private void showAlertDialogNow(String message, String title) {
        WarnDialog warning = new WarnDialog(this, title, message, new WarnDialog.DialogClickListener() {
            @Override
            public void onClick() {
            }
        });
        warning.show();
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
