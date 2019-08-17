package com.angelova.w510.radixapp.details_activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.ResponsesAdapter;
import com.angelova.w510.radixapp.dialogs.ResponseDialog;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.menu_items.OrderActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Response;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetOfferResponsesTask;
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

public class OfferDetailsActivity extends AppCompatActivity {

    private Offer offer;

    private Toolbar mToolbar;
    private TextView mSentOn;
    private TextView mInquiryNumber;
    private TextView mFilesAmount;

    private TextView mInfoMenu;
    private TextView mDiscussionMenu;
    private View mInfoUnderline;
    private View mDiscussionUnderline;

    private TextView mToLang;
    private TextView mFromLang;

    private TextView mNoResponsesView;
    private FloatingActionMenu mFloatingMenu;
    private FloatingActionButton mSendResponseBtn;
    private FloatingActionButton mConvertBtn;

    private ScrollView mMainInfoLayout;
    private TextView mFullName;
    private TextView mOrderType;
    private TextView mTranslationType;
    private TextView mNotes;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mDesDelDate;
    private TextView mDocumentsList;
    private ListView mResponsesLayout;
    private ResponsesAdapter mResponsesAdapter;

    public static final String SHARED_PROFILE_KEY = "profile";

    private Profile mProfile;

    private ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        initializeActivity();
    }

    private void initializeActivity() {
        mLoadingDialog = ProgressDialog.show(OfferDetailsActivity.this, "",
                getString(R.string.loader_dialog_text), true);

        offer = (Offer) getIntent().getSerializableExtra("offer");

        mProfile = getProfile();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSentOn = (TextView) findViewById(R.id.sent_on);
        mInquiryNumber = (TextView) findViewById(R.id.inquiry_id);
        mFilesAmount = (TextView) findViewById(R.id.files_amount);

        mSentOn.setText(offer.getCreatedOn());
        mInquiryNumber.setText(offer.getId());
        mFilesAmount.setText(String.format("%d", offer.getFileNames().size()));

        mInfoMenu = (TextView) findViewById(R.id.offer_info_menu);
        mDiscussionMenu = (TextView) findViewById(R.id.discussions_menu);
        mInfoUnderline = findViewById(R.id.info_underline);
        mDiscussionUnderline = findViewById(R.id.discussions_underline);

        mDiscussionMenu.setText(String.format(Locale.US, "%s (%s)", getString(R.string.offer_details_discussion), offer.getResponsesCount()));
        mInfoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDiscussionMenu.setTextColor(getResources().getColor(R.color.black));
                mDiscussionUnderline.setVisibility(View.GONE);
                mInfoMenu.setTextColor(getResources().getColor(R.color.colorPrimary));
                mInfoUnderline.setVisibility(View.VISIBLE);

                mMainInfoLayout.setVisibility(View.VISIBLE);
                mResponsesLayout.setVisibility(View.GONE);
                mFloatingMenu.setVisibility(View.GONE);
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
                if(offer.getResponses() != null && offer.getResponses().size() > 0) {
                    mNoResponsesView.setVisibility(View.GONE);
                    mFloatingMenu.setVisibility(View.VISIBLE);
                } else {
                    mNoResponsesView.setVisibility(View.VISIBLE);
                    mFloatingMenu.setVisibility(View.GONE);
                }
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mToLang = (TextView) findViewById(R.id.to_language);
        mFromLang = (TextView) findViewById(R.id.from_language);
        mMainInfoLayout = (ScrollView) findViewById(R.id.main_info_layout);
        mFullName = (TextView) findViewById(R.id.full_name);
        mOrderType = (TextView) findViewById(R.id.order_type);
        mTranslationType = (TextView) findViewById(R.id.translation_type);
        mNotes = (TextView) findViewById(R.id.notes);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        mDesDelDate = (TextView) findViewById(R.id.des_del_date);
        mDocumentsList = (TextView) findViewById(R.id.documents);

        mResponsesLayout = (ListView) findViewById(R.id.responses_listview);

        mFromLang.setText(offer.getFromLanguage());
        mToLang.setText(offer.getToLanguage());

        mNoResponsesView = (TextView) findViewById(R.id.no_responses_view);
        mFloatingMenu = (FloatingActionMenu) findViewById(R.id.menu);
        mSendResponseBtn = (FloatingActionButton) findViewById(R.id.menu_item_send);
        mConvertBtn = (FloatingActionButton) findViewById(R.id.menu_item_convert);

        mSendResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseDialog responseDialog = new ResponseDialog(OfferDetailsActivity.this, new ResponseDialog.DialogClickListener() {
                    @Override
                    public void onSendButtonClicked(String comment) {
                        mLoadingDialog = ProgressDialog.show(OfferDetailsActivity.this, "",
                                getString(R.string.send_response_loading_dialog_text), true);

                        mFloatingMenu.close(true);

                        new SendResponseTask(OfferDetailsActivity.this, "inquiries/mobile/postResponses", offer.getId(), comment, mProfile.getToken()).execute();
                    }

                    @Override
                    public void onCancelButtonClicked() {

                    }
                });
                responseDialog.show();
            }
        });

        mConvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OfferDetailsActivity.this, OrderActivity.class);
                intent.putExtra("offerDetails", offer);
                startActivity(intent);
                finish();
            }
        });

        mFullName.setText(offer.getName());
        mOrderType.setText(offer.getOrderType());
        mTranslationType.setText(offer.getTranslationType());
        mNotes.setText(offer.getNotes());
        mEmail.setText(offer.getEmail());
        mPhone.setText(offer.getPhone());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy', 'HH:mm");
        try {
            Date desiredDeliveryDate = inputFormat.parse(offer.getDesiredDeliveryDate());
            String dateToBeShown = outputFormat.format(desiredDeliveryDate);
            mDesDelDate.setText(dateToBeShown);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        StringBuilder documentsListBuilder = new StringBuilder();
        for(String fileName : offer.getFileNames()) {
            documentsListBuilder.append(fileName);
            documentsListBuilder.append("\n");
        }
        documentsListBuilder.setLength(documentsListBuilder.length() - 1);
        mDocumentsList.setText(documentsListBuilder.toString());

        new GetOfferResponsesTask(OfferDetailsActivity.this, "inquiries/mobile/getResponses", offer.getId(), mProfile.getToken()).execute();
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

    private void saveProfile(Profile profile) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = appPreferences.edit();
        Gson gson = new Gson();

        String updatedJson = gson.toJson(profile);
        prefsEditor.putString(SHARED_PROFILE_KEY, updatedJson);
        prefsEditor.apply();
    }

    public void updateOfferDetails(JSONObject receivedResponses) {
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

            offer.setResponses(responses);

            mResponsesAdapter = new ResponsesAdapter(this, offer.getResponses());
            mResponsesLayout.setAdapter(mResponsesAdapter);

            mLoadingDialog.hide();
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    public void handleSuccessfulResponseUpload() {
        hideLoadingDialog();
        mLoadingDialog = ProgressDialog.show(OfferDetailsActivity.this, "",
                getString(R.string.loader_dialog_text), true);

        new GetOfferResponsesTask(OfferDetailsActivity.this, "inquiries/mobile/getResponses", offer.getId(), mProfile.getToken()).execute();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
