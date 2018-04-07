package com.angelova.w510.radixapp.details_activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.ResponsesAdapter;
import com.angelova.w510.radixapp.dialogs.ResponseDialog;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.models.Response;
import com.angelova.w510.radixapp.tasks.GetOrderResponsesTask;
import com.angelova.w510.radixapp.tasks.SendResponseTask;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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

    private TextView mTitleWithId;
    private TextView mToLang;
    private TextView mFromLang;
    private TextView mSentOn;
    private TextView mFilesCount;

    private TextView mInfoItem;
    private TextView mResponsesItem;
    //private Button mSendResponseBtn;
    private TextView mNoResponsesView;
    private FloatingActionMenu mFloatingMenu;
    private FloatingActionButton mSendResponseBtn;
    private FloatingActionButton mConfirmBtn;
    private FloatingActionButton mRejectBtn;

    private ScrollView mMainInfoLayout;
    private TextView mFullName;
    private TextView mOrderType;
    private TextView mTranslationType;
    private TextView mNotes;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mDesDelDate;
    private TextView mDocumentsList;
    private LinearLayout mExpDelDateLayout;
    private TextView mExpDelDate;
    private TextView mReceiving;
    private LinearLayout mAnticipatedPriceByAdminLayout;
    private TextView mAnticipatedPriceByAdmin;
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

        mTitleWithId = (TextView) findViewById(R.id.header_title);
        mToLang = (TextView) findViewById(R.id.to_lang);
        mFromLang = (TextView) findViewById(R.id.from_lang);
        mSentOn = (TextView) findViewById(R.id.sent_on);
        mFilesCount = (TextView) findViewById(R.id.files_count);

        mTitleWithId.setText(String.format(Locale.US, "Order %s", mOrder.getId()));
        mFromLang.setText(getLanguageAbbreviation(mOrder.getFromLanguage()));
        mToLang.setText(getLanguageAbbreviation(mOrder.getToLanguage()));
        mFilesCount.setText(String.format(Locale.US, "%d file(s)", mOrder.getAllFileNames().size()));
        mSentOn.setText(String.format(Locale.US, "Sent On: %s", mOrder.getCreatedOn()));

        mInfoItem = (TextView) findViewById(R.id.main_info_item);
        mResponsesItem = (TextView) findViewById(R.id.responses_item);
        mNoResponsesView = (TextView) findViewById(R.id.no_responses_view);
        mFloatingMenu = (FloatingActionMenu) findViewById(R.id.menu);
        mSendResponseBtn = (FloatingActionButton) findViewById(R.id.menu_item_send);
        mConfirmBtn = (FloatingActionButton) findViewById(R.id.menu_item_confirm);
        mRejectBtn = (FloatingActionButton) findViewById(R.id.menu_item_reject);

        mInfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.colorPrimary));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mMainInfoLayout.setVisibility(View.VISIBLE);
                mResponsesLayout.setVisibility(View.GONE);
                mFloatingMenu.setVisibility(View.GONE);
                mNoResponsesView.setVisibility(View.GONE);
            }
        });

        mResponsesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.colorPrimary));
                mMainInfoLayout.setVisibility(View.GONE);
                mResponsesLayout.setVisibility(View.VISIBLE);
                if(mOrder.getResponses() != null && mOrder.getResponses().size() > 0) {
                    mNoResponsesView.setVisibility(View.GONE);
                    mFloatingMenu.setVisibility(View.VISIBLE);
                } else {
                    mFloatingMenu.setVisibility(View.GONE);
                    mNoResponsesView.setVisibility(View.VISIBLE);
                }
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

                        mFloatingMenu.close(true);

                        new SendResponseTask(OrderDetailsActivity.this, "orders/mobile/postResponses", mOrder.getId(), comment, mProfile.getToken()).execute();
                    }

                    @Override
                    public void onCancelButtonClicked() {

                    }
                });
                responseDialog.show();
            }
        });

        mConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mMainInfoLayout = (ScrollView) findViewById(R.id.main_info_layout);
        mFullName = (TextView) findViewById(R.id.full_name);
        mOrderType = (TextView) findViewById(R.id.order_type);
        mTranslationType = (TextView) findViewById(R.id.translation_type);
        mNotes = (TextView) findViewById(R.id.notes);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        mDesDelDate = (TextView) findViewById(R.id.des_del_date);
        mDocumentsList = (TextView) findViewById(R.id.documents);
        mExpDelDateLayout = (LinearLayout) findViewById(R.id.exp_del_date_layout);
        mExpDelDate = (TextView) findViewById(R.id.exp_del_date);
        mReceiving = (TextView) findViewById(R.id.receiving);
        mAnticipatedPriceByAdminLayout = (LinearLayout) findViewById(R.id.anticipated_price_layout);
        mAnticipatedPriceByAdmin = (TextView) findViewById(R.id.anticipated_price);

        mResponsesLayout = (ListView) findViewById(R.id.responses_listview);

        mFullName.setText(mOrder.getName());
        mOrderType.setText(mOrder.getOrderType());
        mTranslationType.setText(mOrder.getTranslationType());
        mNotes.setText(mOrder.getNotes());
        mEmail.setText(mOrder.getEmail());
        mPhone.setText(mOrder.getPhone());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy', 'HH:mm");
        try {
            Date desiredDeliveryDate = inputFormat.parse(mOrder.getDesiredDeliveryDate());
            String dateToBeShown = outputFormat.format(desiredDeliveryDate);
            mDesDelDate.setText(dateToBeShown);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        StringBuilder documentsListBuilder = new StringBuilder();
        for(String fileName : mOrder.getAllFileNames()) {
            documentsListBuilder.append(fileName + "\n");
        }
        documentsListBuilder.delete(documentsListBuilder.lastIndexOf("\n"), documentsListBuilder.length() - 1);
        mDocumentsList.setText(documentsListBuilder.toString());
        if(mOrder.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(mOrder.getExpectedDeliveryDate())) {
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
            mReceiving.setText("From Office");
        } else if (mOrder.getPickUpMethod().equalsIgnoreCase("E")) {
            mReceiving.setText("On Email");
        } else {
            mReceiving.setText("By Post");
        }
        if(mOrder.getAnticipatedPriceByAdmin() != null && !TextUtils.isEmpty(mOrder.getAnticipatedPriceByAdmin())) {
            mAnticipatedPriceByAdmin.setText(String.format(Locale.US, "%s €", mOrder.getAnticipatedPriceByAdmin()));
        } else {
            mAnticipatedPriceByAdminLayout.setVisibility(View.GONE);
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

            mLoadingDialog.hide();
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
        mLoadingDialog.hide();
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
}
