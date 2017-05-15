package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/14/17.
 */
public class Price {

    private double amount;
    private String currencyCode;

    public Price(double amount, String currencyCode) {
        this.amount = amount;
        this.currencyCode = currencyCode;
    }

    public Price() {

    }

    public Price setAmount(double amount) {
        this.amount = amount;
        return this;
    }


    public Price setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
}
