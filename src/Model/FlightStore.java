package Model;

import Model.Flight;

public interface FlightStore {
    public Iterable<Flight> flights();
}
