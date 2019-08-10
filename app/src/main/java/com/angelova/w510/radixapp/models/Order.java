package com.angelova.w510.radixapp.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

/**
 * Created by W510 on 4.2.2018 г..
 */

public class Order implements Serializable {

    private String id;
    private String name;
    private String fromLanguage;
    private String toLanguage;
    private String orderType;
    private String translationType;
    private String notes;
    private String email;
    private String phone;
    private String desiredDeliveryDate;
    private String expectedDeliveryDate;
    private String pickUpMethod;
    private String anticipatedPrice;
    private String anticipatedPriceByAdmin;
    private String inquiryToDelete;
    private String paymentMethod;
    private boolean requestsInvoice;
    private boolean requestsProformaInvoice;
    private InvoiceData invoiceData;
    private List<Uri> documentUris;
    private List<String> documentsFromOffer;
    private List<String> allFileNames;
    private boolean isReady;
    private String createdOn;
    private List<Response> responses;
    private boolean gotResponse;
    private String responsesCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDesiredDeliveryDate() {
        return desiredDeliveryDate;
    }

    public void setDesiredDeliveryDate(String desiredDeliveryDate) {
        this.desiredDeliveryDate = desiredDeliveryDate;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getPickUpMethod() {
        return pickUpMethod;
    }

    public void setPickUpMethod(String pickUpMethod) {
        this.pickUpMethod = pickUpMethod;
    }

    public String getAnticipatedPrice() {
        return anticipatedPrice;
    }

    public String getAnticipatedPriceByAdmin() {
        return anticipatedPriceByAdmin;
    }

    public void setAnticipatedPriceByAdmin(String anticipatedPriceByAdmin) {
        this.anticipatedPriceByAdmin = anticipatedPriceByAdmin;
    }

    public void setAnticipatedPrice(String anticipatedPrice) {
        this.anticipatedPrice = anticipatedPrice;
    }

    public String getInquiryToDelete() {
        return inquiryToDelete;
    }

    public void setInquiryToDelete(String inquiryToDelete) {
        this.inquiryToDelete = inquiryToDelete;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isRequestsInvoice() {
        return requestsInvoice;
    }

    public void setRequestsInvoice(boolean requestsInvoice) {
        this.requestsInvoice = requestsInvoice;
    }

    public boolean isRequestsProformaInvoice() {
        return requestsProformaInvoice;
    }

    public void setRequestsProformaInvoice(boolean requestsProformaInvoice) {
        this.requestsProformaInvoice = requestsProformaInvoice;
    }

    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<Uri> getDocumentUris() {
        return documentUris;
    }

    public void setDocumentUris(List<Uri> documentUris) {
        this.documentUris = documentUris;
    }

    public List<String> getDocumentsFromOffer() {
        return documentsFromOffer;
    }

    public void setDocumentsFromOffer(List<String> documentsFromOffer) {
        this.documentsFromOffer = documentsFromOffer;
    }

    public List<String> getAllFileNames() {
        return allFileNames;
    }

    public void setAllFileNames(List<String> allFileNames) {
        this.allFileNames = allFileNames;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setOrderResponses(List<Response> responses) {
        this.responses = responses;
    }

    public boolean isGotResponse() {
        return gotResponse;
    }

    public void setGotResponse(boolean gotResponse) {
        this.gotResponse = gotResponse;
    }

    public String getResponsesCount() {
        return responsesCount;
    }

    public void setResponsesCount(String responsesCount) {
        this.responsesCount = responsesCount;
    }
}
