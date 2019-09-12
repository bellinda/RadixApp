package com.angelova.w510.radixapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.InvoicesAdapter;
import com.angelova.w510.radixapp.models.Invoice;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllInvoicesTask;
import com.google.gson.Gson;

import java.util.List;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class InvoicesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mListView;
    private InvoicesAdapter mInvoicesAdapter;
    private TextView mNoInvoicesView;

    private Profile mProfile;
    private boolean isLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoices, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mListView = (RecyclerView) rootView.findViewById(R.id.listView);
        mNoInvoicesView = (TextView) rootView.findViewById(R.id.no_offers_view);

        mProfile = getProfile();
        if (mProfile.getInvoices() != null && mProfile.getInvoices().size() > 0) {
            mInvoicesAdapter = new InvoicesAdapter(getActivity(), mProfile.getInvoices(), mProfile.getOrders());
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mInvoicesAdapter);
        } else {
            mNoInvoicesView.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mSwipeRefreshLayout.setRefreshing(true);
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
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void handleInvoicesLoaded(List<Invoice> invoices) {
        mProfile.setInvoices(invoices);
        saveProfile(mProfile);

        if (invoices != null && invoices.size() > 0) {
            mInvoicesAdapter = new InvoicesAdapter(getActivity(), invoices, mProfile.getOrders());
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mInvoicesAdapter);
            mNoInvoicesView.setVisibility(View.GONE);
        } else {
            mNoInvoicesView.setVisibility(View.VISIBLE);
        }

//        List<Invoice> offersByType = getOrdersBasedOnSelectedType();
//        if (offersByType != null && offersByType.size() > 0) {
//            mOffersAdapter = new OffersAdapter(getActivity(), offersByType);
//            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//            mListView.setHasFixedSize(true);
//            mListView.setAdapter(mOffersAdapter);
//            mNoInvoicesView.setVisibility(View.GONE);
//        } else {
//            mNoInvoicesView.setVisibility(View.VISIBLE);
//        }
        isLoading = false;
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void saveProfile(Profile profile) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefsEditor = appPreferences.edit();
        Gson gson = new Gson();

        String updatedJson = gson.toJson(profile);
        prefsEditor.putString(SHARED_PROFILE_KEY, updatedJson);
        prefsEditor.apply();
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            isLoading = true;
            new GetAllInvoicesTask(getActivity(), "invoices", mProfile.getToken()).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoading) {
            mSwipeRefreshLayout.setRefreshing(true);
            isLoading = true;
            new GetAllInvoicesTask(getActivity(), "invoices", mProfile.getToken()).execute();
        }
    }
}
