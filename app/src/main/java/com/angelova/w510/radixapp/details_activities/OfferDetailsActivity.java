package com.angelova.w510.radixapp.details_activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.ResponsesAdapter;
import com.angelova.w510.radixapp.dialogs.ResponseDialog;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.OfferResponse;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetOfferResponsesTask;
import com.angelova.w510.radixapp.tasks.SendResponseTask;
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

public class OfferDetailsActivity extends AppCompatActivity {

    private Offer offer;

    private TextView mTitleWithId;
    private TextView mToLang;
    private TextView mFromLang;
    private TextView mSentOn;
    private TextView mFilesCount;

    private TextView mInfoItem;
    private TextView mResponsesItem;
    private Button mSendResponseBtn;
    private TextView mNoResponsesView;

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

        mTitleWithId = (TextView) findViewById(R.id.header_title);
        mToLang = (TextView) findViewById(R.id.to_lang);
        mFromLang = (TextView) findViewById(R.id.from_lang);
        mSentOn = (TextView) findViewById(R.id.sent_on);
        mFilesCount = (TextView) findViewById(R.id.files_count);

        mTitleWithId.setText(String.format(Locale.US, "Offer %s", offer.getId()));
        mFromLang.setText(getLanguageAbbreviation(offer.getFromLanguage()));
        mToLang.setText(getLanguageAbbreviation(offer.getToLanguage()));
        mFilesCount.setText(String.format(Locale.US, "%d file(s)", offer.getFileNames().size()));
        mSentOn.setText(String.format(Locale.US, "Sent On: %s", offer.getCreatedOn()));

        mInfoItem = (TextView) findViewById(R.id.main_info_item);
        mResponsesItem = (TextView) findViewById(R.id.responses_item);
        mSendResponseBtn = (Button) findViewById(R.id.add_new_response_btn);
        mNoResponsesView = (TextView) findViewById(R.id.no_responses_view);

        mInfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.colorPrimary));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mMainInfoLayout.setVisibility(View.VISIBLE);
                mResponsesLayout.setVisibility(View.GONE);
                mSendResponseBtn.setVisibility(View.GONE);
            }
        });

        mResponsesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.colorPrimary));
                mMainInfoLayout.setVisibility(View.GONE);
                mResponsesLayout.setVisibility(View.VISIBLE);
                if(offer.getOfferResponses() != null && offer.getOfferResponses().size() > 0) {
                    mNoResponsesView.setVisibility(View.GONE);
                    mSendResponseBtn.setVisibility(View.VISIBLE);
                } else {
                    mSendResponseBtn.setVisibility(View.GONE);
                    mNoResponsesView.setVisibility(View.VISIBLE);
                }
            }
        });

        mSendResponseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResponseDialog responseDialog = new ResponseDialog(OfferDetailsActivity.this, new ResponseDialog.DialogClickListener() {
                    @Override
                    public void onSendButtonClicked(String comment) {
                        mLoadingDialog = ProgressDialog.show(OfferDetailsActivity.this, "",
                                getString(R.string.send_response_loading_dialog_text), true);

                        new SendResponseTask(OfferDetailsActivity.this, "inquiries/mobile/postResponses", offer.getId(), comment, mProfile.getToken()).execute();
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
        mDesDelDate = (TextView) findViewById(R.id.des_del_date);
        mDocumentsList = (TextView) findViewById(R.id.documents);

        mResponsesLayout = (ListView) findViewById(R.id.responses_listview);

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
            documentsListBuilder.append(fileName + "\n");
        }
        documentsListBuilder.delete(documentsListBuilder.lastIndexOf("\n"), documentsListBuilder.length() - 1);
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
        List<OfferResponse> responses = new ArrayList<>();
        try {
            JSONArray adminResponses = receivedResponses.getJSONArray("fromAdmin");
            for (int i = 0; i < adminResponses.length(); i++) {
                JSONObject respObj = adminResponses.getJSONObject(i);
                OfferResponse response = new OfferResponse();
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
                OfferResponse response = new OfferResponse();
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

            offer.setOfferResponses(responses);

            mResponsesAdapter = new ResponsesAdapter(this, offer.getOfferResponses());
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
}
