package com.example.matt.airlineticketreservation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.matt.airlineticketreservation.AccountDatabase.AccountList;

public class MainActivity extends AppCompatActivity {

    Button createAcctActivity_btn;
    Button reserveSeatActivity_btn;
    Button cancelResActivity_btn;
    Button manageSystem_btn;

    AccountList accountDB;


//Main activity to send to other activities
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createAcctActivity_btn = (Button) findViewById(R.id.createAccAct_btn);
        reserveSeatActivity_btn = (Button) findViewById(R.id.reserveSeatAct_btn);
        cancelResActivity_btn = (Button) findViewById(R.id.cancelReservationAct_btn);
        manageSystem_btn = (Button) findViewById(R.id.manageSystemAct_btn);
        createAcctActivity_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    openCreateAccount();
                }
        });

        reserveSeatActivity_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openReserveSeat();
            }
        });

        cancelResActivity_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openCancelReservation();
            }
        });

        manageSystem_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openManageSystem();
            }
        });

    }
    //send to create account
    public void openCreateAccount(){
        Intent intent = new Intent(this, createAccountActivity.class);
        startActivity(intent);
    }
    // send to Reserve seat
    public void openReserveSeat(){
        Intent intent = new Intent(this, ReserveSeatActivity.class);
        startActivity(intent);
    }
    // send to Cancel Reservation login
    public void openCancelReservation(){
        Intent intent = new Intent(this, CancelLogInActivity.class);
        startActivity(intent);
    }

    public void openManageSystem(){
        Intent intent = new Intent(this, ManageSystemLogActivity.class);
        startActivity(intent);
    }
}
