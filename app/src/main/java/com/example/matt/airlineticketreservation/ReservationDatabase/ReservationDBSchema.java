package com.example.matt.airlineticketreservation.ReservationDatabase;
//same as others but for reservations
public class ReservationDBSchema {
    public static final class ReservationTable{
        public static final String NAME = "RESERVATIONS";

        public static final class Cols {
            public static final String UUID              = "uuid";
            public static final String RESERVATIONNUMBER = "reservation_number";
            public static final String FLIGHTNUMBER      = "flight_number";
            public static final String USERNAME          = "username";
            public static final String DEPARTURE         = "departure";
            public static final String ARRIVAL           = "arrival";
            public static final String TIME              = "time_departure";
            public static final String NUMOFSEATS        = "number_of_seats";
            public static final String PRICE             = "price";
//            public static final String DATECREATED  = "date_created";
//            public static final String TIMECREATED  = "time_created";
        }
    }
}
