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

import java.util.ArrayList;
import java.util.List;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class InvoicesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mListView;
    private InvoicesAdapter mInvoicesAdapter;
    private TextView mNoInvoicesView;

    private Profile mProfile;
    private boolean isLoading = true;

    private TextView mAllTitle;
    private TextView mUnpaidTitle;
    private TextView mPaidTitle;
    private TextView mRejectedTitle;

    private boolean isAllSelected = true;
    private boolean isUnpaidSelected = false;
    private boolean isPaidSelected = false;
    private boolean isRejectedSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_invoices, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mListView = (RecyclerView) rootView.findViewById(R.id.listView);
        mNoInvoicesView = (TextView) rootView.findViewById(R.id.no_offers_view);

        mAllTitle = (TextView) rootView.findViewById(R.id.all_title);
        mUnpaidTitle = (TextView) rootView.findViewById(R.id.unpaid_title);
        mPaidTitle = (TextView) rootView.findViewById(R.id.paid_title);
        mRejectedTitle = (TextView) rootView.findViewById(R.id.rejected_title);

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

        mAllTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    if (getProfile().getInvoices() != null && getProfile().getInvoices().size() > 0) {
                        mNoInvoicesView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mInvoicesAdapter = new InvoicesAdapter(getActivity(), getProfile().getInvoices(), getProfile().getOrders());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mInvoicesAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoInvoicesView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mAllTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isAllSelected = true;
                }
            }
        });

        mUnpaidTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Invoice> unpaidInvoices = getUnpaidInvoices();
                    if (unpaidInvoices.size() > 0) {
                        mNoInvoicesView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mInvoicesAdapter = new InvoicesAdapter(getActivity(), unpaidInvoices, getProfile().getOrders());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mInvoicesAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoInvoicesView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mUnpaidTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mUnpaidTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isUnpaidSelected = true;
                }
            }
        });

        mPaidTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Invoice> paidInvoices = getPaidInvoices();
                    if (paidInvoices.size() > 0) {
                        mNoInvoicesView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mInvoicesAdapter = new InvoicesAdapter(getActivity(), paidInvoices, getProfile().getOrders());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mInvoicesAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoInvoicesView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mPaidTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mPaidTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isPaidSelected = true;
                }
            }
        });

        mRejectedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Invoice> rejectedInvoices = getRejectedInvoices();
                    if (rejectedInvoices.size() > 0) {
                        mNoInvoicesView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mInvoicesAdapter = new InvoicesAdapter(getActivity(), rejectedInvoices, getProfile().getOrders());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mInvoicesAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoInvoicesView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mRejectedTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mRejectedTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isRejectedSelected = true;
                }
            }
        });

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

        List<Invoice> invoicesByType = getInvoicesBasedOnSelectedType();
        if (invoicesByType != null && invoicesByType.size() > 0) {
            mInvoicesAdapter = new InvoicesAdapter(getActivity(), invoicesByType, mProfile.getOrders());
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mInvoicesAdapter);
            mNoInvoicesView.setVisibility(View.GONE);
        } else {
            mNoInvoicesView.setVisibility(View.VISIBLE);
        }

        isLoading = false;
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private List<Invoice> getInvoicesBasedOnSelectedType() {
        if (isAllSelected) {
            return mProfile.getInvoices();
        } else if (isUnpaidSelected) {
            return getUnpaidInvoices();
        } else if (isPaidSelected) {
            return getPaidInvoices();
        } else {
            return getRejectedInvoices();
        }
    }

    private List<Invoice> getUnpaidInvoices() {
        List<Invoice> unpaidInvoices = new ArrayList<>();
        if(mProfile.getInvoices() != null) {
            for (Invoice invoice : mProfile.getInvoices()) {
                if (!invoice.isInvoicePaid() && !invoice.isInvoicePaymentRejected() && !invoice.isPartialPayment()) {
                    unpaidInvoices.add(invoice);
                }
            }
        }
        return unpaidInvoices;
    }

    private List<Invoice> getPaidInvoices() {
        List<Invoice> paidInvoices = new ArrayList<>();
        if(mProfile.getInvoices() != null) {
            for (Invoice invoice : mProfile.getInvoices()) {
                if (invoice.isInvoicePaid()) {
                    paidInvoices.add(invoice);
                }
            }
        }
        return paidInvoices;
    }

    private List<Invoice> getRejectedInvoices() {
        List<Invoice> rejectedInvoices = new ArrayList<>();
        if(mProfile.getInvoices() != null) {
            for (Invoice invoice : mProfile.getInvoices()) {
                if (invoice.isInvoicePaymentRejected()) {
                    rejectedInvoices.add(invoice);
                }
            }
        }
        return rejectedInvoices;
    }

    private void saveProfile(Profile profile) {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefsEditor = appPreferences.edit();
        Gson gson = new Gson();

        String updatedJson = gson.toJson(profile);
        prefsEditor.putString(SHARED_PROFILE_KEY, updatedJson);
        prefsEditor.apply();
    }

    private void removeAllMarks() {
        mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mAllTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mUnpaidTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mUnpaidTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mPaidTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mPaidTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mRejectedTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mRejectedTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void resetAllFlags() {
        isAllSelected = false;
        isUnpaidSelected = false;
        isPaidSelected = false;
        isRejectedSelected = false;
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
