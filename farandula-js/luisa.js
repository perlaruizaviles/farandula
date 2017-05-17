'use strict';

const SearchCommand = require("./search_command.js");

class Luisa {

    set flightManager(value) {
        this._flightManager = value;
    }

    constructor(){
        this._flightManager = null;
    }
    findMeFlights(){
        return new SearchCommand(this._flightManager);
    }

}


module.exports = new Luisa();