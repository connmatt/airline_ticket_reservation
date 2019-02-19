package com.example.matt.airlineticketreservation.ReservationDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationDBSchema.ReservationTable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

//Similar to the other ones like FlightList
public class ReservationList {

    private static ReservationList mReservationList;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private ReservationHelper mReservationHelper;
    private List<Reservation> mReservations;

    public static ReservationList get(Context context)
    {
        if(mReservationList == null)
        {
            mReservationList = new ReservationList(context);
        }
        return mReservationList;
    }

    private ReservationList(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new ReservationHelper(mContext).getWritableDatabase();
        mReservationHelper = new ReservationHelper(mContext);
        mReservations = getReservations();
    }

    public void addReservation(Reservation res){
        ContentValues values = getContentValues(res);
        mReservationHelper.insertReservation(res);
    }

    public List<Reservation> getReservations() {return mReservationHelper.getReservations();}

    public Reservation getReservation(UUID id){
        ReservationCursorWrapper cursor = queryReservations(ReservationTable.Cols.UUID + " = ? ",
                new String[] {id.toString()});
        try{
            if(cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getReservation();
        }finally{
            cursor.close();
        }
    }

    public void updateList(){mReservations = getReservations();}

    @Deprecated
    private ReservationCursorWrapper queryReservations(String whereClause, String[] whereArgs){
        Cursor cursor = mReservationHelper.queryDB(ReservationTable.NAME,whereClause,whereArgs);
        return new ReservationCursorWrapper(cursor);
    }

    public void updateReservation(Reservation res){
        String uuidString = res.getResId().toString();
        ContentValues values = getContentValues(res);
        mReservationHelper.updateReservation(uuidString, values);
    }

    private static ContentValues getContentValues(Reservation res)
    {
        ContentValues values = new ContentValues();
//        Date dateCreated = null;
//        long timeCreated;
//        if(fL.getfDateCreated() == null)
//        {
//            dateCreated = null;
//            timeCreated = 0;
//        }
//        else{
//            dateCreated = new Date(String.valueOf(fL.getfDateCreated()));
//            timeCreated = fL.getfTimeCreated();
//        }

        values.put(ReservationTable.Cols.UUID, res.getResId().toString());
        values.put(ReservationTable.Cols.RESERVATIONNUMBER, res.getReservation_Num());
        values.put(ReservationTable.Cols.USERNAME, res.getUsername());
        values.put(ReservationTable.Cols.FLIGHTNUMBER, res.getFlightNumber());
        values.put(ReservationTable.Cols.DEPARTURE, res.getDeparture());
        values.put(ReservationTable.Cols.ARRIVAL, res.getArrival());
        values.put(ReservationTable.Cols.TIME, res.getTime_Of_Departure());
        values.put(ReservationTable.Cols.NUMOFSEATS, res.getNum_Of_Seats());
        values.put(ReservationTable.Cols.PRICE, res.getPrice());

        return values;
    }

    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<Reservation> reservations = mReservationHelper.getReservations();
        sb.append("Flights\n");
        for(Reservation res: reservations){
            sb.append(res.toString());
        }
        return sb.toString();
    }

}
