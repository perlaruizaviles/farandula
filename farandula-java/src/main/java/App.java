import java.io.IOException;
import java.util.Properties;
import com.nearsoft.farandula.Creds;
import com.nearsoft.farandula.FarandulaException;
import com.nearsoft.farandula.TripManager;

public class App {

    public static void main(String[] args) throws FarandulaException, IOException {
        final Properties props = new Properties();
        props.load(TripManager.class.getResourceAsStream("/config.properties"));

        final Creds creds = new Creds(props.getProperty("sabre.client_id"), props.getProperty("sabre.client_secret"));
        final TripManager tripManager = new TripManager(creds);
        tripManager.getAvail();
    }
}
