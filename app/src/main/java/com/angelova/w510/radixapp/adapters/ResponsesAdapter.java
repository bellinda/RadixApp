package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.OfferResponse;
import com.angelova.w510.radixapp.models.Profile;
import com.google.gson.Gson;

import java.text.ParseException;
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
    public static final String SHARED_PROFILE_KEY = "profile";

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

            viewHolder.mResponseTitle = (TextView) convertView.findViewById(R.id.response_title);
            viewHolder.mResponseContent = (TextView) convertView.findViewById(R.id.response_content);
            viewHolder.mResponseFooter = (TextView) convertView.findViewById(R.id.response_footer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResponsesAdapter.ViewHolder) convertView.getTag();
        }

        if(response.isFromAdmin()) {
            viewHolder.mResponseContent.setText(buildResponseFromAdminContent(response));

            String footer = buildAdminFooterText(response);
            viewHolder.mResponseFooter.setText(footer);
        } else {
            viewHolder.mResponseTitle.setText("Your response:");
            viewHolder.mResponseContent.setText(response.getComment());

            String footer = buildUserFooterText(response);
            viewHolder.mResponseFooter.setText(footer);
        }

        return convertView;
    }

    private String buildResponseFromAdminContent(OfferResponse response) {
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

    private String buildAdminFooterText(OfferResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append("Radix Services, ");

        try {
            String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.UK);
            Date createdOn = formatter.parse(response.getCreatedOn());
            String outputFormat = "dd MMM yyyy, HH:mm";
            SimpleDateFormat dateFormatter = new SimpleDateFormat(outputFormat, Locale.UK);
            String cratedOnFormatted = dateFormatter.format(createdOn);
            builder.append(cratedOnFormatted);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return builder.toString();
    }

    private String buildUserFooterText(OfferResponse response) {
        StringBuilder builder = new StringBuilder();
        builder.append(getProfile().getName());
        builder.append(", ");

        try {
            String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.UK);
            Date createdOn = formatter.parse(response.getCreatedOn());
            String outputFormat = "dd MMM yyyy, HH:mm";
            SimpleDateFormat dateFormatter = new SimpleDateFormat(outputFormat, Locale.UK);
            String cratedOnFormatted = dateFormatter.format(createdOn);
            builder.append(cratedOnFormatted);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
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

    private Profile getProfile() {
        SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        Profile profile = new Profile();
        String json = appPreferences.getString(SHARED_PROFILE_KEY, "");
        if(!json.isEmpty()) {
            profile = gson.fromJson(json, Profile.class);
        }
        return profile;
    }

    private static class ViewHolder {
        TextView mResponseTitle;
        TextView mResponseContent;
        TextView mResponseFooter;
    }
}
