package com.example.matt.airlineticketreservation.FlightDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.matt.airlineticketreservation.FlightDatabase.FlightDBSchema.FlightTable;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;
//Similar to CREATE ACCOUNT CURSOR WRAPER
public class FlightCursorWrapper extends CursorWrapper {

    public FlightCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Flight getFlight(){
        String uuidString = getString(getColumnIndex(FlightTable.Cols.UUID));
        String flightNumber = getString(getColumnIndex(FlightTable.Cols.FLIGHTNUMBER));
        String departure = getString(getColumnIndex(FlightTable.Cols.DEPARTURE));
        String arrival = getString(getColumnIndex(FlightTable.Cols.ARRIVAL));
        String time_of_departure = getString(getColumnIndex(FlightTable.Cols.TIME));
        int num_of_seats = getInt(getColumnIndex(FlightTable.Cols.NUMOFSEATS));
        double price = getDouble(getColumnIndex(FlightTable.Cols.PRICE));
//        long dateCreated = getLong(getColumnIndex(FlightTable.Cols.DATECREATED));
//        long timeCreated = getLong(getColumnIndex(FlightTable.Cols.TIMECREATED));

        Flight flight = new Flight(UUID.fromString(uuidString));
        flight.setFlightNumber(flightNumber);
        flight.setDeparture(departure);
        flight.setArrival(arrival);
        flight.setTime_Of_Departure(time_of_departure);
        flight.setNum_Of_Seats(num_of_seats);
        flight.setPrice(price);
//        flight.setfDateCreated(new Date(dateCreated));
//        flight.setfTimeCreated(new Time(timeCreated));

        return flight;
    }
}
