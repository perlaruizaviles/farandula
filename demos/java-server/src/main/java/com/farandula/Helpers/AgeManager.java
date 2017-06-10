package com.farandula.Helpers;

/**
 * Created by emote on 22/05/17.
 */

public interface AgeManager {

    int getNumberAdults();

    void setNumberAdults(int numberAdults);

    int[] getChildAges();

    void setChildAges(int[] childAges);

    int[] getInfantAges();

    void setInfantAges(int[] infantAges);

    int[] getInfantOnSeatAges();

    void setInfantOnSeatAges(int[] infantOnSeatAges);

}
