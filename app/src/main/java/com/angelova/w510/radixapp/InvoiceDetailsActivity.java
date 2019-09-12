package com.angelova.w510.radixapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.angelova.w510.radixapp.models.Invoice;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.DownloadPdfTask;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;

import java.io.File;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class InvoiceDetailsActivity extends AppCompatActivity {

    private Invoice mInvoice;
    private Profile mProfile;

    private com.github.barteksc.pdfviewer.PDFView mPdfViewTry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        mPdfViewTry = (PDFView) findViewById(R.id.pdfView);

        mInvoice = (Invoice) getIntent().getSerializableExtra("invoice");

        mProfile = getProfile();

        new DownloadPdfTask(InvoiceDetailsActivity.this, mInvoice.getFile().get(0), mInvoice.getConsecutiveID(), "/invoices/download", mProfile.getToken()).execute();
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

    public void handleFileDownloaded(File file) {
        mPdfViewTry.setVisibility(View.VISIBLE);
        mPdfViewTry.fromFile(file)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(5)
                .load();
    }
}
