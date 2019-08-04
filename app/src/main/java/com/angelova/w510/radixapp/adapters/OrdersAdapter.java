package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.details_activities.OrderDetailsActivity;
import com.angelova.w510.radixapp.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by W510 on 17.2.2018 г..
 */

public class OrdersAdapter extends Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> mOrders;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.mOrders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orders_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Order order = mOrders.get(position);
        viewHolder.fromLanguage.setText(order.getFromLanguage());
        viewHolder.toLanguage.setText(order.getToLanguage());
        viewHolder.filesCount.setText(String.format(Locale.US, "%d file(s)", order.getAllFileNames().size()));
        if(order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
            viewHolder.submittedOn.setText(String.format(Locale.US, "Sent On: %s", order.getCreatedOn()));
            viewHolder.statusBar.setBackgroundColor(context.getResources().getColor(R.color.colorPending));
//            if(order.isGotResponse()) {
//                viewHolder.statusIcon.setBackgroundResource(R.mipmap.ic_got_response);
//            } else {
//                viewHolder.statusIcon.setBackgroundResource(R.drawable.ic_sand_clock);
//            }
        } else if (order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate()) && !order.isReady()) {
            String expectedDeliveryDate = order.getExpectedDeliveryDate();
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); //2018-02-04T12:42:35.042Z
            originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
            targetFormat.setTimeZone(Calendar.getInstance().getTimeZone());
            String expectedDeliveryDateToBeShown = "";
            try {
                Date date = originalFormat.parse(expectedDeliveryDate);
                expectedDeliveryDateToBeShown = targetFormat.format(date);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            viewHolder.submittedOn.setText(String.format(Locale.US, "Expected delivery date: %s", expectedDeliveryDateToBeShown));
            viewHolder.statusBar.setBackgroundColor(context.getResources().getColor(R.color.colorInProgress)); //.setBackgroundResource(R.mipmap.ic_progress);
        } else {
            //TODO: to get from backend date when got ready
            viewHolder.submittedOn.setText(String.format(Locale.US, "Got ready on: %s", order.getExpectedDeliveryDate()));
            viewHolder.statusBar.setBackgroundColor(context.getResources().getColor(R.color.colorReady));
        }

        viewHolder.responsesCount.setText(String.format("%d", order.getResponses() != null ? order.getResponses().size() : 0));

        viewHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("order", order);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView filesCount;
        TextView submittedOn;
        TextView responsesCount;
        ConstraintLayout mainContainer;
        View statusBar;

        public ViewHolder(View view) {
            super(view);
            fromLanguage = (TextView) view.findViewById(R.id.from_language);
            toLanguage = (TextView) view.findViewById(R.id.to_language);
            filesCount = (TextView) view.findViewById(R.id.files_count);
            submittedOn = (TextView) view.findViewById(R.id.submitted_on);
            statusBar = view.findViewById(R.id.status_color_bar);
            mainContainer = (ConstraintLayout) view.findViewById(R.id.main_container);
            responsesCount = (TextView) view.findViewById(R.id.responses_count);
        }
    }

    private String getLanguageAbbreviation(String language) {
        switch (language) {
            case "Bulgarian":
                return "BG";
            case "German":
                return "DE";
            case "French":
                return "FR";
            default:
                return "EN";
        }
    }
}
