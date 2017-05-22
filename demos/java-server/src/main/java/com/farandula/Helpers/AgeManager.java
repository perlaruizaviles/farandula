package com.farandula.Helpers;

/**
 * Created by emote on 22/05/17.
 */
public class AgeManager {
    private int numberAdults;
    private int [] childAges;
    private int [] infantAges;
    private int [] infantOnSeatAges;

    public int getNumberAdults() {
        return numberAdults;
    }

    public void setNumberAdults(int numberAdults) {
        this.numberAdults = numberAdults;
    }

    public int[] getChildAges() {
        return childAges;
    }

    public void setChildAges(int[] childAges) {
        this.childAges = childAges;
    }

    public int[] getInfantAges() {
        return infantAges;
    }

    public void setInfantAges(int[] infantAges) {
        this.infantAges = infantAges;
    }

    public int[] getInfantOnSeatAges() {
        return infantOnSeatAges;
    }

    public void setInfantOnSeatAges(int[] infantOnSeatAges) {
        this.infantOnSeatAges = infantOnSeatAges;
    }
}
