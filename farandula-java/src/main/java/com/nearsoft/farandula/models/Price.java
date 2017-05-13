package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/11/17.
 */
public class Price {

    private double basePrice;
    private double taxesPrice;
    private double totalPrice;

    private double pricePerAdult;
    private double taxPerAdult;

    private double pricePerChild;
    private double taxPerChild;

    private double pricePerInfant;
    private double taxPerInfant;

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public double getTaxesPrice() {
        return taxesPrice;
    }

    public void setTaxesPrice(double taxesPrice) {
        this.taxesPrice = taxesPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getPricePerAdult() {
        return pricePerAdult;
    }

    public void setPricePerAdult(double pricePerAdult) {
        this.pricePerAdult = pricePerAdult;
    }

    public double getTaxPerAdult() {
        return taxPerAdult;
    }

    public void setTaxPerAdult(double taxPerAdult) {
        this.taxPerAdult = taxPerAdult;
    }

    public double getPricePerChild() {
        return pricePerChild;
    }

    public void setPricePerChild(double pricePerChild) {
        this.pricePerChild = pricePerChild;
    }

    public double getTaxPerChild() {
        return taxPerChild;
    }

    public void setTaxPerChild(double taxPerChild) {
        this.taxPerChild = taxPerChild;
    }

    public double getPricePerInfant() {
        return pricePerInfant;
    }

    public void setPricePerInfant(double pricePerInfant) {
        this.pricePerInfant = pricePerInfant;
    }

    public double getTaxPerInfant() {
        return taxPerInfant;
    }

    public void setTaxPerInfant(double taxPerInfant) {
        this.taxPerInfant = taxPerInfant;
    }
}
