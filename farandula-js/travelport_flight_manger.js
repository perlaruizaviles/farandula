class TravelportFlightManager {

    constructor(search){
        this._search = search;
    }


    getAvail(){

        let response = this.buildAndSendRequestForAvail(this._search);
        return [];
    }

    buildAndSendRequestForAvail(search) {
        //create SOAP envelope
        let envelope = this.buildEnvelopeStringFromSearch(search);

        // Send SOAP Message to SOAP Server
        let message = buildSOAPMessage(envelope);

        let url_api = "https://americas.universal-api.pp.travelport.com/B2BGateway/connect/uAPI/AirService";
        let soapResponse = sendRequest(message, url_api);

        return soapResponse;
    }

    buildEnvelopeStringFromSearch(search) {


    }
}

module.exports = TravelportFlightManager;