package com.angelova.w510.radixapp.menu_items;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.MainActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.dialogs.WarnDialog;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.requests_utils.ServiceGenerator;
import com.angelova.w510.radixapp.services.OrderUploadService;
import com.angelova.w510.radixapp.tasks.GetOfferInfoTask;
import com.angelova.w510.radixapp.utils.FileUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import info.hoang8f.android.segmented.SegmentedGroup;
import me.gujun.android.taggroup.TagGroup;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private static final int REQUEST_EXTERNAL_STORAGE = 2077;

    private static final int READ_REQUEST_CODE = 42;

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
    private Spinner mFromSpinner;
    private TextView mFromLangLabel;
    private Spinner mToSpinner;
    private TextView mToLangLabel;
    private RadioGroup mEmailRg;
    private RadioButton mCurEmailRb;
    private RadioButton mOtherEmailRb;
    private EditText mEmailInput;
    private SegmentedGroup mOrderTypeGroup;
    private RadioButton mExpressOrderRb;
    private RadioButton mNormalOrderRb;
    private SegmentedGroup mTranslationTypeGroup;
    private RadioButton mSpecTransRb;
    private RadioButton mNonSpecTranRb;
    private EditText mNotesInput;
    private EditText mPhoneInput;
    private TextView mDeliveryDateText;
    private ImageView mDeliveryDateBtn;
    private TextView mDeliveryTimeText;
    private ImageView mDeliveryTimeBtn;
    private RadioGroup mDeliveryTypeRg;
    private RadioButton mDelFromOfficeRb;
    private RadioButton mDelOnEmailRb;
    private RadioButton mDelByPostRb;
    private LinearLayout mSelectFiles;
    private TagGroup mSelectedFilesGroup;
    private TextView mSelectedFilesLabel;
    private EditText mAnticipatedPrice;
    private Button mSubmitBtn;
    private ProgressBar mSubmitLoader;

    private Profile mProfile;

    private ProgressDialog loadingDialog;

    private final Calendar myCalendar = Calendar.getInstance();
    private ArrayAdapter<CharSequence> langAdapter;

    private List<String> selectedFilesNames = new ArrayList<>();
    private List<String> existingFileNames = new ArrayList<>();
    private List<Uri> selectedUris = new ArrayList<>();

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
        mFromSpinner = (Spinner) findViewById(R.id.from_spinner);
        mFromLangLabel = (TextView) findViewById(R.id.from_lang_label);
        mToSpinner = (Spinner) findViewById(R.id.to_spinner);
        mToLangLabel = (TextView) findViewById(R.id.to_lang_label);
        mEmailRg = (RadioGroup) findViewById(R.id.radio_group_email);
        mCurEmailRb = (RadioButton) findViewById(R.id.default_email_rb);
        mOtherEmailRb = (RadioButton) findViewById(R.id.new_email_rb);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mOrderTypeGroup = (SegmentedGroup) findViewById(R.id.segm_gr_order_type);
        mExpressOrderRb = (RadioButton) findViewById(R.id.express_order);
        mNormalOrderRb = (RadioButton) findViewById(R.id.normal_order);
        mTranslationTypeGroup = (SegmentedGroup) findViewById(R.id.segm_gr_transl_type);
        mSpecTransRb = (RadioButton) findViewById(R.id.special_transl);
        mNonSpecTranRb = (RadioButton) findViewById(R.id.non_special_transl);
        mNotesInput = (EditText) findViewById(R.id.notes_input);
        mPhoneInput = (EditText) findViewById(R.id.phone_input);
        mDeliveryDateText = (TextView) findViewById(R.id.delivery_date_text);
        mDeliveryDateBtn = (ImageView) findViewById(R.id.select_date_btn);
        mDeliveryTimeText = (TextView) findViewById(R.id.delivery_time_text);
        mDeliveryTimeBtn = (ImageView) findViewById(R.id.select_time_btn);
        mDeliveryTypeRg = (RadioGroup) findViewById(R.id.radio_group_delivery_type);
        mDelFromOfficeRb = (RadioButton) findViewById(R.id.from_office_rb);
        mDelOnEmailRb = (RadioButton) findViewById(R.id.on_email_rb);
        mDelByPostRb = (RadioButton) findViewById(R.id.by_courrier_rb);
        mSelectFiles = (LinearLayout) findViewById(R.id.select_documents_layout);
        mSelectedFilesGroup = (TagGroup) findViewById(R.id.tag_group);
        mSelectedFilesLabel = (TextView) findViewById(R.id.selected_files);
        mAnticipatedPrice = (EditText) findViewById(R.id.price_input);
        mSubmitBtn = (Button) findViewById(R.id.submit_btn);
        mSubmitLoader = (ProgressBar) findViewById(R.id.submit_loader);

        mProfile = getProfile();

        selectedFilesNames = new ArrayList<>();
        existingFileNames = new ArrayList<>();
        selectedUris = new ArrayList<>();

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
                    loadingDialog = ProgressDialog.show(OrderActivity.this, "",
                            getString(R.string.loader_dialog_text), true);

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mOfferIdInput.getWindowToken(), 0);

                    new GetOfferInfoTask(OrderActivity.this, "inquiries/mobile", mOfferIdInput.getText().toString(), mProfile.getUserId(), mProfile.getToken()).execute();
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

        langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, getResources().getStringArray(R.array.languages) );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        mFromSpinner.setAdapter(langAdapter);

        mToSpinner.setAdapter(langAdapter);

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

        myCalendar.set(Calendar.HOUR_OF_DAY, 9);
        myCalendar.set(Calendar.MINUTE, 0);

        mDeliveryTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(OrderActivity.this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

        mSelectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OrderActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
                } else {
                    getFilesFromStorage();
                }
            }
        });

        mSelectedFilesGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                selectedFilesNames.remove(tag);
                for(String fileName : existingFileNames) {
                    if(fileName.endsWith(tag)) {
                        existingFileNames.remove(fileName);
                        break;
                    }
                }
                mSelectedFilesGroup.setTags(selectedFilesNames);
                if(selectedFilesNames.size() == 0) {
                    mSelectedFilesLabel.setVisibility(View.GONE);
                }
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmitBtn.setVisibility(View.GONE);
                mSubmitLoader.setVisibility(View.VISIBLE);

                if(mOtherNameRb.isChecked() && (mNameInput.getText() == null || mNameInput.getText().toString().isEmpty())) {
                    showAlertDialogNow("Please enter the name you want to use for this offer", "Warning");
                } else if(!mExpressOrderRb.isChecked() && !mNormalOrderRb.isChecked()) {
                    showAlertDialogNow("Please select the type of your order", "Warning");
                } else if (!mSpecTransRb.isChecked() && !mNonSpecTranRb.isChecked()) {
                    showAlertDialogNow("Please select the type of your translation", "Warning");
                } else if(mOtherEmailRb.isChecked() && (mEmailInput.getText() == null || mEmailInput.getText().toString().isEmpty())) {
                    showAlertDialogNow("Please enter the email on which you want to get a response to your request", "Warning");
                } else if (mOtherEmailRb.isChecked() && mEmailInput.getText() != null && !mEmailInput.getText().toString().isEmpty() &&
                        !isValidEmail(mEmailInput.getText().toString())) {
                    showAlertDialogNow("Please enter a valid email address for getting the respnse of your request", "Warning");
                } else if (mPhoneInput.getText() == null || mPhoneInput.getText().toString().isEmpty()) {
                    showAlertDialogNow("Please enter your phone", "Warning");
                } else if (selectedFilesNames.size() == 0) {
                    showAlertDialogNow("Please select the files, for which translation you are requesting an offer", "Warning");
                } else {
                    Order order = new Order();
                    order.setName(mOtherNameRb.isChecked() ? mNameInput.getText().toString() : mProfile.getName());
                    order.setFromLanguage(mFromSpinner.getSelectedItem().toString());
                    order.setToLanguage(mToSpinner.getSelectedItem().toString());
                    order.setEmail(mOtherEmailRb.isChecked() ? mEmailInput.getText().toString() : mProfile.getEmail());
                    if(mNotesInput.getText() != null) {
                        order.setNotes(mNotesInput.getText().toString());
                    }
                    order.setOrderType(mExpressOrderRb.isChecked() ? "Express" : "Normal");
                    order.setTranslationType(mSpecTransRb.isChecked() ? "Specialized" : "Non-specialized");
                    order.setPhone(mPhoneInput.getText().toString());
                    String myFormat = "yyyy-MM-dd'T'HH:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                    order.setDesiredDeliveryDate(sdf.format(myCalendar.getTime()));
                    if(mDelFromOfficeRb.isChecked()) {
                        order.setPickUpMethod("FO");
                    } else if (mDelOnEmailRb.isChecked()) {
                        order.setPickUpMethod("E");
                    } else {
                        order.setPickUpMethod("P");
                    }
                    if(mAnticipatedPrice.getText() != null && !mAnticipatedPrice.getText().toString().isEmpty()) {
                        order.setAnticipatedPrice(mAnticipatedPrice.getText().toString());
                    }
                    if(mOfferIdInput.getText() != null && !mOfferIdInput.getText().toString().isEmpty()) {
                        order.setInquiryToDelete(mOfferIdInput.getText().toString());
                    }
                    order.setDocumentsFromOffer(existingFileNames);
                    order.setDocumentUris(selectedUris);

                    uploadOrder(order);
                }
            }
        });
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

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            myCalendar.set(Calendar.MINUTE, minute);
            updateDeliveryTime();
        }
    };

    private void updateDeliveryTime() {
        //String time = String.format("%d:%d", myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE));
        String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(myCalendar.getTime());
        mDeliveryTimeText.setText(time);
    }

    private void getFilesFromStorage() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("*/*");
        startActivityForResult(i, READ_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getFilesFromStorage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            Uri uri = data.getData();
            // do somthing...
            try {
                Cursor cursor = getContentResolver()
                        .query(data.getData(), null, null, null, null, null);
                String displayName = "";

                try {
                    // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
                    // "if there's anything to look at, look at it" conditionals.
                    if (cursor != null && cursor.moveToFirst()) {

                        // Note it's called "Display Name".  This is
                        // provider-specific, and might not necessarily be the file name.
                        displayName = cursor.getString(
                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }

                if (!selectedFilesNames.contains(displayName)) {
                    selectedFilesNames.add(displayName);
                    mSelectedFilesGroup.setTags(selectedFilesNames);
                    if (mSelectedFilesLabel.getVisibility() == View.GONE) {
                        mSelectedFilesLabel.setVisibility(View.VISIBLE);
                    }
                    selectedUris.add(uri);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showOfferDetails(JSONObject receivedData) {
        loadingDialog.hide();

        try {
//            mCurNameRb.setEnabled(false);
//            mOtherNameRb.setEnabled(false);
            String fullName = receivedData.getString("fullName");
            if(mCurNameRb.getText().toString().trim().equals(fullName.trim())) {
                mCurNameRb.setChecked(true);
            } else {
                mOtherNameRb.setChecked(true);
                mNameInput.setText(fullName);
            }

            String fromLanguage = receivedData.getString("fromLanguage");
            mFromSpinner.setSelection(langAdapter.getPosition(fromLanguage));
//            mFromSpinner.setEnabled(false);
//            mFromLangLabel.setTextColor(ContextCompat.getColor(this, R.color.inactive_text_color));

            String toLanguage = receivedData.getString("toLanguage");
            mToSpinner.setSelection(langAdapter.getPosition(toLanguage));
//            mToSpinner.setEnabled(false);
//            mToLangLabel.setTextColor(ContextCompat.getColor(this, R.color.inactive_text_color));

//            mOrderTypeGroup.setTintColor(Color.parseColor("#d2d2d2"));
//            mExpressOrderRb.setEnabled(false);
//            mExpressOrderRb.setTextColor(Color.GRAY);
//            mNormalOrderRb.setEnabled(false);
//            mNormalOrderRb.setTextColor(Color.GRAY);
            String orderType = receivedData.getString("orderType");
            if(orderType.equalsIgnoreCase(getString(R.string.offer_normal_order))) {
                mNormalOrderRb.setChecked(true);
            } else {
                mExpressOrderRb.setChecked(true);
            }

//            mTranslationTypeGroup.setTintColor(Color.parseColor("#d2d2d2"));
//            mSpecTransRb.setEnabled(false);
//            mSpecTransRb.setTextColor(Color.GRAY);
//            mNonSpecTranRb.setEnabled(false);
//            mNonSpecTranRb.setTextColor(Color.GRAY);
            String translationType = receivedData.getString("translationType");
            if(translationType.equalsIgnoreCase(getString(R.string.offer_translation_special))) {
                mSpecTransRb.setChecked(true);
            } else {
                mNonSpecTranRb.setChecked(true);
            }

            String notes = receivedData.getString("notes");
            mNotesInput.setText(notes);

            String email = receivedData.getString("email");
            if(mCurEmailRb.getText().toString().trim().equals(email.trim())) {
                mCurEmailRb.setChecked(true);
            } else {
                mOtherEmailRb.setChecked(true);
                mEmailInput.setText(email);
            }

            String phone = receivedData.getString("phone");
            mPhoneInput.setText(phone);

            String myFormat = "yyyy-MM-dd'T'HH:mm";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
            if(receivedData.has("expectedDeliveryDate") && !receivedData.getString("expectedDeliveryDate").isEmpty()) {
                String desiredDeliveryDate = receivedData.getString("expectedDeliveryDate");
                try {
                    Date desiredDeliveryDateAsDate = sdf.parse(desiredDeliveryDate);
                    myCalendar.setTime(desiredDeliveryDateAsDate);
                    updateDeliveryDate();
                    updateDeliveryTime();
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            }

            JSONArray fileNames = receivedData.getJSONArray("file");
            for (int i = 0; i < fileNames.length(); i++) {
                String fullFileName = fileNames.get(i).toString();
                //get name after second _
                String erasedFileName = fullFileName.substring(fullFileName.indexOf("_", fullFileName.indexOf("_") + 1) + 1);
                selectedFilesNames.add(erasedFileName);
                existingFileNames.add(fileNames.get(i).toString());
                mSelectedFilesGroup.setTags(selectedFilesNames);
                if (mSelectedFilesLabel.getVisibility() == View.GONE) {
                    mSelectedFilesLabel.setVisibility(View.VISIBLE);
                }
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
        }
    }

    private void uploadOrder(Order order) {
        // create upload service client
        OrderUploadService service =
                ServiceGenerator.createService(OrderUploadService.class, mProfile.getToken());

        // MultipartBody.Part is used to send also the actual file name
        List<MultipartBody.Part> files = new ArrayList<>();
        for(Uri uri : selectedUris) {
            // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
            // use the FileUtils to get the actual file by uri
            File file = FileUtils.getFile(this, uri);

            // create RequestBody instance from file
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse(getContentResolver().getType(uri)),
                            file
                    );
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("elements", file.getName(), requestFile);
            files.add(body);
        }


        // add another part within the multipart request
        Map<String, RequestBody> params = new HashMap<>();

        RequestBody fullName =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getName());

        RequestBody phone =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getPhone());

        RequestBody email =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getEmail());

        RequestBody orderType =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getOrderType());

        RequestBody translationType =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getTranslationType());

        RequestBody fromLanguage =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getFromLanguage());

        RequestBody toLanguage =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getToLanguage());

        RequestBody notes =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getNotes());

        RequestBody desiredDeliveryDate =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getDesiredDeliveryDate());

        RequestBody pickUpMethod =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, order.getPickUpMethod());

        if(order.getAnticipatedPrice() != null) {
            RequestBody anticipatedPrice =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, order.getAnticipatedPrice());

            params.put("anticipatedPrice", anticipatedPrice);
        }

        if(order.getInquiryToDelete() != null) {
            RequestBody inquiryToDelete =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, order.getInquiryToDelete());

            params.put("inquiryToDelete", inquiryToDelete);
        }

//        for(int i = 0; i < order.getDocumentsFromOffer().size(); i++) {
//            RequestBody existingFile =
//                    RequestBody.create(
//                            okhttp3.MultipartBody.FORM, order.getDocumentsFromOffer().get(i));
//            params.put("existingFiles[" + i + "]",existingFile);
//        }

        params.put("fullName", fullName);
        params.put("phone", phone);
        params.put("email", email);
        params.put("orderType", orderType);
        params.put("translationType", translationType);
        params.put("fromLanguage", fromLanguage);
        params.put("toLanguage", toLanguage);
        params.put("notes", notes);
        params.put("desiredDeliveryDate", desiredDeliveryDate);
        params.put("pickupMethod", pickUpMethod);

        //execute the request
        Call<ResponseBody> call = service.upload(params, order.getDocumentsFromOffer(), files);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                mSubmitLoader.setVisibility(View.GONE);
                mSubmitBtn.setVisibility(View.VISIBLE);

                WarnDialog warning = new WarnDialog(OrderActivity.this, "Submit an order",
                        "Your order is uploaded successfully! Please wait for more information about its status and price on email and in your profile", new WarnDialog.DialogClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(OrderActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                        // close this activity
                        finish();
                    }
                });
                warning.show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                mSubmitLoader.setVisibility(View.GONE);
                mSubmitBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    public void showErrorMessage(String errorMsg) {
        loadingDialog.hide();

        showAlertDialogNow(errorMsg, "Warning");
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

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}