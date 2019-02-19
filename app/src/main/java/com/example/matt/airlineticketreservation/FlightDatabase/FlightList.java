package com.example.matt.airlineticketreservation.FlightDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.matt.airlineticketreservation.FlightDatabase.Flight;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.matt.airlineticketreservation.FlightDatabase.FlightDBSchema.FlightTable;
//SIMILAR TO ACCOUNT List
public class FlightList {
    private static FlightList mFlightList;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private FlightHelper mFlightHelper;
    private List<Flight> mFlights;

    public static FlightList get (Context context){
        if(mFlightList == null){
            mFlightList = new FlightList(context);
        }
        return mFlightList;
    }

    private FlightList(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new FlightHelper(mContext).getWritableDatabase();
        mFlightHelper = new FlightHelper(mContext);
        mFlights = getFlights();
    }

    public void addFlight(Flight fL){
        ContentValues values = getContentValues(fL);
        mFlightHelper.insertFlight(fL);
    }

    public List<Flight> getFlights() {return mFlightHelper.getFlights();}

    public Flight getFlight(UUID id){
        FlightCursorWrapper cursor = queryFlights(FlightTable.Cols.UUID + " = ? " ,
                new String[] {id.toString()});
        try{
            if(cursor.getCount() == 0)
            {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getFlight();
        }finally{
            cursor.close();
        }
    }

    public void updateList(){mFlights = getFlights();}

    @Deprecated
    private FlightCursorWrapper queryFlights(String whereClause, String[] whereArgs){
        Cursor cursor = mFlightHelper.queryDB(FlightTable.NAME,whereClause,whereArgs);

        return new FlightCursorWrapper(cursor);
    }

    public void updateFlight(Flight fL){
        String uuidString = fL.getFlightId().toString();
        ContentValues values = getContentValues(fL);
        mFlightHelper.updateFlight(uuidString, values);
    }

    private static ContentValues getContentValues(Flight fL)
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

        values.put(FlightTable.Cols.UUID, fL.getFlightId().toString());
        values.put(FlightTable.Cols.FLIGHTNUMBER, fL.getFlightNumber());
        values.put(FlightTable.Cols.DEPARTURE, fL.getDeparture());
        values.put(FlightTable.Cols.ARRIVAL, fL.getArrival());
        values.put(FlightTable.Cols.TIME, fL.getTime_Of_Departure());
        values.put(FlightTable.Cols.NUMOFSEATS, fL.getNum_Of_Seats());
        values.put(FlightTable.Cols.PRICE, fL.getPrice());

        return values;
    }

    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<Flight> flights = mFlightHelper.getFlights();
        sb.append("Flights\n");
        for(Flight fl: flights){
            sb.append(fl.toString());
        }
        return sb.toString();
    }

    public void createAutoFlights(String FlightNo, String departure, String arrival, String departure_time, int numOfSeats, double price)
    {
        getFlights();
        Flight nwFlight = new Flight();
        nwFlight.setFlightNumber(FlightNo);
        nwFlight.setDeparture(departure);
        nwFlight.setArrival(arrival);
        nwFlight.setTime_Of_Departure(departure_time);
        nwFlight.setNum_Of_Seats(numOfSeats);
        nwFlight.setPrice(price);
//        nwAccount.setaDateCreated(new Date());
//        nwAccount.setaTimeCreated(new Date());
        addFlight(nwFlight);
        updateFlight(nwFlight);
    }
}
