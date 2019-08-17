package com.angelova.w510.radixapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TermsAndConditionsDialog extends Dialog {

    private Context mActivity;
    private TextView mTextView;
    private TextView mCloseBtn;

    public TermsAndConditionsDialog(final Activity activity) {
        super(activity);
        this.mActivity = activity;
        this.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_terms_and_conditions);

        boolean isTablet = mActivity.getResources().getBoolean(R.bool.isTablet);
        if (!isTablet) {
            getWindow().setLayout((Utils.getWidth(mActivity) / 100) * 100, WindowManager.LayoutParams.MATCH_PARENT);
        }

        mTextView = (TextView) findViewById(R.id.text_view);
        mCloseBtn = (TextView) findViewById(R.id.close_btn);

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
