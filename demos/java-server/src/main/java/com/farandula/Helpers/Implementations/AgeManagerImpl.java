package com.farandula.Helpers.Implementations;

import com.farandula.Helpers.AgeManager;
import org.springframework.stereotype.Component;

/**
 * Created by Admin on 6/5/17.
 */
@Component
public class AgeManagerImpl implements AgeManager{


    int numberAdults;
    int [] childAges;
    int [] infantAges;
    int [] infantOnSeatAges ;

    public int getNumberAdults() { return numberAdults; }

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
