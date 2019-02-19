package com.example.matt.airlineticketreservation.FlightDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matt.airlineticketreservation.FlightDatabase.FlightDBSchema.FlightTable;

import java.util.ArrayList;
import java.util.List;
//SIMILAR to AccountHelper
public class FlightHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "flights.db";

    private SQLiteDatabase db;

    public FlightHelper(Context context) {super(context, DATABASE_NAME, null, VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + FlightTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                FlightTable.Cols.UUID + "," +
                FlightTable.Cols.FLIGHTNUMBER + "," +
                FlightTable.Cols.DEPARTURE + "," +
                FlightTable.Cols.ARRIVAL + "," +
                FlightTable.Cols.TIME + "," +
                FlightTable.Cols.NUMOFSEATS + "," +
                FlightTable.Cols.PRICE +
//                FlightTable.Cols.DATECREATED + "," +
//                FlightTable.Cols.TIMECREATED +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertFlight(Flight flight) {
        ContentValues cv = new ContentValues();
        cv.put(FlightTable.Cols.UUID, flight.getFlightId().toString());
        cv.put(FlightTable.Cols.FLIGHTNUMBER, flight.getFlightNumber());
        cv.put(FlightTable.Cols.DEPARTURE, flight.getDeparture());
        cv.put(FlightTable.Cols.ARRIVAL, flight.getArrival());
        cv.put(FlightTable.Cols.TIME, flight.getTime_Of_Departure());
        cv.put(FlightTable.Cols.NUMOFSEATS, flight.getNum_Of_Seats());
        cv.put(FlightTable.Cols.PRICE, flight.getPrice());

//        try {
//            cv.put(FlightTable.Cols.DATECREATED, flight.getfDateCreated().toString());
//        } catch (NullPointerException e) {
//            cv.put(FlightTable.Cols.DATECREATED, "");
//            System.out.println("Date Created set to null");
//        }
//        try {
//            cv.put(FlightTable.Cols.TIMECREATED, flight.getfTimeCreated());
//        } catch (NullPointerException e) {
//            cv.put(FlightTable.Cols.TIMECREATED, "");
//            System.out.println("Time Created set to null");
//        }

        db = this.getWritableDatabase();

        return db.insert(FlightTable.NAME, null, cv);
    }

    public boolean updateFlight(String uuidString, ContentValues values) {
        try {
            db = this.getWritableDatabase();
            db.update(FlightTable.NAME, values, FlightTable.Cols.UUID + " = ? ",
                    new String[]{uuidString});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteFlight(String uuidString) {
        try {
            db = this.getWritableDatabase();
            db.delete(FlightTable.NAME, FlightTable.Cols.UUID + " = ?",
                    new String[]{uuidString});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Cursor queryDB(String DBName, String whereClause, String[] whereArgs){
        db = this.getWritableDatabase();
        try{
            return db.query(
                    FlightTable.NAME,
                    null,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );
        }catch(Exception e){
            return null;
        }
    }

    public List<Flight> getFlights(){
        List<Flight> flights = new ArrayList<>();

        FlightCursorWrapper cursor = new FlightCursorWrapper(this.queryDB(FlightTable.NAME, null, null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                flights.add(cursor.getFlight());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null){
                cursor.close();
            }
            return flights;
        }
    }
}
