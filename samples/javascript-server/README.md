# JavaScript back-end

Since Farandula NodeJs is still in early development, this back-end is a neat and practical implementation of how the structure should work. Further and more complex documentation should be added in near future.

## Node Modules required

For the node back-end two modules are used:
- Hapi:

  Framework used to build applications and services. 
  ```
  $ npm install hapi
  ```
- Wreck:

  An HTTP Client utilities framework.
  ```
  $ npm install wreck
  ```
  
## Configuring Hapi server
```
server.connection({
  host: 'localhost',
  port: process.env.PORT || 8000
});
```

## Routes
This back-end contains two routes:

- `/api/airports`
- `/api/flights`



### End-point for airports

This end-point receives the parameter `pattern` and redirects it to other server that handles this request and returns the desired data. Finally this data is sent back to the client that made the request. 

Here's the code:

```
server.route({
  method: 'GET',
  path: api.path + '/airports',
  handler: function (request, reply) {
    let params = request.query;
    Wreck.get(farandulaApi.baseURL + farandulaApi.airports + '?pattern=' + params["pattern"], (err, payload, res) => {
      reply('' + res);
    });
  }
});
```
### End-point for flights
This end-point receives the next parameters:

- departingAirportCodes
- departingDates
- arrivalAirportCodes
- type
- passenger
- cabin
- limit

When searching for round trips two more parameters are added:

- returnDates
- returnTimes

The parameters as the `api/airports` end-point are redirected to other server that handles the request and returns the desired data and finally is sent to the client that made the request to this end-point.

Here's the code:

```
server.route({
  method: 'GET',
  path: api.path + '/flights',
  handler: function (request, reply) {
    let params = request.query;
    let baseURLRequest = farandulaApi.baseURL + farandulaApi.flights + '?'
      + 'departingAirportCodes=' + params["departingAirportCodes"]
      + '&departingDates=' + params["departingDates"]
      + '&departingTimes=' + params["departingTimes"]
      + '&arrivalAirportCodes=' + params["arrivalAirportCodes"]
      + '&type=' + params["type"]
      + '&passenger=' + params["passenger"]
      + '&cabin=' + params["cabin"]
      + '&limit' + params["limit"];

    if (params["type"] === "roundTrip") {
      baseURLRequest += '&returnDates=' + params["returnDates"] + '&returnTimes=' + params["returnTimes"];
    }

    Wreck.get(baseURLRequest,
      (err, payload, res) => {
        reply('' + res);
      });
  }
});
```

## Starting the server
```
server.start();
```
