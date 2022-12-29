package API;

import static spark.Spark.get;
import static spark.Spark.initExceptionHandler;
import Model.Flight;
import Model.Histogram;
import java.util.TreeSet;

public class API {
    private static Histogram<Object> histogram;
    private static Iterable<Flight> flights;
    
    public static void createAPI(Iterable<Flight> flights) {
        API.flights = flights;
        initExceptionHandler((e) -> System.out.println("Initialization failed"));
        get("/:dimension", (req, res) -> constructString(req.params(":dimension"), null, null));
        get("/:dimension/bin/:bin", (req, res) -> constructString(req.params(":dimension"), null, req.params(":bin")));
        get("/:dimension/:filter", (req, res) -> constructString(req.params(":dimension"), req.params(":filter"), null));
        get("/:dimension/:filter/bin/:bin", (req, res) -> constructString(req.params(":dimension"), req.params(":filter"), req.params(":bin")));
    }
    
    private static String constructString(String dimension, String filter, String bin) {
        String result = "{\"dimension\": \""+dimension+"\",\"filter\": \""+filter+"\",\"binsize\": \""+bin+"\",\"values\": {";
        try {
            histogram(dimension.toLowerCase(), filter, bin != null ? Integer.parseInt(bin) : null);
        } catch (Exception e) {
            return e.getMessage();
        }
        for (Object key : new TreeSet<>(histogram.getKeySet())) result += "\""+String.valueOf(key).toLowerCase()+"\": \""+histogram.getValue(key)+"\",";
        return result.substring(0, result.length()-1) + "}}";
    }
    
    private static void histogram(String dimension, String filter, Integer bin) throws Exception {
        histogram = new Histogram<>();
        switch(dimension) {
            case "day_of_week":
                for (Flight flight : flights) 
                    histogram.increment(flight.getDayOfWeek().toString());
                break;
            case "dep_time":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getDepartureTime().getHour() : flight.getDepartureTime().getHour() % (24/bin));
                break;
            case "dep_delay":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getDepartureDelay() : flight.getDepartureDelay() / bin);
                break;
            case "arr_time":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getArrivalTime().getHour() : flight.getArrivalTime().getHour() % (24/bin));
                break;
            case "arr_delay":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getArrivalDelay() : flight.getArrivalDelay() / bin);
                break;
            case "cancelled":
                for (Flight flight : flights) 
                    histogram.increment(flight.getCancelled());
                break;
            case "diverted":
                for (Flight flight : flights) 
                    histogram.increment(flight.getDiverted());
                break;
            case "air_time":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getAirTime() : flight.getAirTime() / bin);
                break;
            case "distance":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getDistance() : flight.getDistance() / bin);
                break;
            default:
                throw new Exception("Error: dimension not valid");
        }
    }
}
