package com.example.matt.airlineticketreservation.AccountDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.example.matt.airlineticketreservation.AccountDatabase.AccountDBSchema.AccountTable;

public class AccountList {
    private static AccountList mAccountList;
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private AccountHelper mAccountHelper;
    private List<CreateAccount> mAccounts;

    //constructor
    public static AccountList get(Context context){
        if(mAccountList==null){
            mAccountList = new AccountList(context);
        }
        return mAccountList;
    }
    // another that gets called above
    private AccountList(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new AccountHelper(mContext).getWritableDatabase();
        mAccountHelper = new AccountHelper(mContext);
        mAccounts = getAccounts();
    }
    // add account to database
    public void addAccount(CreateAccount cA){
        ContentValues values = getContentValues(cA);
        mAccountHelper.insertAccount(cA);
    }
    //get list of accounts
    public List<CreateAccount> getAccounts(){
        return mAccountHelper.getAccounts();
    }
    //get specific account in database
    public CreateAccount getAccount(UUID id){
        AccountCursorWrapper cursor = queryAccounts( AccountTable.Cols.UUID + " = ? ",
                new String[] {id.toString()});
        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getAccount();
        }finally{
            cursor.close();
        }
    }
    // update list of accounts
    public void updateList(){mAccounts = getAccounts();}

    @Deprecated
    public void insertAccount(CreateAccount cAccount){
        this.addAccount(cAccount);
    }
    //update specific account
    public void updateAccount(CreateAccount cA){
        String uuidString = cA.getaId().toString();
        ContentValues values = getContentValues(cA);
        mAccountHelper.updateAccount(uuidString, values);
    }


    @Deprecated
    private AccountCursorWrapper queryAccounts(String whereClause, String[] whereArgs){

        Cursor cursor = mAccountHelper.queryDB(AccountTable.NAME,whereClause,whereArgs);

        return new AccountCursorWrapper(cursor);
    }
    // set up values to be put in database
    private static ContentValues getContentValues(CreateAccount cA){
        ContentValues values = new ContentValues();
//        Date dateCreated = null;
//        long timeCreated;
//        if(cA.getaDateCreated() == null)
//        {
//            dateCreated = null;
//            timeCreated = 0;
//        }else{
//            dateCreated = new Date(String.valueOf(cA.getaDateCreated()));
//            timeCreated = cA.getaTimeCreated();
//        }

        values.put(AccountTable.Cols.UUID, cA.getaId().toString());
        values.put(AccountTable.Cols.USERNAME, cA.getaUsername());
        values.put(AccountTable.Cols.PASSWORD, cA.getaPassword());
//        values.put(AccountTable.Cols.DATE, cA.getaDateCreated().toString());
//        values.put(AccountTable.Cols.TIME, cA.getaTimeCreated());

        return values;
    }
    // create auto accounts
    public void createAutoAccounts(String username, String password)
    {
        getAccounts();
        CreateAccount nwAccount = new CreateAccount();
        nwAccount.setUsername(username);
        nwAccount.setaPassword(password);
//        nwAccount.setaDateCreated(new Date());
//        nwAccount.setaTimeCreated(new Date());
        addAccount(nwAccount);
        updateAccount(nwAccount);
    }
    public String getLogString(){
        StringBuilder sb = new StringBuilder();
        List<CreateAccount> accounts = mAccountHelper.getAccounts();
        sb.append("Accounts\n");
        for(CreateAccount acct : accounts){
            sb.append(acct.toString());
        }
        return sb.toString();
    }
}
