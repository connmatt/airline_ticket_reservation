package com.example.matt.airlineticketreservation.ReservationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationDBSchema.ReservationTable;

import java.util.ArrayList;
import java.util.List;

//similar to Other helpers but is for Reservations and it connects with reservationList
public class ReservationHelper extends SQLiteOpenHelper{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "reservations.db";

    private SQLiteDatabase db;

    public ReservationHelper(Context context){super(context, DATABASE_NAME,null,VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ReservationTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ReservationTable.Cols.UUID + "," +
                ReservationTable.Cols.RESERVATIONNUMBER + "," +
                ReservationTable.Cols.FLIGHTNUMBER + "," +
                ReservationTable.Cols.USERNAME + "," +
                ReservationTable.Cols.DEPARTURE + "," +
                ReservationTable.Cols.ARRIVAL + "," +
                ReservationTable.Cols.TIME + "," +
                ReservationTable.Cols.NUMOFSEATS + "," +
                ReservationTable.Cols.PRICE +
//                FlightTable.Cols.DATECREATED + "," +
//                FlightTable.Cols.TIMECREATED +
                ")");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public long insertReservation(Reservation reservation){
        ContentValues cv = new ContentValues();
        cv.put(ReservationTable.Cols.UUID, reservation.getResId().toString());
        cv.put(ReservationTable.Cols.RESERVATIONNUMBER, reservation.getReservation_Num());
        cv.put(ReservationTable.Cols.FLIGHTNUMBER, reservation.getFlightNumber());
        cv.put(ReservationTable.Cols.USERNAME, reservation.getUsername());
        cv.put(ReservationTable.Cols.DEPARTURE, reservation.getDeparture());
        cv.put(ReservationTable.Cols.ARRIVAL, reservation.getArrival());
        cv.put(ReservationTable.Cols.TIME, reservation.getTime_Of_Departure());
        cv.put(ReservationTable.Cols.NUMOFSEATS, reservation.getNum_Of_Seats());
        cv.put(ReservationTable.Cols.PRICE, reservation.getPrice());

        db = this.getWritableDatabase();

        return db.insert(ReservationTable.NAME, null, cv);
    }

    public boolean updateReservation(String uuidString, ContentValues values) {
        try {
            db = this.getWritableDatabase();
            db.update(ReservationTable.NAME, values, ReservationTable.Cols.UUID + " = ? ",
                    new String[]{uuidString});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteReservation(String uuidString){
        try{
            db = this.getWritableDatabase();
            db.delete(ReservationTable.NAME, ReservationTable.Cols.UUID + " = ?",
                    new String[] {uuidString});
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public Cursor queryDB(String DBName, String whereClause, String[] whereArgs){
        db = this.getWritableDatabase();
        try{
            return db.query(
                    ReservationTable.NAME,
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

    public List<Reservation> getReservations(){
        List<Reservation> reservations = new ArrayList<>();

        ReservationCursorWrapper cursor = new ReservationCursorWrapper(this.queryDB(ReservationTable.NAME, null, null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                reservations.add(cursor.getReservation());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null){
                cursor.close();
            }
            return reservations;
        }
    }
}
