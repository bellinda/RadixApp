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

public class AllOrdersActivity extends BaseActivity {

    public static final String SHARED_PROFILE_KEY = "profile";

    private TextView mNotProcessedOrdersCount;
    private TextView mInProgressOrdersCount;
    private TextView mReadyOrdersCount;
    private ListView mListView;
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
        mNotProcessedOrdersCount = (TextView) findViewById(R.id.not_processed_orders_count);
        mInProgressOrdersCount = (TextView) findViewById(R.id.in_progress_orders_count);
        mReadyOrdersCount = (TextView) findViewById(R.id.ready_orders_count);
        mListView = (ListView) findViewById(R.id.listView);
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
}
