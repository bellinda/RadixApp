package com.angelova.w510.radixapp.menuItems;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.angelova.w510.radixapp.requests_utils.ServiceGenerator;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.services.FileUploadService;
import com.angelova.w510.radixapp.utils.FileUtils;
import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.gujun.android.taggroup.TagGroup;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferActivity extends BaseActivity {

    private static final int READ_REQUEST_CODE = 42;

    private static final int REQUEST_EXTERNAL_STORAGE = 2077;

    public static final String SHARED_PROFILE_KEY = "profile";

    private RadioGroup mNameRadioGroup;
    private RadioButton mCurrentNameRb;
    private RadioButton mOtherNameRb;
    private EditText mNameInput;
    private LinearLayout mSelectFiles;
    private TagGroup mSelectedFilesGroup;
    private TextView mSelectedFilesLabel;
    private RadioButton mExpressOrderRb;
    private RadioButton mNormalOrderRb;
    private RadioButton mSpecialTranslRb;
    private RadioButton mNonSpecialTranslRb;
    private Spinner mFromSpinner;
    private Spinner mToSpinner;
    private EditText mNotesInput;
    private RadioGroup mEmailRadioGroup;
    private RadioButton mCurrentEmailRb;
    private RadioButton mOtherEmailRb;
    private EditText mEmailInput;
    private EditText mPhoneInput;
    private Button mSubmitBtn;
    private ProgressBar mSubmitLoader;
    private TextView mDeliveryDateText;
    private ImageView mDeliveryDateBtn;
    private TextView mDeliveryTimeText;
    private ImageView mDeliveryTimeBtn;

    private Profile mProfile;

    private List<String> selectedFilesNames = new ArrayList<>();
    private List<Uri> selectedUris = new ArrayList<>();

    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        initializeActivity();
    }

    private void initializeActivity() {
        mNameRadioGroup = (RadioGroup) findViewById(R.id.radio_group_name);
        mCurrentNameRb = (RadioButton) findViewById(R.id.default_name);
        mOtherNameRb = (RadioButton) findViewById(R.id.other_name);
        mNameInput = (EditText) findViewById(R.id.name_input);
        mSelectFiles = (LinearLayout) findViewById(R.id.select_documents_layout);
        mSelectedFilesGroup = (TagGroup) findViewById(R.id.tag_group);
        mSelectedFilesLabel = (TextView) findViewById(R.id.selected_files);
        mExpressOrderRb = (RadioButton) findViewById(R.id.express_order);
        mNormalOrderRb = (RadioButton) findViewById(R.id.normal_order);
        mSpecialTranslRb = (RadioButton) findViewById(R.id.special_transl);
        mNonSpecialTranslRb = (RadioButton) findViewById(R.id.non_special_transl);
        mFromSpinner = (Spinner) findViewById(R.id.from_spinner);
        mToSpinner = (Spinner) findViewById(R.id.to_spinner);
        mNotesInput = (EditText) findViewById(R.id.notes_input);
        mEmailRadioGroup = (RadioGroup) findViewById(R.id.radio_group_email);
        mCurrentEmailRb = (RadioButton) findViewById(R.id.default_email_rb);
        mOtherEmailRb = (RadioButton) findViewById(R.id.new_email_rb);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mPhoneInput = (EditText) findViewById(R.id.phone_input);
        mSubmitBtn = (Button) findViewById(R.id.submit_btn);
        mSubmitLoader = (ProgressBar) findViewById(R.id.submit_loader);
        mDeliveryDateText = (TextView) findViewById(R.id.delivery_date_text);
        mDeliveryDateBtn = (ImageView) findViewById(R.id.select_date_btn);
        mDeliveryTimeText = (TextView) findViewById(R.id.delivery_time_text);
        mDeliveryTimeBtn = (ImageView) findViewById(R.id.select_time_btn);

        mProfile = getProfile();

        selectedFilesNames = new ArrayList<>();
        selectedUris = new ArrayList<>();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_android);
        setSupportActionBar(myToolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);
        ctl.setTitle("Request an offer");

        mCurrentNameRb.setText(mProfile.getName());

        mNameInput.setTag(mNameInput.getKeyListener());
        mNameInput.setKeyListener(null);

        mCurrentEmailRb.setText(mProfile.getEmail());

        mEmailInput.setTag(mEmailInput.getKeyListener());
        mEmailInput.setKeyListener(null);

        mNameRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.default_name) {
                    mNameInput.setTag(mNameInput.getKeyListener());
                    mNameInput.setKeyListener(null);
                } else if (checkedId == R.id.other_name) {
                    mNameInput.setKeyListener((KeyListener) mNameInput.getTag());
                    mNameInput.setEnabled(true);
                }
            }
        });

        mEmailRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.default_email_rb) {
                    mEmailInput.setTag(mEmailInput.getKeyListener());
                    mEmailInput.setKeyListener(null);
                } else if (checkedId == R.id.new_email_rb) {
                    mEmailInput.setKeyListener((KeyListener) mEmailInput.getTag());
                    mEmailInput.setEnabled(true);
                }
            }
        });

        mSelectFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(OfferActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(OfferActivity.this,
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
                mSelectedFilesGroup.setTags(selectedFilesNames);
                if(selectedFilesNames.size() == 0) {
                    mSelectedFilesLabel.setVisibility(View.GONE);
                }
            }
        });

        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, getResources().getStringArray(R.array.languages) );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        mFromSpinner.setAdapter(langAdapter);

        mToSpinner.setAdapter(langAdapter);

        mNotesInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.notes_input) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
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
                } else if (!mSpecialTranslRb.isChecked() && !mNonSpecialTranslRb.isChecked()) {
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
                    Offer offer = new Offer();
                    offer.setName(mOtherNameRb.isChecked() ? mNameInput.getText().toString() : mProfile.getName());
                    offer.setFromLanguage(mFromSpinner.getSelectedItem().toString());
                    offer.setToLanguage(mToSpinner.getSelectedItem().toString());
                    offer.setEmail(mOtherEmailRb.isChecked() ? mEmailInput.getText().toString() : mProfile.getEmail());
                    if(mNotesInput.getText() != null) {
                        offer.setNotes(mNotesInput.getText().toString());
                    }
                    offer.setOrderType(mExpressOrderRb.isChecked() ? "Express" : "Normal");
                    offer.setTranslationType(mSpecialTranslRb.isChecked() ? "Specialized" : "Non-specialized");
                    offer.setPhone(mPhoneInput.getText().toString());
                    String myFormat = "yyyy-MM-dd'T'HH:mm"; //In which you need put here
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    offer.setDesiredDeliveryDate(sdf.format(myCalendar.getTime()));
                    offer.setDocumentUris(selectedUris);

                    uploadOffer(offer);
                }
            }
        });

        mDeliveryDateBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(OfferActivity.this, dateSetListener, myCalendar
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
                new TimePickerDialog(OfferActivity.this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
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

            if(!selectedFilesNames.contains(displayName)) {
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
        super.onActivityResult(requestCode, resultCode, data);

    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

    private void uploadOffer(Offer offer) {
        // create upload service client
        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class, mProfile.getToken());

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
                        okhttp3.MultipartBody.FORM, offer.getName());

        RequestBody phone =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getPhone());

        RequestBody email =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getEmail());

        RequestBody orderType =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getOrderType());

        RequestBody translationType =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getTranslationType());

        RequestBody fromLanguage =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getFromLanguage());

        RequestBody toLanguage =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getToLanguage());

        RequestBody notes =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getNotes());

        RequestBody desiredDeliveryDate =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, offer.getDesiredDeliveryDate());

        params.put("fullName", fullName);
        params.put("phone", phone);
        params.put("email", email);
        params.put("orderType", orderType);
        params.put("translationType", translationType);
        params.put("fromLanguage", fromLanguage);
        params.put("toLanguage", toLanguage);
        params.put("notes", notes);
        params.put("desiredDeliveryDate", desiredDeliveryDate);

        //execute the request
        Call<ResponseBody> call = service.upload(params, files);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                Log.v("Upload", "success");
                mSubmitLoader.setVisibility(View.GONE);
                mSubmitBtn.setVisibility(View.VISIBLE);

                WarnDialog warning = new WarnDialog(OfferActivity.this, "Request an offer", "Your request is uploaded successfully!", new WarnDialog.DialogClickListener() {
                    @Override
                    public void onClick() {
                        Intent i = new Intent(OfferActivity.this, MainActivity.class);
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
}
