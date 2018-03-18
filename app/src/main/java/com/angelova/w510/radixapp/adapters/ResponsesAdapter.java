package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.details_activities.OfferDetailsActivity;
import com.angelova.w510.radixapp.models.Offer;
import com.angelova.w510.radixapp.models.OfferResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by W510 on 18.3.2018 г..
 */

public class ResponsesAdapter extends ArrayAdapter<OfferResponse> {

    private Context context;

    public ResponsesAdapter(Context context, List<OfferResponse> responses) {
        super(context, R.layout.respones_list_item, responses);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        final OfferResponse response = getItem(position);
        ResponsesAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ResponsesAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.respones_list_item, parent, false);

            viewHolder.mResponseContent = (TextView) convertView.findViewById(R.id.response_content);
            viewHolder.mResponseFooter = (TextView) convertView.findViewById(R.id.response_footer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResponsesAdapter.ViewHolder) convertView.getTag();
        }

        //TODO: check if answer from admin
        viewHolder.mResponseContent.setText(buildResponseContent(response));

        String footer = buildFooterText(response);
        viewHolder.mResponseFooter.setText(footer);

        return convertView;
    }

    private String buildResponseContent(OfferResponse response) {
        StringBuilder content = new StringBuilder();
        String expectedDeliveryDate = convertServerDateToUserTimeZone(response.getExpectedDeliveryDate());
        if(response.getCountPer().equalsIgnoreCase("pages")) {
            content.append(String.format("It seems like you have a document(s) with %s pages. Our unit price is %s, so the anticipated price of your translation will be %s and the expected delivery date is %s.\n*The final price of your document could be given when the translation is ready, because of the final count of the pages.",
                    response.getQuantity(), response.getUnitPrice(), response.getAnticipatedPrice(), expectedDeliveryDate));
        } else {
            content.append(String.format("It seems like you have a document(s) with %s words. Our unit price is %s, so the anticipated price of your translation will be %s and the expected delivery date is %s.\n*The final price of your document could be given when the translation is ready, because of the final count of the words.",
                    response.getQuantity(), response.getUnitPrice(), response.getAnticipatedPrice(), expectedDeliveryDate));
        }
        content.append("\n");
        content.append(String.format("Comment: %s", response.getComment()));

        return content.toString();
    }

    private String buildFooterText(OfferResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("Radix Services, ");

        String dateInCurrentTimeZone = convertServerDateToUserTimeZone(response.getCreatedOn());
        builder.append(dateInCurrentTimeZone);

        return builder.toString();
    }

    public String convertServerDateToUserTimeZone(String serverDate) {
        String ourdate;
        try {
            //2019-10-10T07:10:00.000Z
            String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.UK);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = formatter.parse(serverDate);
            Calendar cal = Calendar.getInstance();
            TimeZone timeZone = cal.getTimeZone();
            String outputFormat = "dd MMM yyyy, HH:mm";
            SimpleDateFormat dateFormatter = new SimpleDateFormat(outputFormat, Locale.UK); //this format changeable
            dateFormatter.setTimeZone(timeZone);
            ourdate = dateFormatter.format(value);

            //Log.d("OurDate", OurDate);
        } catch (Exception e) {
            ourdate = "0000-00-00 00:00:00";
        }
        return ourdate;
    }

    private static class ViewHolder {
        TextView mResponseContent;
        TextView mResponseFooter;
    }
}
