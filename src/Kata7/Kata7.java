package Kata7;

import static API.API.createAPI;
import java.io.File;
import java.sql.SQLException;

public class Kata7 {

    public static void main(String[] args) throws SQLException {
        SqliteFlightStore flightStore = new SqliteFlightStore(new File("flights.db"));
        createAPI(flightStore.flights());
    }
    
}
