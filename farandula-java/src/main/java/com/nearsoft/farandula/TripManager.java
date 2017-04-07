package com.nearsoft.farandula;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TripManager {

    private final OkHttpClient.Builder _builder = new OkHttpClient.Builder();
    private final AccessManager _accessManager;

    public TripManager(AccessManager accessManager) {
        _accessManager = accessManager;
    }

    public TripManager(Creds creds) {
        _accessManager = new AccessManager(creds);
    }

    public void getAvail() throws FarandulaException {
        try {
            final Response response = buildHttpClient().newCall(buildRequestForAvail()).execute();
            buildAvailResponse(response);
        } catch (IOException e) {
            throw new FarandulaException(e, ErrorType.AVAILABILITY_ERROR, "error retrieving availability");
        }
    }

    private void buildAvailResponse(Response response) throws IOException {
        System.out.println(response.body().string());
    }

    private Request buildRequestForAvail() {
        final Request.Builder builder = new Request.Builder();
        builder.url("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1");
        builder.post(RequestBody.create(MediaType.parse("application/json"), s));
        return builder.build();
    }

    private OkHttpClient buildHttpClient() throws FarandulaException {
        if (_builder.interceptors().isEmpty()) {
            _builder.addInterceptor(new AuthInterceptor(_accessManager.getAccessToken()));
            _builder.connectTimeout(1, TimeUnit.MINUTES);
            _builder.readTimeout(1, TimeUnit.MINUTES);
        }
        return _builder.build();
    }

    String s = "{\n" + "\n" + " \"OTA_AirLowFareSearchRQ\": {\n" + "\n" + "     \"Target\": \"Production\",\n" + "\n" + "       \"POS\": {\n" + "\n" +
        "            \"Source\": [{\n" + "\n" + "                \"PseudoCityCode\":\"F9CE\",\n" + "\n" + "                \"RequestorID\": {\n" +
        "\n" + "                    \"Type\": \"1\",\n" + "\n" + "                  \"ID\": \"1\",\n" + "\n" +
        "                    \"CompanyName\": {\n" + "\n" + "                        \n" + "\n" + "                  }\n" + "\n" +
        "             }\n" + "\n" + "         }]\n" + "\n" + "        },\n" + "\n" + "        \"OriginDestinationInformation\": [{\n" + "\n" +
        "          \"RPH\": \"1\",\n" + "\n" + "           \"DepartureDateTime\": \"2017-07-07T11:00:00\",\n" + "\n" +
        "           \"OriginLocation\": {\n" + "\n" + "             \"LocationCode\": \"DFW\"\n" + "\n" + "         },\n" + "\n" +
        "            \"DestinationLocation\": {\n" + "\n" + "                \"LocationCode\": \"CDG\"\n" + "\n" + "         },\n" + "\n" +
        "            \"TPA_Extensions\": {\n" + "\n" + "             \"SegmentType\": {\n" + "\n" + "                    \"Code\": \"O\"\n" + "\n" +
        "               }\n" + "\n" + "         }\n" + "\n" + "     },\n" + "\n" + "        {\n" + "\n" + "         \"RPH\": \"2\",\n" + "\n" +
        "           \"DepartureDateTime\": \"2017-07-08T11:00:00\",\n" + "\n" + "           \"OriginLocation\": {\n" + "\n" +
        "             \"LocationCode\": \"CDG\"\n" + "\n" + "         },\n" + "\n" + "            \"DestinationLocation\": {\n" + "\n" +
        "                \"LocationCode\": \"DFW\"\n" + "\n" + "         },\n" + "\n" + "            \"TPA_Extensions\": {\n" + "\n" +
        "             \"SegmentType\": {\n" + "\n" + "                    \"Code\": \"O\"\n" + "\n" + "               }\n" + "\n" + "         }\n" +
        "\n" + "     }],\n" + "\n" + "       \"TravelPreferences\": {\n" + "\n" + "          \"ValidInterlineTicket\": true,\n" + "\n" +
        "           \"CabinPref\": [{\n" + "\n" + "             \"Cabin\": \"Y\",\n" + "\n" + "             \"PreferLevel\": \"Preferred\"\n" + "\n" +
        "            }],\n" + "\n" + "           \"TPA_Extensions\": {\n" + "\n" + "             \"TripType\": {\n" + "\n" +
        "                   \"Value\": \"Return\"\n" + "\n" + "             },\n" + "\n" + "                \"LongConnectTime\": {\n" + "\n" +
        "                    \"Min\": 780,\n" + "\n" + "                 \"Max\": 1200,\n" + "\n" + "                    \"Enable\": true\n" + "\n" +
        "              },\n" + "\n" + "                \"ExcludeCallDirectCarriers\": {\n" + "\n" + "                  \"Enabled\": true\n" + "\n" +
        "             }\n" + "\n" + "         }\n" + "\n" + "     },\n" + "\n" + "        \"TravelerInfoSummary\": {\n" + "\n" +
        "            \"SeatsRequested\": [1],\n" + "\n" + "          \"AirTravelerAvail\": [{\n" + "\n" +
        "              \"PassengerTypeQuantity\": [{\n" + "\n" + "                 \"Code\": \"ADT\",\n" + "\n" +
        "                    \"Quantity\": 1\n" + "\n" + "               }]\n" + "\n" + "            }]\n" + "\n" + "        },\n" + "\n" +
        "        \"TPA_Extensions\": {\n" + "\n" + "         \"IntelliSellTransaction\": {\n" + "\n" + "             \"RequestType\": {\n" + "\n" +
        "                    \"Name\": \"50ITINS\"\n" + "\n" + "             }\n" + "\n" + "         }\n" + "\n" + "     }\n" + "\n" + " }\n" + "\n" +
        "}";
}