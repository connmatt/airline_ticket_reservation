package com.example.matt.airlineticketreservation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

//This activity is when the user clicks cancel reservation they will go to this activityd
public class CancelLogInActivity extends AppCompatActivity {

    EditText mUserName;
    EditText mPassword;
    Button mLoginBtn;
    AccountList accountList;
    List<CreateAccount> accounts;
    CreateAccount account;

    private AlertDialog.Builder alertBuilder;

    public static Reservation reservation;
    List<Reservation> listOfReservations;
    ReservationList reservationDB;
    public ReservationHelper reservationHelper;

    Flight flight;
    List<Flight> listOfFlights;
    FlightList flightDB;




    android.support.v7.app.AlertDialog mAlertDialog;


    public static String NEXTDEST ="com.example.matt.airlineticketreservation.next";
    public static String next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(btnListener);

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
    }

    View.OnClickListener btnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent;
            mUserName = (EditText) findViewById(R.id.usrLogIn); //get the textfield for username
            mPassword = (EditText) findViewById(R.id.passwrdLogIn); //get the text field for password

            if(IsEmpty(mUserName) || IsEmpty(mPassword)) // if they are empty display message
            {
                toastMaker("Each field must be filled out.");
            }
            else{ //check if account is real then check if they have reservations
                if(RealAccount(mUserName.getText().toString(), mPassword.getText().toString()) ) {
                    reservationDB.updateList();
                    listOfReservations = reservationDB.getReservations();
                    if(hasReservations(mUserName.getText().toString())) // has reservations so send to CancelReservationActivity
                    {
                        intent = CancelReservationActivity.newIntent(CancelLogInActivity.this, reservation);
                        startActivity(intent);
                    }

                }
            }
        }
    };
    //check if Edit Text is empty
    private boolean IsEmpty(EditText textToCheck)
    {
        return textToCheck.getText().toString().trim().length() == 0;
    }
    //this is to display message on screend
    private void toastMaker(String m){
        Toast t = Toast.makeText(this.getApplicationContext(),m, Toast.LENGTH_LONG);
        t.show();
    }
    //check if account exists within createAccount database
    private boolean RealAccount(String usrnm, String passwd){
        if(usrnm.length() >= 4 && usrnm.matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"))
        {
            if(passwd.length() >= 4 && passwd.matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"))
            {
                boolean real = false;
                accounts = accountList.getAccounts();
                for(CreateAccount acc : accounts)
                {
                    if(acc.getaUsername().equals(usrnm) && acc.getaPassword().equals(passwd))
                    {
                        account = acc;
                        real = true;
                        return true;
                    }

                }
                if(!real){//alert window pops up with message waits for user to click button
                    alertBuilder.setMessage("Account does not exist.");
                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            Intent intent = new Intent(CancelLogInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertBuilder.show();
                }


            }
            else{//another alert window
                alertBuilder.setMessage("Password needs to have at least 3 alpha characters and 1 numerical character.");
                alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                        Intent intent = new Intent(CancelLogInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertBuilder.show();
            }
        }
        else{ // and another
            alertBuilder.setMessage("Username needs to have at least 3 alpha characters and 1 numerical character.");
            alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlertDialog.dismiss();
                    Intent intent = new Intent(CancelLogInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertBuilder.show();
        }
        return false;
    }
    //check if account has reservations
    private boolean hasReservations(String username)
    {
        listOfReservations = reservationDB.getReservations();

        for(Reservation res : listOfReservations)
        {
            if(res.getUsername().equals(username))
            {
                reservation = reservationDB.getReservation(res.getResId());
                return true;
            }
        }//look another alert window
        alertBuilder.setMessage("There are no reservations associated with " + username);
        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAlertDialog.dismiss();
                Intent intent = new Intent(CancelLogInActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        alertBuilder.show();
        return false;
    }
    //grab the flight information that is connected to reservation
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

