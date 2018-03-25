package com.angelova.w510.radixapp.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;

/**
 * Created by W510 on 25.3.2018 г..
 */

public class ResponseDialog extends Dialog {

    private Activity activity;
    private EditText messageView;
    private TextView sendButton;
    private TextView cancelButton;

    private DialogClickListener listener;

    public interface DialogClickListener {
        void onSendButtonClicked(String comment);
        void onCancelButtonClicked();
    }

    public ResponseDialog(Activity activity, DialogClickListener onClickListener) {
        super(activity);
        this.activity = activity;
        this.listener = onClickListener;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_response);

        messageView = (EditText) findViewById(R.id.message_view);
        sendButton = (TextView) findViewById(R.id.send_button);
        cancelButton = (TextView) findViewById(R.id.cancel_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageView.getText() != null && !messageView.getText().toString().isEmpty()) {
                    dismiss();
                    if (listener != null) {
                        listener.onSendButtonClicked(messageView.getText().toString());
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(listener != null) {
                    listener.onCancelButtonClicked();
                }
            }
        });
    }
}
