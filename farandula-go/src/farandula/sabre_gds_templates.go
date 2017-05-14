package farandula

import (
	"fmt"
	"text/template"
	"bytes"
)

var sabreRequestString = `
{
  "OTA_AirLowFareSearchRQ": {
    "Target": "Production",
    "POS": {
      "Source": [{
        "PseudoCityCode":"F9CE",
          "RequestorID": {
            "Type": "1",
            "ID": "1",
            "CompanyName": {
            }
          }
        }
      ]
    },
    "OriginDestinationInformation": [
    {{range $id, $flight := .Itinerary}}
    {{if gt $id 0}} , {{end}} {
      "RPH": "{{add1 $id}}",
      "DepartureDateTime": "{{$flight.At.Format "2006-01-02"}}T11:00:00",
      "OriginLocation": {
        "LocationCode": "{{$flight.From}}"
      },
      "DestinationLocation": {
        "LocationCode": "{{$flight.To}}"
      },
      "TPA_Extensions": {
        "SegmentType": {
          "Code": "O"
        }
      }
    } {{end}}
    ],
    "TravelPreferences": {
      "ValidInterlineTicket": true,
      "CabinPref": [{
        "Cabin": "{{encodeCabin .Cabin}}",
        "PreferLevel": "Preferred"
      }],
      "TPA_Extensions": {
        "TripType": {
          "Value": "Return"
        },
        "LongConnectTime": {
          "Min": 780,
          "Max": 1200,
          "Enable": true
        },
        "ExcludeCallDirectCarriers": {
          "Enabled": true
        }
      }
    },
    "TravelerInfoSummary": {
      "SeatsRequested": [{{countTravelers .Travelers}}],
      "AirTravelerAvail": [{
        "PassengerTypeQuantity": [{
          "Code": "ADT",
          "Quantity": {{countTravelers .Travelers}}
        }]
      }]
    },
    "TPA_Extensions": {
      "IntelliSellTransaction": {
        "RequestType": {
          "Name": "50ITINS"
        }
      }
    }
  }
}
`

func add1(n int) string {
	return fmt.Sprintf("%d", n+1)
}

func encodeCabin(cabin Cabin) string {
	var code string
	switch cabin {
	case Economy:
		code = "Y"
	case First:
		code = "F"
	default:
		code = "Y"
	}
	return code
}

func countTravelers(travelers TravelerQuery) string {
	var count int
	for _, v := range travelers {
		count += v
	}
	return fmt.Sprintf("%d", count)
}

func (sabre *SabreGDS) generateSabreRequestTemplate() error {
	fnmap := template.FuncMap{
		"add1":           add1,
		"encodeCabin":    encodeCabin,
		"countTravelers": countTravelers,
	}
	templ, err := template.New("sabre request").Funcs(fnmap).Parse(sabreRequestString)
	if err != nil {
		return err
	}
	sabre.template = templ
	return nil
}

func (sabre *SabreGDS) fillSabreRequestTemplate(query *GDSQuery) (*bytes.Buffer, error) {
	buff := bytes.NewBuffer([]byte{})
	err := sabre.template.Execute(buff, query)
	if err != nil {
		return nil, err
	}
	return buff, nil
}
