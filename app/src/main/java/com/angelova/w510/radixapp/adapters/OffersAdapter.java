package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.details_activities.OfferDetailsActivity;
import com.angelova.w510.radixapp.models.Offer;
import java.util.List;
import java.util.Locale;

/**
 * Created by W510 on 11.2.2018 г..
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Context context;
    private List<Offer> mOffers;

    public OffersAdapter(Context context, List<Offer> offers) {
        this.context = context;
        this.mOffers = offers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Offer offer = mOffers.get(position);
        viewHolder.fromLanguage.setText(offer.getFromLanguage());
        viewHolder.toLanguage.setText(offer.getToLanguage());
        viewHolder.filesCount.setText(String.format(Locale.US, "%d file(s)", offer.getFileNames().size()));
        viewHolder.submittedOn.setText(String.format(Locale.US, "Sent On: %s", offer.getCreatedOn()));
        if(offer.isGotResponse()) {
            viewHolder.statusBar.setBackgroundColor(context.getResources().getColor(R.color.colorOfferGotResponse));
        } else {
            viewHolder.statusBar.setBackgroundColor(context.getResources().getColor(R.color.colorOfferPending));
        }

        viewHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OfferDetailsActivity.class);
                intent.putExtra("offer", offer);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOffers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView filesCount;
        TextView submittedOn;
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
        }
    }
}
