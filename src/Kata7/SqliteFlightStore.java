package Kata7;

import Model.FlightStore;
import Model.Flight;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import static java.util.Collections.*;
import java.util.Iterator;

public class SqliteFlightStore implements FlightStore {
    private final File file;
    private final Connection connection;

    public SqliteFlightStore(File file) throws SQLException {
        this.file = file;
        connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
    }

    @Override
    public Iterable<Flight> flights() {
        return new Iterable<Flight>() {
            @Override
            public Iterator<Flight> iterator() {
                try {
                    return createIterator();
                } catch (SQLException ex) {
                    return emptyIterator();
                }
            }
        };
    }
    
    private Iterator<Flight> createIterator() throws SQLException {
        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM flights");
        return new Iterator<Flight>() {
            Flight nextFlight = nextFlight();
            
            @Override
            public boolean hasNext() {
                return nextFlight != null;
            }

            @Override
            public Flight next() {
                Flight flight = nextFlight;
                try {
                    nextFlight = nextFlight();
                } catch (SQLException ex) {
                    nextFlight = null;
                }
                return flight;
            }

            private Flight nextFlight() throws SQLException {
                if(!nextRegister()) return null;
                return new Flight(
                        DayOfWeek.of(rs.getInt("DAY_OF_WEEK")),
                        timeOf(rs.getInt("DEP_TIME")),
                        rs.getInt("DEP_DELAY"),
                        timeOf(rs.getInt("ARR_TIME")),
                        rs.getInt("ARR_DELAY"),
                        rs.getInt("CANCELLED") == 1,
                        rs.getInt("DIVERTED") == 1,
                        rs.getInt("AIR_TIME"),
                        rs.getInt("DISTANCE")
                );
            }
            
            private LocalTime timeOf(int t) {
                return LocalTime.of(t/100 % 24, t%100);
            }

            private boolean nextRegister() {
                try {
                    if(rs.next()) return true;
                    rs.close();
                } catch (SQLException ex) {}
                return false;
            }
        };
    }
}
