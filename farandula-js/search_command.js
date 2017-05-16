
class SearchCommand{
   constructor(flightManager){
       this._flightManager = flightManager;
       this._departureAirportCode = null;
       this._arrivalAirportCode = null;
       this._departingAt = null;
       this._departingDate = null;
       this._returningDate = null;
       this._limitTo = null;
   }

    from (airportCode){
        this._departureAirportCode = airportCode;
        return this;
    }

    to (airportCode){
        this._arrivalAirportCode = airportCode;
        return this;
    }
    departingAt(departing){
        this._departingDate = departing;
        return this;
    }

    returningAt(returning){
        this._returningDate = returning;
        return this;
    }

    limitTo(limitCount){
        this._limitTo = limitCount;
        return this;
    }
    execute(){
        return this._flightManager
            .getAvail(this);
    }


};

module.exports = SearchCommand;

