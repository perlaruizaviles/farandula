package luisa

import (
	"farandula"
)

var currentGDS farandula.GDS

func SetGDS(gds farandula.GDS) {
	currentGDS = gds
}

func FindMeFlights(q farandula.GDSQuery) (farandula.GDSResult, error) {
	r, err := currentGDS.GetAvail(q)
	if err != nil {
		return nil, err
	}
	return r, nil
}
