package com.example.matt.airlineticketreservation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.matt.airlineticketreservation.FlightDatabase.Flight;
import com.example.matt.airlineticketreservation.FlightDatabase.FlightList;
import com.example.matt.airlineticketreservation.ReservationDatabase.Reservation;
import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationHelper;
import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationList;

import java.util.List;

//This is the cancel reservation activity wehre the user logs in and they see their reservations
public class CancelReservationActivity extends AppCompatActivity {
    public static String RESERVE = "com.example.matt.airlineticketreservation.login";
    public static final String TAG = "DeleteReservation_Log";
    public static Reservation reservation;
    TextView reservationInfo;
    TextView usrRes;
    EditText flightNumber;

    AccountList accountList;
    List<CreateAccount> accounts;
    CreateAccount account;

    private AlertDialog.Builder alertBuilder;

    List<Reservation> listOfReservations;
    ReservationList reservationDB;
    public ReservationHelper reservationHelper;

    Flight flight;
    List<Flight> listOfFlights;
    FlightList flightDB;
    android.support.v7.app.AlertDialog mAlertDialog;

    Reservation oneToDelete;

    Button deleteRes_btn;

    public boolean found = false;

    Flight editFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_reservation);
        usrRes = (TextView) findViewById(R.id.UsrReservation); // set textview of username
        usrRes.setText(reservation.getUsername() + " Reservations"); // set text of the textview
        usrRes.setTextSize(30);

        reservationInfo = (TextView) findViewById(R.id.reservationInfo); // set textview for resrvationinfo
        reservationInfo.setMovementMethod(new ScrollingMovementMethod());// make it scrollable


        deleteRes_btn = (Button) findViewById(R.id.dltBtn); // connect button
        deleteRes_btn.setOnClickListener(dltResListener); // make it listen

        accountList = AccountList.get(this);
        accountList.updateList();
        accounts = accountList.getAccounts();


        flightDB = FlightList.get(this);
        flightDB.getFlights();

        reservationDB = ReservationList.get(this);
        reservationDB.getReservations();
        reservationHelper =new ReservationHelper(this);
        Reservation Res;


        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();

        listOfReservations = reservationDB.getReservations();
        displayReservations(reservation.getUsername());

    }

    View.OnClickListener dltResListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            flightNumber = (EditText) findViewById(R.id.flightNum); //connect edit text
            if(IsEmpty(flightNumber)) //check if empty
            {
                Log.i(TAG, "You have not searched for flights.");
                toastMaker("You need to enter in a Flight Number");
            }
            else
            {
                reservationDB.updateList();//update list of reservations
                listOfReservations = reservationDB.getReservations(); //get them reservations
                oneToDelete = grabReservation(flightNumber.getText().toString()); //grab reservation based on flightnumber
                if(found){

                    flightDB.updateList();
                    listOfFlights = flightDB.getFlights();
                    editFlight = GrabFlight(flightNumber.getText().toString());
                    reservationHelper.deleteReservation(reservation.getResId().toString()); //delete the bad boy
                    reservationDB.updateList();
                    editFlight.setNum_Of_Seats(editFlight.getNum_Of_Seats() + oneToDelete.getNum_Of_Seats()); //modify the amount of seats available in the flight database
                    alertBuilder.setMessage("You have deleted Reservation: \n" + oneToDelete.toString()); // alert the user
                    alertBuilder.setNeutralButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            Intent intent = new Intent(CancelReservationActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertBuilder.show();
                }
                else
                {
                    alertBuilder.setMessage("Not a valid Flight Number."); // alert the user
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

    //intent to send from login to this screen
    public static Intent newIntent(Context context, Reservation reserve)
    {
        Intent intent = new Intent(context, CancelReservationActivity.class);
//        intent.putExtra(NEXTDEST, destinationNum);
        if(reserve!=null)
        {
            intent.putExtra(RESERVE, reserve);
            reservation = (Reservation) intent.getExtras().getParcelable(RESERVE);
        }


        return intent;
    }
    //check if empty
    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
    //display the reservations connected to user
    public void displayReservations(String username){
        StringBuilder sb = new StringBuilder();
        for(Reservation res : listOfReservations)
        {
            if(res.getUsername().equals(username))
            {
                sb.append(res.toString());
                sb.append("\n");
            }
        }

        reservationInfo.setText(sb.toString());
    }
    //pop up message
    private void toastMaker(String message){
        Toast t = Toast.makeText(this.getApplicationContext(), message,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER_VERTICAL, 0,0);
        t.show();
    }

    //grab reservation
    public Reservation grabReservation(String flightNum){
        reservationDB.updateList();
        listOfReservations = reservationDB.getReservations();
        Reservation resToDelete = null;
        found = false;
        for(Reservation res : listOfReservations)
        {
            if(res.getFlightNumber().equals(flightNum))
            {
                found = true;
                resToDelete = res;
            }
        }
        return resToDelete;
    }
    // grab flight
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
