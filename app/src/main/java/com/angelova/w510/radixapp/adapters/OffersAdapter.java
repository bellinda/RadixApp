package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.details_activities.OfferDetailsActivity;
import com.angelova.w510.radixapp.list_fragments.AllOffersActivity;
import com.angelova.w510.radixapp.menu_items.OfferActivity;
import com.angelova.w510.radixapp.models.Offer;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 11.2.2018 г..
 */

public class OffersAdapter extends ArrayAdapter<Offer> {

    private Context context;

    public OffersAdapter(Context context, List<Offer> offers) {
        super(context, R.layout.offers_list_item, offers);
        this.context = context;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        final Offer offer = getItem(position);
        OffersAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new OffersAdapter.ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.offers_list_item, parent, false);

            viewHolder.fromLanguage = (TextView) convertView.findViewById(R.id.from_language);
            viewHolder.toLanguage = (TextView) convertView.findViewById(R.id.to_language);
            viewHolder.filesCount = (TextView) convertView.findViewById(R.id.files_count);
            viewHolder.submittedOn = (TextView) convertView.findViewById(R.id.submitted_on);
            viewHolder.viewButton = (Button) convertView.findViewById(R.id.view_btn);
            viewHolder.progressIcon = (ImageView) convertView.findViewById(R.id.progress_icon);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OffersAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.fromLanguage.setText(getLanguageAbbreviation(offer.getFromLanguage()));
        viewHolder.toLanguage.setText(getLanguageAbbreviation(offer.getToLanguage()));
        viewHolder.filesCount.setText(String.format(Locale.US, "%d file(s)", offer.getFileNames().size()));
        viewHolder.submittedOn.setText(String.format(Locale.US, "Sent On: %s", offer.getCreatedOn()));
        if(offer.isGotResponse()) {
            viewHolder.progressIcon.setBackgroundResource(R.mipmap.ic_got_response);
        }

        viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfferDetailsActivity.class);
                intent.putExtra("offer", offer);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView filesCount;
        TextView submittedOn;
        Button viewButton;
        ImageView progressIcon;
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
