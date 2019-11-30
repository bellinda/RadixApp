package com.angelova.w510.radixapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.angelova.w510.radixapp.models.Invoice;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.DownloadPdfTask;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;

import java.io.File;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class InvoiceDetailsActivity extends AppCompatActivity {

    private com.github.barteksc.pdfviewer.PDFView mPdfView;
    private Toolbar mToolbar;
    private ProgressBar mLoader;

    private Invoice mInvoice;
    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mPdfView = (PDFView) findViewById(R.id.pdfView);
        mLoader = (ProgressBar) findViewById(R.id.loader);

        mInvoice = (Invoice) getIntent().getSerializableExtra("invoice");

        mProfile = getProfile();

        mToolbar.setTitle(String.format(getString(R.string.invoices_title), mInvoice.getConsecutiveID()));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolbar.inflateMenu(R.menu.invoice_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {

                } else if (item.getItemId() == R.id.action_download) {

                }
                return false;
            }
        });

        mPdfView.setVisibility(View.GONE);
        mLoader.setVisibility(View.VISIBLE);
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
        mLoader.setVisibility(View.GONE);
        mPdfView.setVisibility(View.VISIBLE);
        mPdfView.fromFile(file)
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
