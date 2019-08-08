package com.angelova.w510.radixapp.models;

public class InvoiceData {
    private String invoicedEntity;
    private String contactPerson;
    private String phone;
    private String address;
    private String vatNumber;
    private String uniqueIdCode;
    //TODO: check downPayment and downPaymentPercentage
    private int downPayment;
    private String invoiceLanguage;
    private String invoiceCurrency;

    public InvoiceData() {

    }

    public String getInvoicedEntity() {
        return invoicedEntity;
    }

    public void setInvoicedEntity(String invoicedEntity) {
        this.invoicedEntity = invoicedEntity;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public String getUniqueIdCode() {
        return uniqueIdCode;
    }

    public void setUniqueIdCode(String uniqueIdCode) {
        this.uniqueIdCode = uniqueIdCode;
    }

    public int getDownPayment() {
        return downPayment;
    }

    public void setDownPayment(int downPayment) {
        this.downPayment = downPayment;
    }

    public String getInvoiceLanguage() {
        return invoiceLanguage;
    }

    public void setInvoiceLanguage(String invoiceLanguage) {
        this.invoiceLanguage = invoiceLanguage;
    }

    public String getInvoiceCurrency() {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency) {
        this.invoiceCurrency = invoiceCurrency;
    }
}
