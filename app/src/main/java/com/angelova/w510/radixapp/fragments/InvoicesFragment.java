package com.angelova.w510.radixapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllInvoicesTask;
import com.google.gson.Gson;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class InvoicesFragment extends Fragment {

    private Profile mProfile;
    private boolean isLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoices, container, false);

        mProfile = getProfile();

        new GetAllInvoicesTask(getActivity(), "invoices", mProfile.getToken()).execute();

        return rootView;
    }

    private Profile getProfile() {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
    }

    public void stopLoader() {
        isLoading = false;
        //mSwipeRefreshLayout.setRefreshing(false);
    }
}
