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

import com.example.matt.airlineticketreservation.AccountDatabase.AccountList;
import com.example.matt.airlineticketreservation.AccountDatabase.CreateAccount;

import java.sql.Time;
import java.util.Date;
import java.util.List;
//Create Account Activity
public class createAccountActivity extends AppCompatActivity {

    public static final String TAG = "AccountCreated_Log";
    EditText usrnm;
    EditText pswd;
    Button cAccount;

    AccountList accountDB;
    List<CreateAccount> listOfAccounts;

    AlertDialog.Builder alertBuilder;
    AlertDialog mAlertDialog;


    @Override//create it
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        cAccount = (Button) findViewById(R.id.btn_createAccount);

        cAccount.setOnClickListener(btnListener);

        accountDB = AccountList.get(this);

        accountDB.getAccounts();
        //create custom accounts
        if(accountDB.getAccounts().size() <=0)
        {
            accountDB.createAutoAccounts("alice5", "csumb100");
            accountDB.createAutoAccounts("brian77", "123ABC");
            accountDB.createAutoAccounts("chris21", "CHRIS21");
            accountDB.createAutoAccounts("admin2", "admin2");

        }
        alertBuilder = new AlertDialog.Builder(this);
        mAlertDialog = alertBuilder.create();
    }

    View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usrnm = (EditText) findViewById(R.id.userName);
            pswd = (EditText) findViewById(R.id.password_text);


            if(IsEmpty(usrnm) || IsEmpty(pswd))//check if empty
            {
                Log.i(TAG, "No Username or Password has been set");
                toastMaker("Must fill in Username and Passsword sections");
            }
            else{
                accountDB.updateList();
                listOfAccounts = accountDB.getAccounts();//check if user is inputting everything correctly
                if(usrnm.length() >= 4 && usrnm.getText().toString().matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$")) {
                    if (pswd.length() >= 4 && pswd.getText().toString().matches("(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$")) {
                        if (checkAccounts(usrnm.getText().toString()) == false) {
                            CreateAccount nwAccount = new CreateAccount();// Make new account
                            nwAccount.setUsername(usrnm.getText().toString());
                            nwAccount.setaPassword(pswd.getText().toString());
//                            nwAccount.setaDateCreated(new Date());
//                            nwAccount.setaTimeCreated(new Date());
                            accountDB.addAccount(nwAccount); // add to database
                            accountDB.updateAccount(nwAccount);
                            toastMaker("User " + usrnm.getText().toString() + " was successfully added!");
                            Intent intent = new Intent(createAccountActivity.this, MainActivity.class);
                            startActivity(intent);//send back to main activity

                        } else {//alert they were wrong
                            alertBuilder.setMessage("Username already exists.");
                            alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAlertDialog.dismiss();
                                    Intent intent = new Intent(createAccountActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertBuilder.show();
                        }
                    }
                    else
                    {   //alert they were wrong again
                        alertBuilder.setMessage("Password must contain at least 3 alpha characters and one numerical character.");
                        alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAlertDialog.dismiss();
                                Intent intent = new Intent(createAccountActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertBuilder.show();
                    }

                }
                else
                {//alert them again
                    alertBuilder.setMessage("Username must contain at least 3 alpha characters and one numerical character.");
                    alertBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAlertDialog.dismiss();
                            Intent intent = new Intent(createAccountActivity.this, MainActivity.class);
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
//check if account equals an account in database
    public boolean checkAccounts(String account) {
        for (CreateAccount account1 : listOfAccounts) {
            if (account1.getaUsername().equals(account)) {
                return true;
            }
        }
        return false;
    }

    private boolean IsEmpty(EditText textToCheck){
        return textToCheck.getText().toString().trim().length() == 0;
    }
}
