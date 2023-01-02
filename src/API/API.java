package API;

import static spark.Spark.get;
import static spark.Spark.initExceptionHandler;
import Model.Flight;
import Model.Histogram;
import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class API {
    private static Histogram<Object> histogram;
    private static Iterable<Flight> flights;
    
    public static void createAPI(Iterable<Flight> flights) {
        API.flights = flights;
        initExceptionHandler((e) -> System.out.println("Initialization failed"));
        get("/:dimension", (req, res) -> constructString(req.params(":dimension"), null));
        get("/:dimension/:bin", (req, res) -> constructString(req.params(":dimension"), req.params(":bin")));
    }
    
    private static String constructString(String dimension, String bin) {
        String result = "{\"dimension\": \""+dimension+"\",\"binsize\": "+bin+",\"values\": [";
        try {
            histogram(dimension.toLowerCase(), bin != null ? Integer.parseInt(bin) : null);
        } catch (Exception e) {
            return e.getMessage();
        }
        for (Object key : sortKeys(histogram.getKeySet(), dimension)) result += "{\""+String.valueOf(key).toLowerCase()+"\": "+histogram.getValue(key)+"},";
        return result.substring(0, result.length()-1) + "]}";
    }
    
    private static TreeSet<Object> sortKeys(Set<Object> keySet, String dimension) {
        if(dimension.equals("day_of_week")) {
            TreeSet<Object> daysOfWeekSet = new TreeSet<>(new DayOfWeekComparator());
            for (Object key : keySet) 
                daysOfWeekSet.add(key);
            return daysOfWeekSet;
        }
        return new TreeSet<>(keySet);
    }
    
    private static class DayOfWeekComparator implements Comparator<Object> {
        @Override
        public int compare(Object day1, Object day2) {
            DayOfWeek dow1 = DayOfWeek.valueOf((String) day1);
            DayOfWeek dow2 = DayOfWeek.valueOf((String) day2);
            return dow1.compareTo(dow2);
        }
    }
    
    private static void histogram(String dimension, Integer bin) throws Exception {
        histogram = new Histogram<>();
        switch(dimension) {
            case "day_of_week":
                for (Flight flight : flights) 
                    histogram.increment(bin == null ? flight.getDayOfWeek().toString() : flight.getDayOfWeek().getValue() / bin);
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
