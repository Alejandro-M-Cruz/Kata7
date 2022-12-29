package Model;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Flight {
    private final DayOfWeek dayOfWeek;
    private final LocalTime departureTime;
    private final int departureDelay;
    private final LocalTime arrivalTime;
    private final int arrivalDelay;
    private final boolean cancelled;
    private final boolean diverted;
    private final int airTime;
    private final int distance;


    public Flight(DayOfWeek dayOfWeek, LocalTime departureTime, int departureDelay, LocalTime arrivalTime, int arrivalDelay, boolean cancelled, boolean diverted, int airTime, int distance) {
        this.dayOfWeek = dayOfWeek;
        this.departureTime = departureTime;
        this.departureDelay = departureDelay;
        this.arrivalTime = arrivalTime;
        this.arrivalDelay = arrivalDelay;
        this.cancelled = cancelled;
        this.diverted = diverted;
        this.airTime = airTime;
        this.distance = distance;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public int getDepartureDelay() {
        return departureDelay;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getArrivalDelay() {
        return arrivalDelay;
    }

    public boolean getCancelled() {
        return cancelled;
    }

    public boolean getDiverted() {
        return diverted;
    }

    public int getAirTime() {
        return airTime;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Flight{" + "dayOfWeek=" + dayOfWeek + ", departureTime=" + departureTime + ", departureDelay=" + departureDelay + ", arrivalTime=" + arrivalTime + ", arrivalDelay=" + arrivalDelay + ", cancelled=" + cancelled + ", diverted=" + diverted + ", airTime=" + airTime + ", distance=" + distance + '}';
    }
    
}
