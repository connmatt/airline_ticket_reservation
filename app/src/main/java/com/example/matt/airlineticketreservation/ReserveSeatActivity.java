package com.example.matt.airlineticketreservation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matt.airlineticketreservation.AccountDatabase.AccountList;
import com.example.matt.airlineticketreservation.AccountDatabase.CreateAccount;

import com.example.matt.airlineticketreservation.FlightDatabase.FlightList;
import com.example.matt.airlineticketreservation.FlightDatabase.Flight;
import com.example.matt.airlineticketreservation.ReservationDatabase.Reservation;

import java.sql.Time;
import java.util.Date;
import java.util.List;
// this is to reserve a seat
public class ReserveSeatActivity extends AppCompatActivity {

    List<String> flights;
    public static final String TAG = "Flights_Log";
    EditText departure;
    EditText arrival;
    EditText numOfSeats;
    TextView flightInfo;
    Button findFlights;

    Button selectFlight;
    EditText flightNumber;

    public int numOfFlights = 0;

    public Flight selectedFlight;
    AccountList accountDB;
    List<CreateAccount> listOfAccounts;

    FlightList flightDB;
    List<Flight> listOfFlights;

    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;



    public boolean foundFlights = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);
        findFlights = (Button) findViewById(R.id.findFlight_btn);
        selectFlight = (Button) findViewById(R.id.selectFlight);
        flightInfo = (TextView) findViewById(R.id.flightInfo);
        flightInfo.setMovementMethod(new ScrollingMovementMethod());

        findFlights.setOnClickListener(btnListener);

        selectFlight.setOnClickListener(slctFltListener);


        //create the db if not already created
        accountDB = AccountList.get(this);
        accountDB.getAccounts();
        if(accountDB.getAccounts().size() <=0)
        {
            accountDB.createAutoAccounts("alice5", "csumb100");
            accountDB.createAutoAccounts("brian77", "123ABC");
            accountDB.createAutoAccounts("chris21", "CHRIS21");
            accountDB.createAutoAccounts("admin2", "admin2");

        }
        //create the db if not already created
        flightDB = FlightList.get(this);
        flightDB.getFlights();
        if(flightDB.getFlights().size()<= 0 || flightDB.getFlights() == null)
        {
            flightDB.createAutoFlights("Otter101", "Monterey", "Los Angeles", "10:00(AM)", 10, 150.00);
            flightDB.createAutoFlights("Otter102", "Los Angeles", "Monterey", "1:00(PM)", 10, 150.00);
            flightDB.createAutoFlights("Otter201", "Monterey", "Seattle", "11:00(AM)", 5, 200.50);
            flightDB.createAutoFlights("Otter205", "Monterey", "Seattle", "3:00(PM)", 15, 150.00);
            flightDB.createAutoFlights("Otter205", "Seattle", "Monterey", "2:00(PM)", 5, 200.50);
        }

        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();
    }

    View.OnClickListener slctFltListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {//this is to connect the button to an action
            flightNumber = (EditText) findViewById(R.id.flightNum);
            if(IsEmpty(flightNumber))
            {
                Log.i(TAG, "You have not searched for flights.");
                toastMaker("You need to enter in a Flight Number");
            }
            else
            {   //checks the flight Number and then updates the view to show available flights
                flightDB.updateList();
                listOfFlights = flightDB.getFlights();
                selectedFlight = GrabFlight(flightNumber.getText().toString());
                Reservation reservation = new Reservation();
                reservation.setFlightNumber(selectedFlight.getFlightNumber());
                reservation.setDeparture(selectedFlight.getDeparture());
                reservation.setArrival(selectedFlight.getArrival());
                reservation.setTime_Of_Departure(selectedFlight.getTime_Of_Departure());
                reservation.setNum_Of_Seats(Integer.parseInt(numOfSeats.getText().toString()));
                reservation.setPrice(selectedFlight.getPrice() * Integer.parseInt(numOfSeats.getText().toString()));
                //toastMaker(reservation.toString());
                // creates reservation to send to the log in screen so the user can confirm the reservation
                Intent intent = LogInActivity.newIntent(ReserveSeatActivity.this, reservation);
                startActivity(intent);
            }
        }
    };
// this is to display the flights
    View.OnClickListener btnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            departure = (EditText) findViewById(R.id.departure_txt);
            arrival = (EditText) findViewById(R.id.arrival_txt);
            numOfSeats = (EditText) findViewById(R.id.numOfSeats_txt);

            if(IsEmpty(departure) || IsEmpty(arrival) || IsEmpty(numOfSeats))
            {
                Log.i(TAG, "No Departure, arrival, or number of seats has been set.");
                toastMaker("Must fill in Departure, Arrival, and Number of Seats.");
            }
            else
            {// get database for flights
                flightDB.updateList();
                listOfFlights = flightDB.getFlights();
                flightInfo.setText("");

                if(Integer.parseInt(numOfSeats.getText().toString()) <= 7) // check if seats asking for is greater than 7
                {
                    foundFlight(departure.getText().toString(), arrival.getText().toString(), Integer.parseInt(numOfSeats.getText().toString()));
                    if(foundFlights)
                    {
                        alertBuilder.setMessage("Flights have been found!");
                        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.dismiss();
                            }
                        });
                        alertBuilder.show();
                    }
                    else
                    {
                        alertBuilder.setMessage("There are no flights available for " + departure.getText().toString() + " to " + arrival.getText().toString());
                        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.dismiss();
                                alertBuilder.setMessage("Please exit from reserving seats");
                                alertBuilder.setNeutralButton("EXIT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAlertDialog.dismiss();
                                        Intent intent = new Intent(ReserveSeatActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                alertBuilder.show();
                            }
                        });
                        alertBuilder.show();
                    }

                }
                else
                {
                    alertBuilder.setMessage("You can't reserve more than 7 seats.");
                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                        }
                    });
                    alertBuilder.show();
                }

            }
        }
    };



    private void toastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(), message,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        t.show();
    }

    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
    // check if found flights to display
    private boolean foundFlight(String departure, String arrival, int num)
    {
        numOfFlights = 0;
        foundFlights = false;
        for (Flight flight: listOfFlights)
        {
            if(flight.getDeparture().equals(departure) && flight.getArrival().equals(arrival) && flight.getNum_Of_Seats() >= num){
                numOfFlights++;
                AddDisplayFlight(flight.DisplayFlight());
                foundFlights = true;
            }
        }
        flightInfo.setText("Flights Found: " + numOfFlights + "\n\n" + flightInfo.getText().toString());

        return false;
    }
    // display the flights
    public String AddDisplayFlight(String flight)
    {
        flightInfo.append(flight);
        flightInfo.append("\n");
        return flightInfo.getText().toString();
    }
    //grab flight
    public Flight GrabFlight(String flightNum){
        Flight Selected = null;
        for(Flight flight : listOfFlights)
        {
            if(flight.getFlightNumber().equals(flightNum))
            {
                Selected = flight;
            }
        }
        return Selected;
    }

}
