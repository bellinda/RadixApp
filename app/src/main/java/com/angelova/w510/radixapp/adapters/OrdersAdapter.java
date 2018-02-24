package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.Order;

import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 17.2.2018 г..
 */

public class OrdersAdapter extends ArrayAdapter<Order> {

    private Context context;

    public OrdersAdapter(Context context, List<Order> notes) {
        super(context, R.layout.orders_list_item, notes);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);
        OrdersAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new OrdersAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.orders_list_item, parent, false);

            viewHolder.fromLanguage = (TextView) convertView.findViewById(R.id.from_language);
            viewHolder.toLanguage = (TextView) convertView.findViewById(R.id.to_language);
            viewHolder.filesCount = (TextView) convertView.findViewById(R.id.files_count);
            viewHolder.submittedOn = (TextView) convertView.findViewById(R.id.submitted_on);
            viewHolder.statusIcon = (ImageView) convertView.findViewById(R.id.status_icon);
            viewHolder.viewButton = (Button) convertView.findViewById(R.id.view_btn);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OrdersAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.fromLanguage.setText(getLanguageAbbreviation(order.getFromLanguage()));
        viewHolder.toLanguage.setText(getLanguageAbbreviation(order.getToLanguage()));
        viewHolder.filesCount.setText(String.format(Locale.US, "%d file(s)", order.getAllFileNames().size()));
        if(order.getExpectedDeliveryDate() == null || TextUtils.isEmpty(order.getExpectedDeliveryDate())) {
            viewHolder.submittedOn.setText(String.format(Locale.US, "Sent On: %s", order.getCreatedOn()));
            viewHolder.statusIcon.setBackgroundResource(R.drawable.ic_sand_clock);
        } else if (order.getExpectedDeliveryDate() != null && !TextUtils.isEmpty(order.getExpectedDeliveryDate()) && !order.isReady()) {
            viewHolder.submittedOn.setText(String.format(Locale.US, "Expected delivery date:\n%s", order.getExpectedDeliveryDate()));
            viewHolder.statusIcon.setBackgroundResource(R.mipmap.ic_progress);
        } else {
            //TODO: to get from backend date when got ready
            viewHolder.submittedOn.setText(String.format(Locale.US, "Got ready on: %s", order.getExpectedDeliveryDate()));
            viewHolder.statusIcon.setBackgroundResource(R.mipmap.ic_ready);
        }

        viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView filesCount;
        TextView submittedOn;
        ImageView statusIcon;
        Button viewButton;
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