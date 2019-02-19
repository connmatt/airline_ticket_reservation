package com.example.matt.airlineticketreservation.ReservationDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationDBSchema.ReservationTable;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;
//similar to the other cursorwrappers but for reservations
public class ReservationCursorWrapper extends CursorWrapper {

    public ReservationCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Reservation getReservation(){
        String uuidString = getString(getColumnIndex(ReservationTable.Cols.UUID));
        int reservationNumber = getInt(getColumnIndex(ReservationTable.Cols.RESERVATIONNUMBER));
        String flightNumber = getString(getColumnIndex(ReservationTable.Cols.FLIGHTNUMBER));
        String userName = getString(getColumnIndex(ReservationTable.Cols.USERNAME));
        String departure = getString(getColumnIndex(ReservationTable.Cols.DEPARTURE));
        String arrival = getString(getColumnIndex(ReservationTable.Cols.ARRIVAL));
        String time_of_departure = getString(getColumnIndex(ReservationTable.Cols.TIME));
        int num_of_seats = getInt(getColumnIndex(ReservationTable.Cols.NUMOFSEATS));
        double price = getDouble(getColumnIndex(ReservationTable.Cols.PRICE));
//        long dateCreated = getLong(getColumnIndex(FlightTable.Cols.DATECREATED));
//        long timeCreated = getLong(getColumnIndex(FlightTable.Cols.TIMECREATED));

        Reservation reservation = new Reservation(UUID.fromString(uuidString));
        reservation.setReservation_Num(reservationNumber);
        reservation.setFlightNumber(flightNumber);
        reservation.setUsername(userName);
        reservation.setDeparture(departure);
        reservation.setArrival(arrival);
        reservation.setTime_Of_Departure(time_of_departure);
        reservation.setNum_Of_Seats(num_of_seats);
        reservation.setPrice(price);
//        flight.setfDateCreated(new Date(dateCreated));
//        flight.setfTimeCreated(new Time(timeCreated));

        return reservation;
    }
}
