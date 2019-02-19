package com.example.matt.airlineticketreservation;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.matt.airlineticketreservation.AccountDatabase.AccountList;
import com.example.matt.airlineticketreservation.AccountDatabase.CreateAccount;
import com.example.matt.airlineticketreservation.FlightDatabase.Flight;
import com.example.matt.airlineticketreservation.FlightDatabase.FlightList;
import com.example.matt.airlineticketreservation.ReservationDatabase.Reservation;
import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationHelper;
import com.example.matt.airlineticketreservation.ReservationDatabase.ReservationList;

import java.util.List;
//this login activity will confirm the reservation seats
public class LogInActivity extends AppCompatActivity {
    
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

    public static String RESERVE = "com.example.matt.airlineticketreservation.reservation";
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
            mUserName = (EditText) findViewById(R.id.usrLogIn);
            mPassword = (EditText) findViewById(R.id.passwrdLogIn);
            
            if(IsEmpty(mUserName) || IsEmpty(mPassword))
            {
                toastMaker("Each field must be filled out.");
            }
            else{//check if account is real
                if(RealAccount(mUserName.getText().toString(), mPassword.getText().toString())) {
                    flightDB.updateList();
                    listOfFlights = flightDB.getFlights();
                    reservationDB.updateList();
                    reservationDB.getReservations();
                    flight = GrabFlight(reservation.getFlightNumber()); // get flight connected to reservation
                    //int numOfAvailableSeats = flight.getNum_Of_Seats();
                    reservation.setUsername(mUserName.getText().toString()); // set username
//                    flight.setNum_Of_Seats(numOfAvailableSeats - reservation.getNum_Of_Seats());

                    //flightDB.updateFlight(flight);

                    alertBuilder.setMessage("Here is your Reservation: \n" + reservation.toString()); // show reservation
                    alertBuilder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            toastMaker(reservation.toString());
                            reservationDB.addReservation(reservation); //if confirmed add to reservations.db
                            reservationDB.updateReservation(reservation);
                            reservationDB.updateList();
                            listOfReservations = reservationDB.getReservations();
                            flight.setNum_Of_Seats(flight.getNum_Of_Seats() - reservation.getNum_Of_Seats()); //calcualte new amount of seats and add to flight and update it
                            flightDB.updateFlight(flight);
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent); //send to mainactivity
                        }
                    });
                    alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            alertBuilder.setMessage("Are you sure you want to cancel your reservation?"); //double check the user if they want to cancel the reservation
                            alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
//                                    ((AlertDialog) mAlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                                    ((AlertDialog) mAlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
                                    reservationHelper.deleteReservation(reservation.getResId().toString());
                                    reservationDB.updateList();
                                    alertBuilder.setMessage("The reservation failed to be created.");
                                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mAlertDialog.dismiss();
                                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                            startActivity(intent); // send to main activity
                                        }
                                    });
                                    alertBuilder.show();
                                }
                            });
                            alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                    ((AlertDialog) mAlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                    ((AlertDialog) mAlertDialog).getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);

                                }
                            });
                            alertBuilder.show();
                        }
                    });
                    alertBuilder.show();
                }
            }
        }
    };
    //check edit text if empty
    private boolean IsEmpty(EditText textToCheck)
    {
        return textToCheck.getText().toString().trim().length() == 0;
    }
    //pop up message
    private void toastMaker(String m){
        Toast t = Toast.makeText(this.getApplicationContext(),m, Toast.LENGTH_LONG);
        t.show();
    }
    //check if account exists
    private boolean RealAccount(String usrnm, String passwd){
        if(usrnm.length() >= 4 && usrnm.matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"))
        {
            if(passwd.length() >= 4 && passwd.matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$"))
            {
                boolean real = false;
                accounts = accountList.getAccounts();
                for(CreateAccount acc : accounts)
                {//grab specific accoung
                    if(acc.getaUsername().equals(usrnm) && acc.getaPassword().equals(passwd))
                    {
                        account = acc;
                        real = true;
                        return true;
                    }

                }
                if(!real){//notify if account doesn't exist
                    alertBuilder.setMessage("Account does not exist.");
                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertBuilder.show();
                }


            }
            else{//basic log in error
                alertBuilder.setMessage("Password needs to have at least 3 alpha characters and 1 numerical character.");
                alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog.dismiss();
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertBuilder.show();
            }
        }
        else{//log in error
            alertBuilder.setMessage("Username needs to have at least 3 alpha characters and 1 numerical character.");
            alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlertDialog.dismiss();
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            alertBuilder.show();
        }
        return false;
    }
//intent to send to another activity
    public static Intent newIntent(Context context, Reservation reserve)
    {
        Intent intent = new Intent(context, LogInActivity.class);
//        intent.putExtra(NEXTDEST, destinationNum);
        if(reserve!=null)
        {
            intent.putExtra(RESERVE, reserve);
            reservation = (Reservation) intent.getExtras().getParcelable(RESERVE);
        }


        return intent;
    }
//grab specific flight
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

    public void printAllReservations()
    {
        for(Reservation res : listOfReservations)
        {
            toastMaker(res.toString());
        }
    }
}
