package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/11/17.
 */
public class Fares {

    private Price basePrice;
    private Price taxesPrice;
    private Price totalPrice;

    private Price pricePerAdult;
    private Price taxPerAdult;

    private Price pricePerChild;
    private Price taxPerChild;

    private Price pricePerInfant;
    private Price taxPerInfant;

    public Price getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Price basePrice) {
        this.basePrice = basePrice;
    }

    public Price getTaxesPrice() {
        return taxesPrice;
    }

    public void setTaxesPrice(Price taxesPrice) {
        this.taxesPrice = taxesPrice;
    }

    public Price getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Price totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Price getPricePerAdult() {
        return pricePerAdult;
    }

    public void setPricePerAdult(Price pricePerAdult) {
        this.pricePerAdult = pricePerAdult;
    }

    public Price getTaxPerAdult() {
        return taxPerAdult;
    }

    public void setTaxPerAdult(Price taxPerAdult) {
        this.taxPerAdult = taxPerAdult;
    }

    public Price getPricePerChild() {
        return pricePerChild;
    }

    public void setPricePerChild(Price pricePerChild) {
        this.pricePerChild = pricePerChild;
    }

    public Price getTaxPerChild() {
        return taxPerChild;
    }

    public void setTaxPerChild(Price taxPerChild) {
        this.taxPerChild = taxPerChild;
    }

    public Price getPricePerInfant() {
        return pricePerInfant;
    }

    public void setPricePerInfant(Price pricePerInfant) {
        this.pricePerInfant = pricePerInfant;
    }

    public Price getTaxPerInfant() {
        return taxPerInfant;
    }

    public void setTaxPerInfant(Price taxPerInfant) {
        this.taxPerInfant = taxPerInfant;
    }

    @Override
    public String toString() {
        return "Fares{" +
                "basePrice=" + basePrice +
                '}';
    }
}
