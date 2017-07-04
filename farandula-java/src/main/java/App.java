import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightConnector;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.Passenger;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        //TODO this should be created using a fancy pattern ,  like IoC or factory or whatever
        final SabreFlightConnector sabre = new SabreFlightConnector();



        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);


        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );


        List<Itinerary> flightList = Luisa.using(sabre).findMeFlights()

                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(50).execute();


        System.out.println(flightList);

    }
}
