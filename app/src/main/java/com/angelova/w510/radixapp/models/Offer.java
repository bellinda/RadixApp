package com.angelova.w510.radixapp.models;

import android.net.Uri;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by W510 on 21.1.2018 г..
 */

public class Offer {

    private String name;
    private String fromLanguage;
    private String toLanguage;
    private String orderType;
    private String translationType;
    private String notes;
    private String email;
    private String phone;
    private List<Uri> documentUris;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromLanguage() {
        return fromLanguage;
    }

    public void setFromLanguage(String fromLanguage) {
        this.fromLanguage = fromLanguage;
    }

    public String getToLanguage() {
        return toLanguage;
    }

    public void setToLanguage(String toLanguage) {
        this.toLanguage = toLanguage;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTranslationType() {
        return translationType;
    }

    public void setTranslationType(String translationType) {
        this.translationType = translationType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Uri> getDocumentUris () {
        return documentUris;
    }

    public void setDocumentUris(List<Uri> documentUris) {
        this.documentUris = documentUris;
    }
}
