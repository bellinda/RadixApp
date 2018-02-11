package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Offer;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 11.2.2018 г..
 */

public class OffersAdapter extends ArrayAdapter<Offer> {

    private Context context;

    public OffersAdapter(Context context, List<Offer> notes) {
        super(context, R.layout.offers_list_item, notes);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        Offer offer = getItem(position);
        OffersAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new OffersAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.offers_list_item, parent, false);

            viewHolder.fromLanguage = (TextView) convertView.findViewById(R.id.from_language);
            viewHolder.toLanguage = (TextView) convertView.findViewById(R.id.to_language);
            viewHolder.filesCount = (TextView) convertView.findViewById(R.id.files_count);
            viewHolder.submittedOn = (TextView) convertView.findViewById(R.id.submitted_on);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OffersAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.fromLanguage.setText(getLanguageAbbreviation(offer.getFromLanguage()));
        viewHolder.toLanguage.setText(getLanguageAbbreviation(offer.getToLanguage()));
        viewHolder.filesCount.setText(String.format(Locale.US, "%d file(s)", offer.getFileNames().size()));
        viewHolder.submittedOn.setText(String.format(Locale.US, "Sent On: %s", offer.getCreatedOn()));

        return convertView;
    }

    private static class ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView filesCount;
        TextView submittedOn;
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
