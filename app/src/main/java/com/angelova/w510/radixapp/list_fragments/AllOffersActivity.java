package com.angelova.w510.radixapp.list_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OffersAdapter;
import com.angelova.w510.radixapp.menu_items.OfferActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

public class AllOffersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private ListView mListView;
    private FloatingActionButton mAddNewOfferBtn;
    private OffersAdapter mOffersAdapter;
    private TextView mPendingOffersCount;
    private TextView mOffersWithResponseCount;
    private CoordinatorLayout mMainContentLayout;
    private TextView mNoOffersView;
    private TextView mTitleView;

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_offers);

        initializeActivity();
    }

    private void initializeActivity() {
        mMainContentLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mListView = (ListView) findViewById(R.id.listView);
        mAddNewOfferBtn = (FloatingActionButton) findViewById(R.id.add_new_offer_btn);
        mPendingOffersCount = (TextView) findViewById(R.id.pending_offers_count);
        mOffersWithResponseCount = (TextView) findViewById(R.id.answered_offers_count);
        mTitleView = (TextView) findViewById(R.id.header_title);
        mNoOffersView = (TextView) findViewById(R.id.no_offers_view);
        mProfile = getProfile();

        mOffersAdapter = new OffersAdapter(this, mProfile.getOffers());
        mListView.setAdapter(mOffersAdapter);

        mAddNewOfferBtn.attachToListView(mListView, new ScrollDirectionListener() {
            @Override
            public void onScrollDown() {
                //Log.d("ListViewFragment", "onScrollDown()");
            }

            @Override
            public void onScrollUp() {
                //Log.d("ListViewFragment", "onScrollUp()");
            }
        }, new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //Log.d("ListViewFragment", "onScrollStateChanged()");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //Log.d("ListViewFragment", "onScroll()");
            }
        });

        mAddNewOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllOffersActivity.this, OfferActivity.class);
                startActivity(intent);
                finish();
//                Snackbar snackbar = Snackbar
//                        .make(mMainContentLayout, "Clicked on the floating button", Snackbar.LENGTH_LONG);
//                snackbar.show();
            }
        });

        mPendingOffersCount.setText(getPendingOffersCount() + "");
        mOffersWithResponseCount.setText(getOffersWithResponseCount() + "");

        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNoOffersView.getVisibility() == View.VISIBLE) {
                    mNoOffersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
                mOffersAdapter = new OffersAdapter(AllOffersActivity.this, mProfile.getOffers());
                mListView.setAdapter(mOffersAdapter);
            }
        });

        mPendingOffersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Offer> pendingOffers = getPendingOffers();
                if(pendingOffers.size() > 0) {
                    mNoOffersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOffersAdapter = new OffersAdapter(AllOffersActivity.this, pendingOffers);
                    mListView.setAdapter(mOffersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOffersView.setVisibility(View.VISIBLE);
                }
            }
        });

        mOffersWithResponseCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Offer> offersWithResponse = getOffersWithResponse();
                if(offersWithResponse.size() > 0) {
                    mNoOffersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOffersAdapter = new OffersAdapter(AllOffersActivity.this, offersWithResponse);
                    mListView.setAdapter(mOffersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOffersView.setVisibility(View.VISIBLE);
                }
            }
        });
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

    private int getPendingOffersCount() {
        int pendingOffersCount = 0;
        for(Offer offer : mProfile.getOffers()) {
            if(!offer.isGotResponse()) {
                pendingOffersCount++;
            }
        }

        return pendingOffersCount;
    }

    private int getOffersWithResponseCount() {
        int offersWithResponseCount = 0;
        for(Offer offer : mProfile.getOffers()) {
            if(offer.isGotResponse()) {
                offersWithResponseCount++;
            }
        }
        return offersWithResponseCount;
    }

    private List<Offer> getPendingOffers() {
        List<Offer> pendingOffers = new ArrayList<>();
        for(Offer offer : mProfile.getOffers()) {
            if(!offer.isGotResponse()) {
                pendingOffers.add(offer);
            }
        }
        return pendingOffers;
    }

    private List<Offer> getOffersWithResponse() {
        List<Offer> offersWithResponse = new ArrayList<>();
        for(Offer offer : mProfile.getOffers()) {
            if(offer.isGotResponse()) {
                offersWithResponse.add(offer);
            }
        }
        return offersWithResponse;
    }
}
