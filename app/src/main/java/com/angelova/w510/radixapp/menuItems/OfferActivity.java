package com.angelova.w510.radixapp.menuItems;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;

import java.util.ArrayList;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

public class OfferActivity extends BaseActivity {

    private static final int READ_REQUEST_CODE = 42;

    private LinearLayout mSelectFile;
    private TagGroup mSelectedFilesGroup;
    private TextView mSelectedFilesLabel;
    private Spinner mFromSpinner;
    private Spinner mToSpinner;

    private List<String> selectedFilesNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        initializeActivity();
    }

    private void initializeActivity() {
        mSelectFile = (LinearLayout) findViewById(R.id.select);
        mSelectedFilesGroup = (TagGroup) findViewById(R.id.tag_group);
        mSelectedFilesLabel = (TextView) findViewById(R.id.selected_files);
        mFromSpinner = (Spinner) findViewById(R.id.from_spinner);
        mToSpinner = (Spinner) findViewById(R.id.to_spinner);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_android);
        setSupportActionBar(myToolbar);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayoutAndroidExample);
        ctl.setTitle("Request an offer");

//        mSelectFile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
//                // browser.
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//
//                // Filter to only show results that can be "opened", such as a
//                // file (as opposed to a list of contacts or timezones)
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                // Filter to show only images, using the image MIME data type.
//                // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
//                // To search for all documents available via installed storage providers,
//                // it would be "*/*".
//                intent.setType("*/*");
//
//                startActivityForResult(intent, READ_REQUEST_CODE);
//            }
//        });
//
//        mSelectedFilesGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
//            @Override
//            public void onTagClick(String tag) {
//                selectedFilesNames.remove(tag);
//                mSelectedFilesGroup.setTags(selectedFilesNames);
//                if(selectedFilesNames.size() == 0) {
//                    mSelectedFilesLabel.setVisibility(View.GONE);
//                }
//            }
//        });
//
        ArrayAdapter<CharSequence> langAdapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_text, getResources().getStringArray(R.array.languages) );
        langAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
        mFromSpinner.setAdapter(langAdapter);

        mToSpinner.setAdapter(langAdapter);
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
}
