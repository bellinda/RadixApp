package com.angelova.w510.radixapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OrdersAdapter;
import com.angelova.w510.radixapp.OrderActivity;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.angelova.w510.radixapp.tasks.GetAllOrdersTask;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.angelova.w510.radixapp.utils.Utils.SHARED_PROFILE_KEY;

public class AllOrdersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mListView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView mNoOrdersView;
    private FloatingActionButton mAddNewOrderBtn;
    private OrdersAdapter mOrdersAdapter;

    private TextView mAllTitle;
    private TextView mNotProcessedTitle;
    private TextView mInProgressTitle;
    private TextView mReadyTitle;

    private Profile mProfile;

    private boolean isLoading = true;

    private boolean isAllSelected = true;
    private boolean isPendingSelected = false;
    private boolean isInProgressSelected = false;
    private boolean isReadySelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_orders, container, false);
        mListView = (RecyclerView) rootView.findViewById(R.id.listView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mNoOrdersView = (TextView) rootView.findViewById(R.id.no_orders_view);
        mAddNewOrderBtn = (FloatingActionButton) rootView.findViewById(R.id.add_new_order_btn);

        mAllTitle = (TextView) rootView.findViewById(R.id.all_title);
        mNotProcessedTitle = (TextView) rootView.findViewById(R.id.not_processed_title);
        mInProgressTitle = (TextView) rootView.findViewById(R.id.in_progress_title);
        mReadyTitle = (TextView) rootView.findViewById(R.id.ready_title);

        mProfile = getProfile();

        if (mProfile.getOrders() != null && mProfile.getOrders().size() > 0) {
            mOrdersAdapter = new OrdersAdapter(getActivity(), mProfile.getOrders());
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mOrdersAdapter);
        } else {
            mNoOrdersView.setVisibility(View.VISIBLE);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mAddNewOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    startActivity(intent);
                }
            }
        });

        mAllTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLoading) {
                    if (getProfile().getOrders() != null && getProfile().getOrders().size() > 0) {
                        mNoOrdersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOrdersAdapter = new OrdersAdapter(getActivity(), getProfile().getOrders());
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOrdersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOrdersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mAllTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mAllTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isAllSelected = true;
                }
            }
        });

        mNotProcessedTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Order> notProcessedOrders = getNotProcessedOrders();
                    if (notProcessedOrders.size() > 0) {
                        mNoOrdersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOrdersAdapter = new OrdersAdapter(getActivity(), notProcessedOrders);
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOrdersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOrdersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mNotProcessedTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mNotProcessedTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isPendingSelected = true;
                }
            }
        });

        mInProgressTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Order> ordersInProgress = getInProgressOrders();
                    if (ordersInProgress.size() > 0) {
                        mNoOrdersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOrdersAdapter = new OrdersAdapter(getActivity(), ordersInProgress);
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOrdersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOrdersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mInProgressTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mInProgressTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isInProgressSelected = true;
                }
            }
        });

        mReadyTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoading) {
                    List<Order> readyOrders = getReadyOrders();
                    if (readyOrders.size() > 0) {
                        mNoOrdersView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                        mOrdersAdapter = new OrdersAdapter(getActivity(), readyOrders);
                        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        mListView.setHasFixedSize(true);
                        mListView.setAdapter(mOrdersAdapter);
                    } else {
                        mListView.setVisibility(View.GONE);
                        mNoOrdersView.setVisibility(View.VISIBLE);
                    }
                    removeAllMarks();
                    mReadyTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    mReadyTitle.setTextColor(getResources().getColor(R.color.white));
                    resetAllFlags();
                    isReadySelected = true;
                }
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
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

    private void resetAllFlags() {
        isAllSelected = false;
        isPendingSelected = false;
        isInProgressSelected = false;
        isReadySelected = false;
    }

    public void handleOrdersLoaded(List<Order> orders) {
        mProfile.setOrders(orders);
        saveProfile(mProfile);
        List<Order> ordersByType = getOrdersBasedOnSelectedType();
        if (ordersByType != null && ordersByType.size() > 0) {
            mOrdersAdapter = new OrdersAdapter(getActivity(), ordersByType);
            mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mListView.setHasFixedSize(true);
            mListView.setAdapter(mOrdersAdapter);
            mNoOrdersView.setVisibility(View.GONE);
        } else {
            mNoOrdersView.setVisibility(View.VISIBLE);
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

    private List<Order> getOrdersBasedOnSelectedType() {
        if (isAllSelected) {
            return getProfile().getOrders();
        } else if (isPendingSelected) {
            return getNotProcessedOrders();
        } else if (isInProgressSelected) {
            return getInProgressOrders();
        } else {
            return getReadyOrders();
        }
    }

    public void stopLoader() {
        isLoading = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            isLoading = true;
            new GetAllOrdersTask(getActivity(), "orders/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLoading) {
            mSwipeRefreshLayout.setRefreshing(true);
            isLoading = true;
            new GetAllOrdersTask(getActivity(), "orders/mobile", mProfile.getUserId(), mProfile.getToken()).execute();
        }
    }
}
