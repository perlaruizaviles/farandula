package com.nearsoft.farandula;

import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.SearchCommand;

/**
 * Created by pruiz on 4/10/17.
 */
public class Luisa {
    public static SearchCommand findMeFlights() {
        return new SearchCommand();
    }
    public static BookCommand bookMeThisFligth(Flight flight) {
        return new BookCommand();
    }


    private static class BookCommand extends SearchCommand {
    }
}
