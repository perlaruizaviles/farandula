package farandula

type GDS interface {
	GetAvail(GDSQuery) (GDSResult, error)
	// Method for making reservation
	// Method for making cancellations
	// Method for making modifications
}
