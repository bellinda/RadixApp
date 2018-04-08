package com.angelova.w510.radixapp.list_fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.angelova.w510.radixapp.BaseActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.adapters.OrdersAdapter;
import com.angelova.w510.radixapp.menu_items.OrderActivity;
import com.angelova.w510.radixapp.models.Order;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private TextView mTitleView;
    private TextView mNotProcessedOrdersCount;
    private TextView mInProgressOrdersCount;
    private TextView mReadyOrdersCount;
    private ListView mListView;
    private TextView mNoOrdersView;
    private FloatingActionButton mAddNewOrderBtn;
    private OrdersAdapter mOrdersAdapter;

    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_orders);

        initializeActivity();
    }

    private void initializeActivity() {
        mTitleView = (TextView) findViewById(R.id.header_title);
        mNotProcessedOrdersCount = (TextView) findViewById(R.id.not_processed_orders_count);
        mInProgressOrdersCount = (TextView) findViewById(R.id.in_progress_orders_count);
        mReadyOrdersCount = (TextView) findViewById(R.id.ready_orders_count);
        mListView = (ListView) findViewById(R.id.listView);
        mNoOrdersView = (TextView) findViewById(R.id.no_orders_view);
        mAddNewOrderBtn = (FloatingActionButton) findViewById(R.id.add_new_order_btn);

        mProfile = getProfile();

        mOrdersAdapter = new OrdersAdapter(this, mProfile.getOrders());
        mListView.setAdapter(mOrdersAdapter);

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
                Intent intent = new Intent(AllOrdersActivity.this, OrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mNotProcessedOrdersCount.setText(getNotProcessedOrdersCount() + "");
        mInProgressOrdersCount.setText(getInProgressOrdersCount() + "");
        mReadyOrdersCount.setText(getReadyOrdersCount() + "");

        mTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNoOrdersView.getVisibility() == View.VISIBLE) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                }
                mOrdersAdapter = new OrdersAdapter(AllOrdersActivity.this, mProfile.getOrders());
                mListView.setAdapter(mOrdersAdapter);
            }
        });

        mNotProcessedOrdersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> notProcessedOrders = getNotProcessedOrders();
                if(notProcessedOrders.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(AllOrdersActivity.this, notProcessedOrders);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
            }
        });

        mInProgressOrdersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> ordersInProgress = getInProgressOrders();
                if(ordersInProgress.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(AllOrdersActivity.this, ordersInProgress);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
                }
            }
        });

        mReadyOrdersCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Order> readyOrders = getReadyOrders();
                if(readyOrders.size() > 0) {
                    mNoOrdersView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mOrdersAdapter = new OrdersAdapter(AllOrdersActivity.this, readyOrders);
                    mListView.setAdapter(mOrdersAdapter);
                } else {
                    mListView.setVisibility(View.GONE);
                    mNoOrdersView.setVisibility(View.VISIBLE);
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

    private int getNotProcessedOrdersCount() {
        int notProcessedOrdersCount = 0;
        for(Order order : mProfile.getOrders()) {
            if(order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                notProcessedOrdersCount++;
            }
        }
        return notProcessedOrdersCount;
    }

    private int getInProgressOrdersCount() {
        int inProgressOrdersCount = 0;
        for(Order order : mProfile.getOrders()) {
            if(order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                inProgressOrdersCount++;
            }
        }
        return inProgressOrdersCount;
    }

    private int getReadyOrdersCount() {
        int readyOrdersCount = 0;
        for(Order order : mProfile.getOrders()) {
            if(order.isReady()) {
                readyOrdersCount++;
            }
        }
        return readyOrdersCount;
    }

    private List<Order> getNotProcessedOrders() {
        List<Order> notProcessedOrders = new ArrayList<>();
        for(Order order : mProfile.getOrders()) {
            if(order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                notProcessedOrders.add(order);
            }
        }
        return notProcessedOrders;
    }

    private List<Order> getInProgressOrders() {
        List<Order> ordersInProgress = new ArrayList<>();
        for(Order order : mProfile.getOrders()) {
            if(order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
                ordersInProgress.add(order);
            }
        }
        return ordersInProgress;
    }

    private List<Order> getReadyOrders() {
        List<Order> readyOrders = new ArrayList<>();
        for(Order order : mProfile.getOrders()) {
            if(order.isReady()) {
                readyOrders.add(order);
            }
        }
        return readyOrders;
    }
}
