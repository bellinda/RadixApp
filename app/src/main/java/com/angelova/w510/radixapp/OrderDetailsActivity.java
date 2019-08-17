package com.angelova.w510.radixapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.radixapp.adapters.ResponsesAdapter;
import com.angelova.w510.radixapp.dialogs.ResponseDialog;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.models.Response;
import com.angelova.w510.radixapp.tasks.GetOrderResponsesTask;
import com.angelova.w510.radixapp.tasks.SendResponseTask;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderDetailsActivity extends AppCompatActivity {

    private Order mOrder;

    public static final String SHARED_PROFILE_KEY = "profile";
    private Profile mProfile;
    private ProgressDialog mLoadingDialog;

    private Toolbar mToolbar;
    private TextView mSentOn;
    private TextView mOrderNumber;
    private TextView mTotalAmount;

    private TextView mInfoMenu;
    private TextView mDiscussionMenu;
    private View mInfoUnderline;
    private View mDiscussionUnderline;

    private TextView mNoResponsesView;
    private FloatingActionButton mSendResponseBtn;

    private ScrollView mMainInfoLayout;
    private TextView mFullName;
    private TextView mOrderType;
    private TextView mTranslationType;
    private TextView mNotes;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mDocumentsList;
    private LinearLayout mExpDelDateLayout;
    private TextView mExpDelDate;
    private TextView mReceiving;
    private TextView mFromLanguage;
    private TextView mToLanguage;
    private ListView mResponsesLayout;
    private ResponsesAdapter mResponsesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initializeActivity();
    }

    private void initializeActivity() {
        mLoadingDialog = ProgressDialog.show(OrderDetailsActivity.this, "",
                getString(R.string.loader_dialog_text), true);

        mOrder = (Order) getIntent().getSerializableExtra("order");

        mProfile = getProfile();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSentOn = (TextView) findViewById(R.id.sent_on);
        mOrderNumber = (TextView) findViewById(R.id.order_id);
        mTotalAmount = (TextView) findViewById(R.id.total_amount);

        mSentOn.setText(mOrder.getCreatedOn());
        mOrderNumber.setText(String.format(Locale.US, "#%s", mOrder.getId()));
        if(mOrder.getAnticipatedPriceByAdmin() != null && !TextUtils.isEmpty(mOrder.getAnticipatedPriceByAdmin().trim())) {
            mTotalAmount.setText(String.format(Locale.US, "%s €", mOrder.getAnticipatedPriceByAdmin()));
        } else {
            mTotalAmount.setText(getString(R.string.order_details_no_data_for_price));
        }

        mInfoMenu = (TextView) findViewById(R.id.order_info_menu);
        mDiscussionMenu = (TextView) findViewById(R.id.discussions_menu);
        mInfoUnderline = findViewById(R.id.info_underline);
        mDiscussionUnderline = findViewById(R.id.discussions_underline);

        mDiscussionMenu.setText(String.format(Locale.US, "%s (%s)", getString(R.string.order_details_discussion), mOrder.getResponsesCount()));
        mInfoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDiscussionMenu.setTextColor(getResources().getColor(R.color.black));
                mDiscussionUnderline.setVisibility(View.GONE);
                mInfoMenu.setTextColor(getResources().getColor(R.color.colorPrimary));
                mInfoUnderline.setVisibility(View.VISIBLE);

                mMainInfoLayout.setVisibility(View.VISIBLE);
                mResponsesLayout.setVisibility(View.GONE);
                mSendResponseBtn.setVisibility(View.GONE);
                mNoResponsesView.setVisibility(View.GONE);
            }
        });

        mDiscussionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInfoMenu.setTextColor(getResources().getColor(R.color.black));
                mInfoUnderline.setVisibility(View.GONE);
                mDiscussionMenu.setTextColor(getResources().getColor(R.color.colorPrimary));
                mDiscussionUnderline.setVisibility(View.VISIBLE);

                mMainInfoLayout.setVisibility(View.GONE);
                mResponsesLayout.setVisibility(View.VISIBLE);
                if(mOrder.getResponses() != null && mOrder.getResponses().size() > 0) {
                    mNoResponsesView.setVisibility(View.GONE);
                    mSendResponseBtn.setVisibility(View.VISIBLE);
                } else {
                    mSendResponseBtn.setVisibility(View.GONE);
                    mNoResponsesView.setVisibility(View.VISIBLE);
                }
            }
        });

        mNoResponsesView = (TextView) findViewById(R.id.no_responses_view);
        mSendResponseBtn = (FloatingActionButton) findViewById(R.id.menu_item_send);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mSendResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseDialog responseDialog = new ResponseDialog(OrderDetailsActivity.this, new ResponseDialog.DialogClickListener() {
                    @Override
                    public void onSendButtonClicked(String comment) {
                        mLoadingDialog = ProgressDialog.show(OrderDetailsActivity.this, "",
                                getString(R.string.send_response_loading_dialog_text), true);

                        new SendResponseTask(OrderDetailsActivity.this, "orders/mobile/postResponses", mOrder.getId(), comment, mProfile.getToken()).execute();
                    }

                    @Override
                    public void onCancelButtonClicked() {

                    }
                });
                responseDialog.show();
            }
        });

        mMainInfoLayout = (ScrollView) findViewById(R.id.main_info_layout);
        mFullName = (TextView) findViewById(R.id.full_name);
        mOrderType = (TextView) findViewById(R.id.order_type);
        mTranslationType = (TextView) findViewById(R.id.translation_type);
        mNotes = (TextView) findViewById(R.id.notes);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        mDocumentsList = (TextView) findViewById(R.id.documents);
        mExpDelDateLayout = (LinearLayout) findViewById(R.id.exp_del_date_layout);
        mExpDelDate = (TextView) findViewById(R.id.exp_del_date);
        mReceiving = (TextView) findViewById(R.id.receiving);
        mFromLanguage = (TextView) findViewById(R.id.from_language);
        mToLanguage = (TextView) findViewById(R.id.to_language);

        mResponsesLayout = (ListView) findViewById(R.id.responses_listview);

        mFullName.setText(mOrder.getName());
        mOrderType.setText(mOrder.getOrderType());
        mTranslationType.setText(mOrder.getTranslationType());
        mNotes.setText(mOrder.getNotes());
        mEmail.setText(mOrder.getEmail());
        mPhone.setText(mOrder.getPhone());
        mFromLanguage.setText(mOrder.getFromLanguage());
        mToLanguage.setText(mOrder.getToLanguage());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy', 'HH:mm");
        StringBuilder documentsListBuilder = new StringBuilder();
        for(String fileName : mOrder.getAllFileNames()) {
            documentsListBuilder.append(fileName + "\n");
        }
        documentsListBuilder.setLength(documentsListBuilder.length() - 1);
        mDocumentsList.setText(documentsListBuilder.toString());
        if (mOrder.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(mOrder.getExpectedDeliveryDate())) {
            try {
                Date expDeliveryDate = inputFormat.parse(mOrder.getExpectedDeliveryDate());
                String dateToBeShown = outputFormat.format(expDeliveryDate);
                mExpDelDate.setText(dateToBeShown);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else {
            mExpDelDateLayout.setVisibility(View.GONE);
        }
        if(mOrder.getPickUpMethod().equalsIgnoreCase("FO")) {
            mReceiving.setText(getString(R.string.order_delivery_from_office));
        } else if (mOrder.getPickUpMethod().equalsIgnoreCase("E")) {
            mReceiving.setText(getString(R.string.order_delivery_on_email));
        } else {
            mReceiving.setText(getString(R.string.order_delivery_by_post));
        }

        new GetOrderResponsesTask(OrderDetailsActivity.this, "orders/mobile/getResponses", mOrder.getId(), mProfile.getToken()).execute();
    }

    public void updateOrderDetails(JSONObject receivedResponses) {
        List<Response> responses = new ArrayList<>();
        try {
            JSONArray adminResponses = receivedResponses.getJSONArray("fromAdmin");
            for (int i = 0; i < adminResponses.length(); i++) {
                JSONObject respObj = adminResponses.getJSONObject(i);
                Response response = new Response();
                response.setQuantity(respObj.getString("quantity"));
                response.setExpectedDeliveryDate(respObj.getString("expectedDeliveryDate"));
                response.setUnitPrice(respObj.getString("unitPrice"));
                response.setAnticipatedPrice(respObj.getString("anticipatedPrice"));
                response.setCountPer(respObj.getString("countPer"));
                response.setComment(respObj.getString("comment"));
                response.setStatus(respObj.getString("status"));
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date date = originalFormat.parse(respObj.getString("createdAt"));
                    originalFormat.setTimeZone(Calendar.getInstance().getTimeZone());
                    response.setCreatedOn(originalFormat.format(date));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                response.setFromAdmin(true);

                responses.add(response);
            }

            JSONArray userResponses = receivedResponses.getJSONArray("fromUser");
            for (int i = 0; i < userResponses.length(); i++) {
                JSONObject respObj = userResponses.getJSONObject(i);
                Response response = new Response();
                response.setComment(respObj.getString("comment"));
                SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
                originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    Date date = originalFormat.parse(respObj.getString("createdAt"));
                    originalFormat.setTimeZone(Calendar.getInstance().getTimeZone());
                    response.setCreatedOn(originalFormat.format(date));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                response.setFromAdmin(false);

                responses.add(response);
            }

            Collections.sort(responses);

            mOrder.setOrderResponses(responses);

            mResponsesAdapter = new ResponsesAdapter(this, mOrder.getResponses());
            mResponsesLayout.setAdapter(mResponsesAdapter);

            if (mOrder.getResponses() != null && mOrder.getResponses().size() > 0) {
                mDiscussionMenu.setText(String.format(Locale.US, "%s (%s)", getString(R.string.order_details_discussion), mOrder.getResponses().size()));
            }

            mLoadingDialog.dismiss();
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    private String getLanguageAbbreviation(String language) {
        switch (language) {
            case "Bulgarian":
                return "BG";
            case "German":
                return "DE";
            case "French":
                return "FR";
            default:
                return "EN";
        }
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

    public void handleSuccessfulResponseUpload() {
        hideLoadingDialog();
        mLoadingDialog = ProgressDialog.show(OrderDetailsActivity.this, "",
                getString(R.string.loader_dialog_text), true);

        new GetOrderResponsesTask(OrderDetailsActivity.this, "orders/mobile/getResponses", mOrder.getId(), mProfile.getToken()).execute();
    }

    public void hideLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    public void showErrorMessage(String message) {
        hideLoadingDialog();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
