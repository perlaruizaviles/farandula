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

Follow [this instructions](https://developer.sabre.com/docs/rest_basics/authentication) and read the `generateToken` method implemented for the `SabreGDS` type
at `farandula/sabre_gds.go`

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

### Context-Free Grammar

The tokenization and parsing of terminals like Traveler and Cabin types
aren't specified in the current grammar spec.

```
      <SearchTopLevel> :: search <FlightTopLevel>
     <FlightsTopLevel> :: flights <FlightsDescription>
  <FlightsDescription> :: from <ScheduleDescription> for <TravelersDescription>
                       || for <TravelersDescription> from <ScheduleDescription>
 <ScheduleDescription> :: <FlightDescription> <ScheduleRest>
   <FlightDescription> :: <AirportCode> to <AirportCode> departing <Date>
        <ScheduleRest> :: then <ScheduleContinue>
                       || [epsilon]
    <ScheduleContinue> :: from <ScheduleDescription>
                       || returning <Date>
<TravelersDescription> :: <TravelerDescription> <TravelersRest> in <Cabin>
 <TravelerDescription> :: <Number> <Traveler>
       <TravelersRest> :: , <TravelerDescription> <TravelersForceRest>
                       || and <TravelerDescription>
                       || [epsilon]
  <TravelersForceRest> :: , <TravelerDescription> <TravelersForceRest>
                       || and <TravelerDescription>
```

In this grammar `[epsilon]` means that not matching any pattern is a
success for the non-terminal. For example in `<ScheduleRest>` if no
`then` token is found, means that `<ScheduleDescription>` (which produces
the `<ScheduleRest>`) is a success, that is, the partial match of one
`<FlightDescription>` is enough for making a `<ScheduleDescription>`.
The idea behind this empty productions is to signal that the production
is correct even though no more productions could be generated.

The use of empty productions can make recursive descent parsers fail, the
trick in designing the grammar is to let the parser be sure of what path
to follow by just looking at the next token. With the use of empty
productions, if the next token doesn't match what the parser expects, it
won't get removed from the parser cache and will be able to get consumed
by another rule. (the parser is LL(1))

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
    search flights from DFW to CDG departing 2017-07-09 then from CDG to LHR departing 2017-07-11
    for 3 Seniors and 1 Child in First Class
`)
```

```go
farandula.Query(`
    search flights for 3 Infants, 2 Seniors and 1 Adult from DFW to CDG departing 2017-07-09 then
    from CDG to LHR departing 2017-07-11 then returning 2017-07-15
`)
```

```go
farandula.Queryf(`
    search flights for %d %s, %d %s and 1 Adult from DFW to CDG departing 2017-07-09 then
    from CDG to LHR departing 2017-07-11 then returning %s
`, 3, "Infants", 2, "Seniors", "2017-07-15")
```

### About error reporting and parsing recovery

The way the parser and lexer are implemented allow introducing
better error reporting and recovery mechanisms, but these aren't
implemented in this draft code.

For error reporting we could compute a `column` to complement
the `line` number by projecting the `pos` index to a more readable
domain.

For the recovery mechanism, each `err` check has access to the
entire parsing and lexing state, so instead of propagating the
error message one could continue the parsing in a smart way.

#### Example of error recovery

Suppose the user writes the following query

`search flights from DFW to CDG departing 2017-01-01 returning 2017 for 1 Adult`

The query doesn't satisfy the grammar, we can infer that the user wants
a round trip but forgot to write `then` before the `returning` keyword.
Well in the `par.scheduleRest` method we expect to see `then` as the next
token, and in the first `if err != nil` block we express that no `then`
was found but that the schedule description is complete.

Let's change that by checking if the next token is `returning`, in which
case we proceed with the `scheduleContinue` method:

```go
if par.storedToken.typ != tokenReturning {
    return nil // next token isn't `returning` so we do the right thing with the [epsilon]
}
err = par.scheduleContinue()
if err != nil { // if we guessed wrong the user's intentions
    return err // return an error, the [epsilon] production wasn't intended by the user query
}
return nil
```

If you want to corroborate that this will work, I left the previous
error recovery in the `src/farandula/query_parser.go` file as a comment.
Just uncomment the code and run the tests, try to change the test
suite in `query_test.go` to see that the error get's handled gracefully.