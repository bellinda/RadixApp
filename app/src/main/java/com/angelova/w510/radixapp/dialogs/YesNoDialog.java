package com.angelova.w510.radixapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;

/**
 * Created by W510 on 24.2.2018 г..
 */

public class YesNoDialog extends Dialog {

    private Activity activity;
    private TextView messageView;
    private TextView yesButton;
    private TextView noButton;
    private String message;
    private DialogClickListener listener;

    public interface DialogClickListener {
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();
    }

    public YesNoDialog(Activity activity, String message, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.message = message;
        this.listener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yes_no);

        messageView = (TextView) findViewById(R.id.message_view);
        yesButton = (TextView) findViewById(R.id.yes_button);
        noButton = (TextView) findViewById(R.id.no_button);

        messageView.setText(message);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null) {
                    listener.onPositiveButtonClicked();
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null) {
                    listener.onNegativeButtonClicked();
                }
            }
        });
    }
}

