package com.example.matt.airlineticketreservation.AccountDatabase;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;
import com.example.matt.airlineticketreservation.AccountDatabase.AccountDBSchema.AccountTable;
//CURSOR WRAPPER FOR ACCOUNTS
public class AccountCursorWrapper  extends CursorWrapper {

    public AccountCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public CreateAccount getAccount() {
        String uuidString = getString(getColumnIndex(AccountTable.Cols.UUID));
        String userName = getString(getColumnIndex(AccountTable.Cols.USERNAME));
        String passWord = getString(getColumnIndex(AccountTable.Cols.PASSWORD));
//        long dateCreated = getLong(getColumnIndex(AccountTable.Cols.DATE));
//        long timeCreated = getLong(getColumnIndex(AccountTable.Cols.TIME));

        CreateAccount cAccounts = new CreateAccount(UUID.fromString(uuidString));
        cAccounts.setUsername(userName);
        cAccounts.setaPassword(passWord);
//        cAccounts.setaDateCreated(new Date(dateCreated));
//        cAccounts.setaTimeCreated(new Time(timeCreated));

        return cAccounts;
    }
}