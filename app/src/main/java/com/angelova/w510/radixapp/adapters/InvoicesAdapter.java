package com.angelova.w510.radixapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.angelova.w510.radixapp.InvoiceDetailsActivity;
import com.angelova.w510.radixapp.R;
import com.angelova.w510.radixapp.models.Invoice;
import com.angelova.w510.radixapp.models.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.ViewHolder> {

    private Context context;
    private List<Invoice> mInvoices;
    private List<Order> mOrders;

    public InvoicesAdapter(Context context, List<Invoice> invoices, List<Order> orders) {
        this.context = context;
        this.mInvoices = invoices;
        this.mOrders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoices_list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Invoice invoice = mInvoices.get(position);
        Order order = getOrderById(invoice.getOrderConsecutiveID());
        if (order != null) {
            viewHolder.fromLanguage.setText(order.getFromLanguage());
            viewHolder.toLanguage.setText(order.getToLanguage());
        } else {
            //TODO: download all orders newly and update the invoices or download only one order by id
        }
        viewHolder.invoiceId.setText(String.format("Invoice %s", invoice.getConsecutiveID()));
        viewHolder.type.setText(invoice.getInvoiceType());

        if (invoice.isPartialPayment()) {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.colorInvoicePartial));
            viewHolder.status.setText(context.getString(R.string.fragment_invoices_partial));
        } else if (invoice.isInvoicePaid()) {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.colorInvoicePaid));
            viewHolder.status.setText(context.getString(R.string.fragment_invoices_paid));
        } else if (invoice.isInvoicePaymentRejected()) {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.colorInvoiceRejected));
            viewHolder.status.setText(context.getString(R.string.fragment_invoices_rejected));
        } else {
            viewHolder.status.setBackgroundColor(context.getResources().getColor(R.color.colorInvoiceUnpaid));
            viewHolder.status.setText(context.getString(R.string.fragment_invoices_unpaid));
        }

        viewHolder.amount.setText(String.format("%s lv", invoice.getInvoicedAmount()));

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2018-02-04T12:42:35.042Z
        originalFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        targetFormat.setTimeZone(Calendar.getInstance().getTimeZone());
        try {
            Date date = originalFormat.parse(invoice.getCreatedAt());
            String formattedDate = targetFormat.format(date);
            viewHolder.invoiceDate.setText(String.format("Invoice date: %s", formattedDate));

            if (!invoice.isInvoicePaid()) {
                date = originalFormat.parse(invoice.getPayBefore());
                formattedDate = targetFormat.format(date);
                viewHolder.dueDate.setText(String.format("Due date: %s", formattedDate));
            } else {
                viewHolder.dueDate.setVisibility(View.GONE);
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        viewHolder.mainContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InvoiceDetailsActivity.class);
                intent.putExtra("invoice", invoice);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInvoices.size();
    }

    private Order getOrderById(String id) {
        for (Order order : mOrders) {
            if (order.getId().equals(id)) {
                return order;
            }
        }
        return null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView fromLanguage;
        TextView toLanguage;
        TextView invoiceId;
        TextView type;
        TextView status;
        TextView amount;
        TextView invoiceDate;
        TextView dueDate;
        ConstraintLayout mainContainer;

        public ViewHolder(View view) {
            super(view);
            fromLanguage = (TextView) view.findViewById(R.id.from_language);
            toLanguage = (TextView) view.findViewById(R.id.to_language);
            invoiceId = (TextView) view.findViewById(R.id.invoice_id);
            type = (TextView) view.findViewById(R.id.type);
            status = (TextView) view.findViewById(R.id.status);
            amount = (TextView) view.findViewById(R.id.amount);
            invoiceDate = (TextView) view.findViewById(R.id.invoice_date);
            dueDate = (TextView) view.findViewById(R.id.due_date);
            mainContainer = (ConstraintLayout) view.findViewById(R.id.main_container);
        }
    }
}
