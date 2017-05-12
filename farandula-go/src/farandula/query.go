package farandula

import (
	"fmt"
	"time"
)

type Cabin int

const (
	Economy Cabin = iota
	PremiumEconomy
	First
	Business
	EconomyCoach
	Other
)

type Traveler int

const (
	Adult Traveler = iota
	Child
	Infant
	Senior
)

type FlightQuery struct {
	From string
	To   string
	At   time.Time
}

type ItineraryQuery []FlightQuery

type TravelerQuery map[Traveler]int

type GDSQuery struct {
	Itinerary ItineraryQuery
	Travelers TravelerQuery
	Cabin     Cabin
}

func NewGDSQuery() *GDSQuery {
	q := &GDSQuery{}
	q.Travelers = make(map[Traveler]int)
	q.Travelers[Adult] = 0
	q.Travelers[Child] = 0
	q.Travelers[Infant] = 0
	q.Travelers[Senior] = 0
	return q
}

func Query(qs string) (*GDSQuery, error) {
	query := NewGDSQuery()
	lex := newQueryLexer("inline query", qs)
	par := newQueryParser(lex, query)
	err := par.topLevel()
	if err != nil {
		return nil, err
	}
	return par.query, nil
}

func Queryf(format string, args ...interface{}) (*GDSQuery, error) {
	qs := fmt.Sprintf(format, args...)
	return Query(qs)
}
