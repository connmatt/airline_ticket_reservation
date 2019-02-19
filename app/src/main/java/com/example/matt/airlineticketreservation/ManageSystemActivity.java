package com.example.matt.airlineticketreservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matt.airlineticketreservation.AccountDatabase.CreateAccount;
import com.example.matt.airlineticketreservation.FlightDatabase.Flight;
import com.example.matt.airlineticketreservation.FlightDatabase.FlightList;

import java.util.List;

public class ManageSystemActivity extends AppCompatActivity {

    EditText flightNum;
    EditText departure;
    EditText arrival;
    EditText departure_time;
    EditText price;
    EditText flightCap;
    Button addFlight;

    FlightList flightDB;
    List<Flight> listOfFlights;

    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_system);
        addFlight = (Button) findViewById(R.id.addFlight_btn);
        addFlight.setOnClickListener(btnListener);

        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();

        flightDB = FlightList.get(this);
        flightDB.getFlights();

        alertBuilder.setMessage("Would you like to add a flight?");
        alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();

            }
        });
        alertBuilder.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
                Intent intent = new Intent(ManageSystemActivity.this, MainActivity.class);
                startActivity(intent);//send back to main activity
            }
        });
        alertBuilder.show();

    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            flightNum =(EditText) findViewById(R.id.flightNum);;
            departure =(EditText) findViewById(R.id.departure);;
            arrival =(EditText) findViewById(R.id.arrival);;
            departure_time =(EditText) findViewById(R.id.departTime);;
            price =(EditText) findViewById(R.id.price);;
            flightCap =(EditText) findViewById(R.id.capacity);;


            if(IsEmpty(flightNum) || IsEmpty(departure) || IsEmpty(arrival) || IsEmpty(departure_time) || IsEmpty(price) || IsEmpty(flightCap))//check if empty
            {
                //Log.i(TAG, "No Username or Password has been set");
                toastMaker("Must fill in all sections");
            }
            else{
                flightDB.updateList();
                listOfFlights = flightDB.getFlights();//check if user is inputting everything correctly
                    if (checkFlights(flightNum.getText().toString()) == false) {
                        Flight nwFlight = new Flight();// Make new account
                        nwFlight.setFlightNumber(flightNum.getText().toString());
                        nwFlight.setDeparture(departure.getText().toString());
                        nwFlight.setArrival(arrival.getText().toString());
                        nwFlight.setTime_Of_Departure(departure_time.getText().toString());
                        nwFlight.setNum_Of_Seats(Integer.parseInt(flightCap.getText().toString()));
                        nwFlight.setPrice(Double.parseDouble(price.getText().toString()));
                       //nwAccount.setUsername(usrnm.getText().toString());
                        //nwAccount.setaPassword(pswd.getText().toString());
                        flightDB.addFlight(nwFlight); // add to database
                        flightDB.updateFlight(nwFlight);
                        alertBuilder.setMessage("Flight Number " + flightNum.getText().toString() + " was successfully added!" + "\n" + nwFlight.toString());
                        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.dismiss();
                                Intent intent = new Intent(ManageSystemActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertBuilder.show();
                        toastMaker("Flight Number " + flightNum.getText().toString() + " was successfully added!");

                    } else {//alert they were wrong
                        alertBuilder.setMessage("Flight already exists.");
                        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.dismiss();
                                Intent intent = new Intent(ManageSystemActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertBuilder.show();
                    }
            }

            // Not going to work until we make the log item.
//                logItem = getLogItemFromDisplay();
//                Log.i(TAG, "logItem" + logItem.toString());



        }
    };//pop up message
    private void toastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(), message,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        t.show();
    }

    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }

    public boolean checkFlights(String flight) {
        for (Flight flight1 : listOfFlights) {
            if (flight1.getFlightNumber().equals(flight)) {
                return true;
            }
        }
        return false;
    }
}
