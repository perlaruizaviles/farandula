import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;
import com.nearsoft.farandula.Creds;
import com.nearsoft.farandula.FarandulaException;
import com.nearsoft.farandula.TripManager;
import com.nearsoft.farandula.models.Passenger;
import com.nearsoft.farandula.models.SearchCommand;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;

public class App {

    public static void main(String[] args) throws FarandulaException, IOException {
        final Properties props = new Properties();
        props.load(TripManager.class.getResourceAsStream("/config.properties"));

        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        final TripManager tripManager = new TripManager(creds);

        TripManager.setSupplier(() -> tripManager );

        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        tripManager.getAvail(
                new SearchCommand() .from("DFW")
                        .to("CDG")
                        .departingAt ( departingDate)
                        .returningAt( returningDate)
                        .forPassegers(Passenger.adults(1) )
                        .type( "roundTrip")
                        .sortBy( PRICE,MINSTOPS )
                        .limitTo(50)
        );
    }
}
