package com.angelova.w510.radixapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OffersAdapter;
import com.angelova.w510.radixapp.OfferActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllOffersTask;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AllOffersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String SHARED_PROFILE_KEY = "profile";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mListView;
    private FloatingActionButton mAddNewOfferBtn;
    private OffersAdapter mOffersAdapter;
    private CoordinatorLayout mMainContentLayout;
    private TextView mNoOffersView;

    private TextView mAllTitle;
    private TextView mPendingTitle;
    private TextView mWithResponseTitle;

    private Profile mProfile;

    private boolean isAllSelected = true;
    private boolean isPendingSelected = false;
    private boolean isWithResponsesSelected = false;

    private boolean isLoading = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_offers, container, false);
        mMainContentLayout = (CoordinatorLayout) rootView.findViewById(R.id.main_content);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mListView = (RecyclerView) rootView.findViewById(R.id.listView);
        mAddNewOfferBtn = (FloatingActionButton) rootView.findViewById(R.id.add_new_offer_btn);
        mAllTitle = (TextView) rootView.findViewById(R.id.all_title);
        mPendingTitle = (TextView) rootView.findViewById(R.id.pending_title);
        mWithResponseTitle = (TextView) rootView.findViewById(R.id.with_response_title);
        mNoOffersView = (TextView) rootView.findViewById(R.id.no_offers_view);
        mProfile = getProfile();

        if (mProfile.getOffers() != null && mProfile.getOffers().size() > 0) {
            mOffersAdapter = new OffersAdapter(getActivity(), mProfile.getOffers());
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mOffersAdapter);
        } else {
            mNoOffersView.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mAddNewOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    Intent intent = new Intent(getActivity(), OfferActivity.class);
                    startActivity(intent);
                }
            }
        });

        mAllTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    if (getProfile().getOrders() != null && getProfile().getOrders().size() > 0) {
                        mNoOffersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOffersAdapter = new OffersAdapter(getActivity(), getProfile().getOffers());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOffersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOffersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mAllTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isAllSelected = true;
                }
            }
        });

        mPendingTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Offer> pendingOffers = getPendingOffers();
                    if (pendingOffers.size() > 0) {
                        mNoOffersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOffersAdapter = new OffersAdapter(getActivity(), pendingOffers);
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOffersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOffersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mPendingTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mPendingTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isPendingSelected = true;
                }
            }
        });

        mWithResponseTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Offer> offersWithResponse = getOffersWithResponse();
                    if (offersWithResponse.size() > 0) {
                        mNoOffersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOffersAdapter = new OffersAdapter(getActivity(), offersWithResponse);
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOffersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOffersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mWithResponseTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mWithResponseTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isWithResponsesSelected = true;
                }
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        new GetAllOffersTask(getActivity(), "inquiries/mobile", mProfile.getUserId(), mProfile.getToken()).execute();

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

    private List<Offer> getPendingOffers() {
        List<Offer> pendingOffers = new ArrayList<>();
        if(mProfile.getOffers() != null) {
            for (Offer offer : mProfile.getOffers()) {
                if (!offer.isGotResponse()) {
                    pendingOffers.add(offer);
                }
            }
        }
        return pendingOffers;
    }

    private List<Offer> getOffersWithResponse() {
        List<Offer> offersWithResponse = new ArrayList<>();
        if(mProfile.getOffers() != null) {
            for (Offer offer : mProfile.getOffers()) {
                if (offer.isGotResponse()) {
                    offersWithResponse.add(offer);
                }
            }
        }
        return offersWithResponse;
    }

    public void handleOffersLoaded(List<Offer> offers) {
        mProfile.setOffers(offers);
        saveProfile(mProfile);
        List<Offer> offersByType = getOrdersBasedOnSelectedType();
        if (offersByType != null && offersByType.size() > 0) {
            mOffersAdapter = new OffersAdapter(getActivity(), offersByType);
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mOffersAdapter);
            mNoOffersView.setVisibility(View.GONE);
        } else {
            mNoOffersView.setVisibility(View.VISIBLE);
        }
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

    private List<Offer> getOrdersBasedOnSelectedType() {
        if (isAllSelected) {
            return getProfile().getOffers();
        } else if (isPendingSelected) {
            return getPendingOffers();
        } else {
            return getOffersWithResponse();
        }
    }

    private void removeAllMarks() {
        mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mAllTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mPendingTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mPendingTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mWithResponseTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mWithResponseTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void resetAllFlags() {
        isAllSelected = false;
        isPendingSelected = false;
        isWithResponsesSelected = false;
    }

    public void stopLoader() {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            isLoading = true;
            new GetAllOffersTask(getActivity(), "inquiries/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoading) {
            mSwipeRefreshLayout.setRefreshing(true);
            isLoading = true;
            new GetAllOffersTask(getActivity(), "inquiries/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
        }
    }
}
