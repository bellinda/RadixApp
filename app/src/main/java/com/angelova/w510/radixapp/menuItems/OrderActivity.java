package com.angelova.w510.radixapp.menuItems;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import info.hoang8f.android.segmented.SegmentedGroup;

public class OrderActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private RadioButton mHasOfferRb;
    private RadioButton mNotHaveOffer;
    private TextView mOfferIdLabel;
    private LinearLayout mOfferInputLayout;
    private EditText mOfferIdInput;
    private Button mSendOfferIdBtn;
    private RadioGroup mNameRg;
    private RadioButton mCurNameRb;
    private RadioButton mOtherNameRb;
    private EditText mNameInput;
    private RadioGroup mEmailRg;
    private RadioButton mCurEmailRb;
    private RadioButton mOtherEmailRb;
    private EditText mEmailInput;
    private SegmentedGroup mOrderTypeGroup;
    private RadioButton mExpressOrderRb;
    private RadioButton mNormalOrderRb;
    private TextView mDeliveryDateText;
    private ImageView mDeliveryDateBtn;
    private RadioGroup mDeliveryTypeRg;
    private RadioButton mDelFromOfficeRb;
    private RadioButton mDelOnEmailRb;
    private RadioButton mDelByPostRb;

    private Profile mProfile;

    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initializeActivity();
    }

    private void initializeActivity() {

        mHasOfferRb =(RadioButton) findViewById(R.id.have_offer);
        mNotHaveOffer = (RadioButton) findViewById(R.id.not_have_offer);
        mOfferIdLabel = (TextView) findViewById(R.id.offer_id_label);
        mOfferInputLayout = (LinearLayout) findViewById(R.id.offer_id_input_layout);
        mOfferIdInput = (EditText) findViewById(R.id.offer_id_input);
        mSendOfferIdBtn = (Button) findViewById(R.id.send_offer_id_btn);
        mNameRg = (RadioGroup) findViewById(R.id.radio_group_name);
        mCurNameRb = (RadioButton) findViewById(R.id.default_name);
        mOtherNameRb = (RadioButton) findViewById(R.id.other_name);
        mNameInput = (EditText) findViewById(R.id.name_input);
        mEmailRg = (RadioGroup) findViewById(R.id.radio_group_email);
        mCurEmailRb = (RadioButton) findViewById(R.id.default_email_rb);
        mOtherEmailRb = (RadioButton) findViewById(R.id.new_email_rb);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mOrderTypeGroup = (SegmentedGroup) findViewById(R.id.segm_gr_order_type);
        mExpressOrderRb = (RadioButton) findViewById(R.id.express_order);
        mNormalOrderRb = (RadioButton) findViewById(R.id.normal_order);
        mDeliveryDateText = (TextView) findViewById(R.id.delivery_date_text);
        mDeliveryDateBtn = (ImageView) findViewById(R.id.select_date_btn);
        mDeliveryTypeRg = (RadioGroup) findViewById(R.id.radio_group_delivery_type);
        mDelFromOfficeRb = (RadioButton) findViewById(R.id.from_office_rb);
        mDelOnEmailRb = (RadioButton) findViewById(R.id.on_email_rb);
        mDelByPostRb = (RadioButton) findViewById(R.id.by_courrier_rb);

        mProfile = getProfile();

        final Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_android);
        setSupportActionBar(myToolbar);

        final CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);
        ctl.setTitle("Make an order");
        ctl.setExpandedTitleColor(ContextCompat.getColor(OrderActivity.this, R.color.black_transparent));

        mCurNameRb.setText(mProfile.getName());

        mHasOfferRb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mOfferIdLabel.setVisibility(View.VISIBLE);
                    mOfferInputLayout.setVisibility(View.VISIBLE);
                } else {
                    mOfferIdLabel.setVisibility(View.GONE);
                    mOfferInputLayout.setVisibility(View.GONE);

                    mCurNameRb.setEnabled(true);
                    mOtherNameRb.setEnabled(true);

                    mOrderTypeGroup.setTintColor(ContextCompat.getColor(OrderActivity.this, R.color.menu_icons_color), Color.WHITE);
                    mExpressOrderRb.setEnabled(true);
                    mNormalOrderRb.setEnabled(true);
                }
            }
        });

        mSendOfferIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOfferIdInput.getText() == null || mOfferIdInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter the offer ID before submitting", "Warning");
                } else {
                    //TODO: send request to server to get order information by the provided code
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mOfferIdInput.getWindowToken(), 0);

                    mCurNameRb.setEnabled(false);
                    mOtherNameRb.setEnabled(false);

                    mOrderTypeGroup.setTintColor(Color.parseColor("#d2d2d2"));
                    mExpressOrderRb.setEnabled(false);
                    mExpressOrderRb.setTextColor(Color.GRAY);
                    mNormalOrderRb.setEnabled(false);
                    mNormalOrderRb.setTextColor(Color.GRAY);
                }
            }
        });

        mNameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.default_name) {
                    mNameInput.setTag(mNameInput.getKeyListener());
                    mNameInput.setKeyListener(null);
                    mNameInput.setVisibility(View.GONE);
                } else if (checkedId == R.id.other_name) {
                    mNameInput.setVisibility(View.VISIBLE);
                    mNameInput.setKeyListener((KeyListener) mNameInput.getTag());
                    mNameInput.setEnabled(true);
                }
            }
        });

        mNameInput.setTag(mNameInput.getKeyListener());
        mNameInput.setKeyListener(null);

        mEmailRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.default_email_rb) {
                    mEmailInput.setTag(mEmailInput.getKeyListener());
                    mEmailInput.setKeyListener(null);
                    mEmailInput.setVisibility(View.GONE);
                } else if (checkedId == R.id.new_email_rb) {
                    mEmailInput.setVisibility(View.VISIBLE);
                    mEmailInput.setKeyListener((KeyListener) mEmailInput.getTag());
                    mEmailInput.setEnabled(true);
                }
            }
        });

        mCurEmailRb.setText(mProfile.getEmail());

        mEmailInput.setTag(mEmailInput.getKeyListener());
        mEmailInput.setKeyListener(null);

        mDeliveryDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(OrderActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        updateDeliveryDate();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDeliveryDate();
        }

    };

    private void updateDeliveryDate() {
        String myFormat = "dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDeliveryDateText.setText(sdf.format(myCalendar.getTime()));
    }

    private Profile getProfile() {
        //get current profile from shared preferences (is available)
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
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