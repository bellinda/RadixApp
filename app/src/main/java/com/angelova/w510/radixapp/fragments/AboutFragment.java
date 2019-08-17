package com.angelova.w510.radixapp.fragments;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.dialogs.TermsAndConditionsDialog;

public class AboutFragment extends Fragment {

    private TextView mTermsAndConditionsView;
    private TextView mVersionText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        mTermsAndConditionsView = (TextView) rootView.findViewById(R.id.terms_and_conditions_view);
        mVersionText = (TextView) rootView.findViewById(R.id.version_text);

        mTermsAndConditionsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TermsAndConditionsDialog(getActivity()).show();
            }
        });

        mVersionText.setText(String.format(getString(R.string.fragment_about_version), getCurrentAppVersion()));

        return rootView;
    }

    private String getCurrentAppVersion() {
        PackageManager manager = getActivity().getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}
