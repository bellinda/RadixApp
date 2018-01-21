package com.angelova.w510.radixapp.menuItems;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.KeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Document;
import com.angelova.w510.radixapp.models.Offer;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class OfferActivity extends BaseActivity {

    private static final int READ_REQUEST_CODE = 42;

    private RadioGroup mNameRadioGroup;
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
    private RadioButton mOtherEmailRb;
    private EditText mEmailInput;
    private Button mSubmitBtn;

    private List<String> selectedFilesNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        initializeActivity();
    }

    private void initializeActivity() {
        mNameRadioGroup = (RadioGroup) findViewById(R.id.radio_group_name);
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
        mOtherEmailRb = (RadioButton) findViewById(R.id.new_email_rb);
        mEmailInput = (EditText) findViewById(R.id.email_input);
        mSubmitBtn = (Button) findViewById(R.id.submit_btn);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_android);
        setSupportActionBar(myToolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);
        ctl.setTitle("Request an offer");

        mNameInput.setTag(mNameInput.getKeyListener());
        mNameInput.setKeyListener(null);

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
                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                // browser.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                // Filter to only show results that can be "opened", such as a
                // file (as opposed to a list of contacts or timezones)
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                // Filter to show only images, using the image MIME data type.
                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                // To search for all documents available via installed storage providers,
                // it would be "*/*".
                intent.setType("*/*");

                startActivityForResult(intent, READ_REQUEST_CODE);
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
                } else if (selectedFilesNames.size() == 0) {
                    showAlertDialogNow("Please select the files, for which translation you are requesting an offer", "Warning");
                } else {
                    Offer offer = new Offer();
                    //TODO: get name from profile
                    offer.setName(mOtherNameRb.isChecked() ? mNameInput.getText().toString() : "Someone else");
                    offer.setFromLanguage(mFromSpinner.getSelectedItem().toString());
                    offer.setToLanguage(mToSpinner.getSelectedItem().toString());
                    //TODO: get email from profile
                    offer.setEmail(mOtherEmailRb.isChecked() ? mEmailInput.getText().toString() : "someone.else@gmail.com");
                    if(mNotesInput.getText() != null) {
                        offer.setNotes(mNotesInput.getText().toString());
                    }
                    offer.setOrderType(mExpressOrderRb.isChecked() ? "Express" : "Normal");
                    offer.setTranslationType(mSpecialTranslRb.isChecked() ? "Specialized" : "Non-specialized");
                    List<Document> documents = new ArrayList<>();
                    for(String fileName : selectedFilesNames) {
                        Document currDocument = new Document();
                        currDocument.setName(fileName);
                        documents.add(currDocument);
                    }
                    offer.setDocuments(documents);

                    //TODO:submit to server
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                System.out.println("URI: " + uri.toString());
//                showImage(uri);
                dumpImageMetaData(uri);
            }
        }
    }

    public void dumpImageMetaData(Uri uri) {

        // The query, since it only applies to a single document, will only return
        // one row. There's no need to filter, sort, or select fields, since we want
        // all fields for one document.
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null, null);

        try {
            // moveToFirst() returns false if the cursor has 0 rows.  Very handy for
            // "if there's anything to look at, look at it" conditionals.
            if (cursor != null && cursor.moveToFirst()) {

                // Note it's called "Display Name".  This is
                // provider-specific, and might not necessarily be the file name.
                String displayName = cursor.getString(
                        cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                System.out.println("Display Name: " + displayName);

                if(!selectedFilesNames.contains(displayName)) {
                    selectedFilesNames.add(displayName);
                    mSelectedFilesGroup.setTags(selectedFilesNames);
                    if (mSelectedFilesLabel.getVisibility() == View.GONE) {
                        mSelectedFilesLabel.setVisibility(View.VISIBLE);
                    }
                }

                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                // If the size is unknown, the value stored is null.  But since an
                // int can't be null in Java, the behavior is implementation-specific,
                // which is just a fancy term for "unpredictable".  So as
                // a rule, check if it's null before assigning to an int.  This will
                // happen often:  The storage API allows for remote files, whose
                // size might not be locally known.
                String size = null;
                if (!cursor.isNull(sizeIndex)) {
                    // Technically the column stores an int, but cursor.getString()
                    // will do the conversion automatically.
                    size = cursor.getString(sizeIndex);
                } else {
                    size = "Unknown";
                }
                System.out.println("Size: " + size);
            }
        } finally {
            cursor.close();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void showAlertDialogNow(String message, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OfferActivity.this);
        builder.setMessage(message).setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
