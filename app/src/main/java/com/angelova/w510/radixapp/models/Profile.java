package com.angelova.w510.radixapp.models;

import java.util.List;

/**
 * Created by W510 on 21.1.2018 г..
 */

public class Profile {
    private String name;
    private String email;
    private String password;
    private String token;
    private String userId;

    private List<Offer> offers;
    private List<Order> orders;

    private int unpaidInvoicesCount;
    private int paidInvoicesCount;
    private int requestedInvoicesCount;
    private int deliveredInvoicesCount;
    private List<Invoice> invoices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public int getUnpaidInvoicesCount() {
        return unpaidInvoicesCount;
    }

    public void setUnpaidInvoicesCount(int unpaidInvoicesCount) {
        this.unpaidInvoicesCount = unpaidInvoicesCount;
    }

    public int getPaidInvoicesCount() {
        return paidInvoicesCount;
    }

    public void setPaidInvoicesCount(int paidInvoicesCount) {
        this.paidInvoicesCount = paidInvoicesCount;
    }

    public int getRequestedInvoicesCount() {
        return requestedInvoicesCount;
    }

    public void setRequestedInvoicesCount(int requestedInvoicesCount) {
        this.requestedInvoicesCount = requestedInvoicesCount;
    }

    public int getDeliveredInvoicesCount() {
        return deliveredInvoicesCount;
    }

    public void setDeliveredInvoicesCount(int deliveredInvoicesCount) {
        this.deliveredInvoicesCount = deliveredInvoicesCount;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
