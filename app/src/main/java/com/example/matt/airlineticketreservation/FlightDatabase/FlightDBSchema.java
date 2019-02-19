package com.example.matt.airlineticketreservation.FlightDatabase;
//SChema for flight
public class FlightDBSchema {

    public static final class FlightTable{
        public static final String NAME = "FLIGHTS";

        public static final class Cols {
            public static final String UUID         = "uuid";
            public static final String FLIGHTNUMBER = "flight_number";
            public static final String DEPARTURE    = "departure";
            public static final String ARRIVAL      = "arrival";
            public static final String TIME         = "time_departure";
            public static final String NUMOFSEATS   = "number_of_seats";
            public static final String PRICE        = "price";
//            public static final String DATECREATED  = "date_created";
//            public static final String TIMECREATED  = "time_created";
        }
    }
}
