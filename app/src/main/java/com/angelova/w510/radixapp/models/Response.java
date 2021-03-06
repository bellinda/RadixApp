package com.angelova.w510.radixapp.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by W510 on 17.2.2018 г..
 */

public class Response implements Comparable<Response>, Serializable {

    private String createdOn;
    private String comment;
    private String quantity;
    private String expectedDeliveryDate;
    private String unitPrice;
    private String anticipatedPrice;
    private String countPer;
    private String status;
    private boolean fromAdmin;

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAnticipatedPrice() {
        return anticipatedPrice;
    }

    public void setAnticipatedPrice(String anticipatedPrice) {
        this.anticipatedPrice = anticipatedPrice;
    }

    public String getCountPer() {
        return countPer;
    }

    public void setCountPer(String countPer) {
        this.countPer = countPer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFromAdmin() {
        return fromAdmin;
    }

    public void setFromAdmin(boolean fromAdmin) {
        this.fromAdmin = fromAdmin;
    }

    @Override
    public int compareTo(@NonNull Response o) {
        String serverDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(serverDateFormat, Locale.UK);
        try {
            Date createdOn = formatter.parse(getCreatedOn());
            Date oCreatedOn = formatter.parse(o.getCreatedOn());
            return createdOn.compareTo(oCreatedOn);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return 0;
    }
}
