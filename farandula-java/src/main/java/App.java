import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.Passenger;

import java.time.LocalDateTime;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        final SabreFlightManager tripManager = new SabreFlightManager();

        Luisa.setSupplier(() -> tripManager);

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);


        List<Itinerary> flightList = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(50).execute();


        System.out.println(flightList);

    }
}
