package com.angelova.w510.radixapp.list_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OrdersAdapter;
import com.angelova.w510.radixapp.menu_items.OrderActivity;
import com.angelova.w510.radixapp.menu_items.ProfileActivity;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllOrdersTask;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment {

    public static final String SHARED_PROFILE_KEY = "profile";

    private ListView mListView;
    private TextView mNoOrdersView;
    private FloatingActionButton mAddNewOrderBtn;
    private OrdersAdapter mOrdersAdapter;

    private TextView mAllTitle;
    private TextView mNotProcessedTitle;
    private TextView mInProgressTitle;
    private TextView mReadyTitle;

    private Profile mProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_orders, container, false);
        mListView = (ListView) rootView.findViewById(R.id.listView);
        mNoOrdersView = (TextView) rootView.findViewById(R.id.no_orders_view);
        mAddNewOrderBtn = (FloatingActionButton) rootView.findViewById(R.id.add_new_order_btn);

        mAllTitle = (TextView) rootView.findViewById(R.id.all_title);
        mNotProcessedTitle = (TextView) rootView.findViewById(R.id.not_processed_title);
        mInProgressTitle = (TextView) rootView.findViewById(R.id.in_progress_title);
        mReadyTitle = (TextView) rootView.findViewById(R.id.ready_title);

//        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) mAddNewOrderBtn.getLayoutParams();
//        lp.anchorGravity = Gravity.BOTTOM | GravityCompat.END;
//        mAddNewOrderBtn.setLayoutParams(lp);

        mProfile = getProfile();

        if (mProfile.getOrders() != null && mProfile.getOrders().size() > 0) {
            mOrdersAdapter = new OrdersAdapter(getActivity(), mProfile.getOrders());
            mListView.setAdapter(mOrdersAdapter);
        } else {
            mNoOrdersView.setVisibility(View.VISIBLE);
        }

        mAddNewOrderBtn.attachToListView(mListView, new ScrollDirectionListener() {
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

        mAddNewOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                startActivity(intent);
            }
        });

        mAllTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getProfile().getOrders() != null && getProfile().getOrders().size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(getActivity(), getProfile().getOrders());
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
                removeAllMarks();
                mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mAllTitle.setTextColor(getResources().getColor(R.color.white));
            }
        });

        mNotProcessedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> notProcessedOrders = getNotProcessedOrders();
                if(notProcessedOrders.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(getActivity(), notProcessedOrders);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
                removeAllMarks();
                mNotProcessedTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mNotProcessedTitle.setTextColor(getResources().getColor(R.color.white));
            }
        });

        mInProgressTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> ordersInProgress = getInProgressOrders();
                if(ordersInProgress.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(getActivity(), ordersInProgress);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
                removeAllMarks();
                mInProgressTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mInProgressTitle.setTextColor(getResources().getColor(R.color.white));
            }
        });

        mReadyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> readyOrders = getReadyOrders();
                if(readyOrders.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(getActivity(), readyOrders);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
                removeAllMarks();
                mReadyTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mReadyTitle.setTextColor(getResources().getColor(R.color.white));
            }
        });

        new GetAllOrdersTask(getActivity(), "orders/mobile", mProfile.getUserId(), mProfile.getToken()).execute();

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

    private int getNotProcessedOrdersCount() {
        int notProcessedOrdersCount = 0;
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                    notProcessedOrdersCount++;
                }
            }
        }
        return notProcessedOrdersCount;
    }

    private int getInProgressOrdersCount() {
        int inProgressOrdersCount = 0;
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                    inProgressOrdersCount++;
                }
            }
        }
        return inProgressOrdersCount;
    }

    private int getReadyOrdersCount() {
        int readyOrdersCount = 0;
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.isReady()) {
                    readyOrdersCount++;
                }
            }
        }
        return readyOrdersCount;
    }

    private List<Order> getNotProcessedOrders() {
        List<Order> notProcessedOrders = new ArrayList<>();
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                    notProcessedOrders.add(order);
                }
            }
        }
        return notProcessedOrders;
    }

    private List<Order> getInProgressOrders() {
        List<Order> ordersInProgress = new ArrayList<>();
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                    ordersInProgress.add(order);
                }
            }
        }
        return ordersInProgress;
    }

    private List<Order> getReadyOrders() {
        List<Order> readyOrders = new ArrayList<>();
        if (mProfile.getOrders() != null) {
            for (Order order : mProfile.getOrders()) {
                if (order.isReady()) {
                    readyOrders.add(order);
                }
            }
        }
        return readyOrders;
    }

    private void removeAllMarks() {
        mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mAllTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mNotProcessedTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mNotProcessedTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mInProgressTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mInProgressTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
        mReadyTitle.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mReadyTitle.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void handleOrdersLoaded(List<Order> orders) {
        mProfile.setOrders(orders);
        saveProfile(mProfile);
        if (mProfile.getOrders() != null && mProfile.getOrders().size() > 0) {
            mOrdersAdapter = new OrdersAdapter(getActivity(), mProfile.getOrders());
            mListView.setAdapter(mOrdersAdapter);
            mNoOrdersView.setVisibility(View.GONE);
        } else {
            mNoOrdersView.setVisibility(View.VISIBLE);
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
}
