package com.farandula.models;

import com.nearsoft.farandula.models.Price;

/**
 * Created by enrique on 18/05/17.
 */
public class ItineraryFares {
    private Price basePrice;
    private Price taxesPrice;
    private Price totalPrice;

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
}
