package com.angelova.w510.radixapp.models;

import java.util.List;

public class Invoice {

    private String postedBy;
    private String invoiceCurrency;
    private String invoiceLanguage;
    private boolean invoicePaid;
    private boolean partialPayment;
    private boolean invoicePaymentRejected;
    private boolean uploadedProofDocument;
    private boolean hasUploadedTranslations;
    private String userEmail;
    private String invoicedAmount;
    private String payBefore;
    private String invoiceType;
    private String invoiceTypeBG;
    private String adminComment;
    private String consecutiveID;
    private String orderConsecutiveID;
    private String createdAt;
    private String updatedAt;
    private List<String> file;

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }

    public String getInvoiceLanguage() {
        return invoiceLanguage;
    }

    public void setInvoiceLanguage(String invoiceLanguage) {
        this.invoiceLanguage = invoiceLanguage;
    }

    public boolean isInvoicePaid() {
        return invoicePaid;
    }

    public void setInvoicePaid(boolean invoicePaid) {
        this.invoicePaid = invoicePaid;
    }

    public boolean isPartialPayment() {
        return partialPayment;
    }

    public void setPartialPayment(boolean partialPayment) {
        this.partialPayment = partialPayment;
    }

    public boolean isInvoicePaymentRejected() {
        return invoicePaymentRejected;
    }

    public void setInvoicePaymentRejected(boolean invoicePaymentRejected) {
        this.invoicePaymentRejected = invoicePaymentRejected;
    }

    public boolean isUploadedProofDocument() {
        return uploadedProofDocument;
    }

    public void setUploadedProofDocument(boolean uploadedProofDocument) {
        this.uploadedProofDocument = uploadedProofDocument;
    }

    public boolean isHasUploadedTranslations() {
        return hasUploadedTranslations;
    }

    public void setHasUploadedTranslations(boolean hasUploadedTranslations) {
        this.hasUploadedTranslations = hasUploadedTranslations;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(String invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public String getPayBefore() {
        return payBefore;
    }

    public void setPayBefore(String payBefore) {
        this.payBefore = payBefore;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTypeBG() {
        return invoiceTypeBG;
    }

    public void setInvoiceTypeBG(String invoiceTypeBG) {
        this.invoiceTypeBG = invoiceTypeBG;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public String getConsecutiveID() {
        return consecutiveID;
    }

    public void setConsecutiveID(String consecutiveID) {
        this.consecutiveID = consecutiveID;
    }

    public String getOrderConsecutiveID() {
        return orderConsecutiveID;
    }

    public void setOrderConsecutiveID(String orderConsecutiveID) {
        this.orderConsecutiveID = orderConsecutiveID;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getFile() {
        return file;
    }

    public void setFile(List<String> file) {
        this.file = file;
    }
}
