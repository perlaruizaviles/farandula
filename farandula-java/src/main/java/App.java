import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;

import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Passenger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;

public class App {

    public static void main(String[] args) throws FarandulaException, IOException {
        final SabreFlightManager tripManager = new SabreFlightManager();

        Luisa.setSupplier(() -> tripManager);

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        List<AirLeg> flightList = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(returningDate)
                .forPassegers(Passenger.adults(1))
                .type("roundTrip")
                .sortBy(PRICE, MINSTOPS)
                .limitTo(50).execute();

        System.out.println(flightList);

    }
}
