package com.angelova.w510.radixapp.details_activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Offer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OfferDetailsActivity extends AppCompatActivity {

    private Offer offer;

    private TextView mTitleWithId;
    private TextView mToLang;
    private TextView mFromLang;
    private TextView mSentOn;
    private TextView mFilesCount;

    private TextView mInfoItem;
    private TextView mResponsesItem;

    private ScrollView mMainInfoLayout;
    private TextView mFullName;
    private TextView mOrderType;
    private TextView mTranslationType;
    private TextView mNotes;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mDesDelDate;
    private TextView mDocumentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_details);

        initializeActivity();
    }

    private void initializeActivity() {
        offer = (Offer) getIntent().getSerializableExtra("offer");

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

        mInfoItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.colorPrimary));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mMainInfoLayout.setVisibility(View.VISIBLE);
            }
        });

        mResponsesItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.offer_details_not_active_item_color));
                mResponsesItem.setBackgroundColor(ContextCompat.getColor(OfferDetailsActivity.this, R.color.colorPrimary));
                mMainInfoLayout.setVisibility(View.GONE);
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
}
