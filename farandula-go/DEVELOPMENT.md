# Farandula-Go Development Notes

This document describes the process of porting
[Perla's](https://github.com/perlaruizaviles) Farandula-Java library to
the Go programming language.

## Sabre GDS information

## Account information

- Flight APIKEY: `V1:5wm4lfl2h9s4wgxw:DEVCENTER:EXT`
- Application: `Acuna's App`
- Key: `V1:5wm4lfl2h9s4wgxw:DEVCENTER:EXT`
- Shared Secret: `Y8gw3vVJ`
- Status: `active`
- Access Token: `T1RLAQJW7/1eIs1N9+Qabe/9WRY08Es/kBAEHqbYdfks1QOTEA5L/S4gAADAgXg4o/AYxXvNwX57AmErT2z8UjlEeAX/FGNy3/vw5HgbwEuvyX1MbtUjnY86Egu9utWJP5V1oOoF7vJitpTTC6mf5ZS5MMypcofrnXiBvv+ZbyEch6ptFZqxPVKONXKxLoDwhcCf82LgdklqwOlOVtEuun0+KzI74yUCbwMM+loXs713H2d4uwqNhMnzb/O3P6XVOzIz4UqTxd2giM9FcJ2KDN+fnRGQmYKel+GHxsCR6ImuRWCLs6VwYu2Z4KZF`

## Authentication

Follow [this instructions](https://developer.sabre.com/docs/rest_basics/authentication) and read the `GetToken` method implemented for the `SabreClient` type
at `gds/sabre.go`

In `farandula-java` for each API request a new access token is generated,
Sabre recommends generating only one token and handling it's expiration.
Perla told me that for the Sabre API the access token is unnecessary and
that it works with the `base64(base64(key)+":"+base64(secret))` as the
Authentication header.

## Air Search

- Endpoint Name: `Bargain Finder Max`
- Method: `POST`
- URL: `v3.1.0/shop/flights?mode=live`
- Description: `Finds the lowest available priced itineraries based upon a travel date.`

### Example

- Parameters:

| Parameter    | Value            | Type    | Description                                                                       |
| ------------ | ---------------- | ------- | --------------------------------------------------------------------------------- |
| mode         | live             | string  | Mode                                                                              |
| limit        | 50               | string  | The number of itineraries to retrieve per request                                 |
| offset       | 1                | integer | The starting position in the list of all itineraries that meet the query criteria |
| view         |                  | string  | A Sabre response view definition                                                  |
| Content-Type | application/json | string  | Content type of the payload                                                       |

- Request URI

`https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1`

- Request Headers

```
Authorization: Bearer T1RLAQJW7/1eIs1N9+Qabe/9WRY08Es/kBAEHqbYdfks1QOTEA5L/S4gAADAgXg4o/AYxXvNwX57AmErT2z8UjlEeAX/FGNy3/vw5HgbwEuvyX1MbtUjnY86Egu9utWJP5V1oOoF7vJitpTTC6mf5ZS5MMypcofrnXiBvv+ZbyEch6ptFZqxPVKONXKxLoDwhcCf82LgdklqwOlOVtEuun0+KzI74yUCbwMM+loXs713H2d4uwqNhMnzb/O3P6XVOzIz4UqTxd2giM9FcJ2KDN+fnRGQmYKel+GHxsCR6ImuRWCLs6VwYu2Z4KZF
X-Originating-Ip: 189.173.113.176
Content-Type: application/json
```

- Request Body

See `data/sabre-request.json`

- Response Status

`200 OK`

- Response Headers

```
Server: Apache-Coyote/1.1
Conversation-Id: sur7bnz7p
Date: Wed, 10 May 2017 19:44:57 GMT
Envtype: certification
Limit: 5
Message-Id: NvqBFP,htuc8v1lt
Mode: live
Offset: 1
Proxy-Client-Data: 23.23.79.231:39653
X-Originating-Ip: 189.173.113.176
X-Provider-Instance-Id: raf-darhlc006-9080
Content-Type: application/json;charset=UTF-8
Content-Length: 94473
```

- Response Body

See `data/sabre-response.json`

## Travel Query Language Quasi-Specification

The idea is to be able to specify a travel query a la SQL, a proof of
concept grammar, lexer and parser are implemented in `farandula/query_*.go`.

There are some naming consistency issues, i.e. traveler vs passenger that must be
resolved before continuing the development of TQL.

### Context-Free Grammar

The tokenization and parsing of terminals like Traveler and Cabin types
aren't specified in the current grammar spec.

```
               <Search> :: SEARCH <FlightSearch> <EOF>
         <FlightSearch> :: FLIGHTS <FlightDescription>
    <FlightDescription> :: <ScheduleDescription> <PassengersDescription>
                        || <PassengersDescription> <ScheduleDescription>
<PassengersDescription> :: FOR <PassengersList> IN <Cabin>
       <PassengersList> :: <PassengerCount>
                        || <PassengerCount> <PassengersListVarious>
<PassengersListVarious> :: , <PassengerCount> <PassengersListVarious>
                        || AND <PassengerCount>
       <PassengerCount> :: <Number> <Passenger>
  <ScheduleDescription> :: <ScheduleFlight>
                        || <ScheduleFlight> <ScheduleListVarious>
       <ScheduleFlight> :: FROM <AirportCode> TO <AirportCode> DEPARTING <Date>
  <ScheduleListVarious> :: THEN RETURNING <Date>
                        || THEN <ScheduleDescription>
```

### Examples

```go
farandula.Query(`
    search flights from DFW to CDG departing 2017-07-09 for 1 Adult in Economy
`)
```

```go
farandula.Query(`
    search flights from DFW to CDG departing 2017-07-09 then returning 2017-07-11 for
    2 Adults and 3 Children in Economy
`)
```

```go
farandula.Query(`
    search flights from DFW to CDG departing 2017-07-09 then from CDG to HDR departing 2017-07-11
    for 3 Seniors and 1 Child in First Class
`)
```

```go
farandula.Query(`
    search flights for 3 Infants, 2 Seniors and 1 Adult from DFW to CDG departing 2017-07-09 then
    from CDG to HDR departing 2017-07-11 then returning 2017-07-15
`)
```

```go
farandula.Queryf(`
    search flights for %d %s, %d %s and 1 Adult from DFW to CDG departing 2017-07-09 then
    from CDG to HDR departing 2017-07-11 then returning %s
`, 3, "Infants", 2, "Seniors", "2017-07-15")
```

### About error reporting and parsing recovery

The way the parser and lexer are implemented allow introducing
better error reporting and recovery mechanisms, but these aren't
implemented in this draft code.

For error reporting we could have a `column` field to complement
the `line` number projecting the `pos` index to a more readable
domain.

For the recovery mechanism, each `err` check has access to the
entire parsing and lexing state, so instead of propagating the
error message one could continue the parsing in a smart way.