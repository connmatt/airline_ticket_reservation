package com.example.matt.airlineticketreservation.AccountDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.matt.airlineticketreservation.AccountDatabase.AccountDBSchema.AccountTable;

import java.util.ArrayList;
import java.util.List;
// ACCOUNT HELPER WHICH COMMUNICATES WITH THE ACCOUNT LIST
public class AccountHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "accounts.db";

    private SQLiteDatabase db;

    public AccountHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override// Create database
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + AccountTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                AccountTable.Cols.UUID + ", " +
                AccountTable.Cols.USERNAME + ", " +
                AccountTable.Cols.PASSWORD +
//                AccountTable.Cols.DATE + ", " +
//                AccountTable.Cols.TIME +
                ")"
        );

    }

    @Override//not used but upgrade with new version
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    //Insert account into database
    public long insertAccount(CreateAccount account){

        ContentValues cv = new ContentValues();
        cv.put(AccountTable.Cols.UUID, account.getaId().toString());
        cv.put(AccountTable.Cols.USERNAME, account.getaUsername());
        cv.put(AccountTable.Cols.PASSWORD, account.getaPassword());

//        try{
//            cv.put(AccountTable.Cols.DATE, account.getaDateCreated().toString());
//        }catch(NullPointerException e){
//            cv.put(AccountTable.Cols.DATE, "");
//            System.out.println("Date Created set to null");
//        }
//
//        try{
//            cv.put(AccountTable.Cols.TIME, account.getaTimeCreated());
//        }catch(NullPointerException e){
//            cv.put(AccountTable.Cols.TIME, "");
//            System.out.println("Time Created set to null");
//        }

        db = this.getWritableDatabase();

//        long rowID = db.insert(TodoTable.NAME, null, cv);
//        return rowID

        //name of the table, nullColumnHack, data to add
        return db.insert(AccountTable.NAME, null, cv);

    }
    //checks if there is a database to update account in database
    public boolean updateAccount(String uuidString, ContentValues values){
        try{
            db = this.getWritableDatabase();
            db.update(AccountTable.NAME, values, AccountTable.Cols.UUID + " = ? ",
                    new String[] {uuidString});
            return true;
        }catch(Exception e){
            return false;
        }
    }
    //delete account
    public boolean deleteAccount(String uuidString){
        try{
            db = this.getWritableDatabase();
            db.delete(AccountTable.NAME, AccountTable.Cols.UUID + " = ?",
                    new String[] {uuidString});
            return true;
        }catch(Exception e)
        {
            return false;
        }
    }
    //Cursor to get index of db
    public Cursor queryDB(String DBName, String whereClause, String[] whereArgs){
        db = this.getWritableDatabase();
        try{
            return db.query(
                    AccountTable.NAME,
                    null,
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null
            );
        }catch(Exception e){
            return null;
        }
    }

    //list of accounts
    public List<CreateAccount> getAccounts(){
        List<CreateAccount> accounts = new ArrayList<>();

        AccountCursorWrapper cursor = new AccountCursorWrapper(this.queryDB(AccountTable.NAME,null,null));

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                accounts.add(cursor.getAccount());
                cursor.moveToNext();
            }
        }finally{
            if(cursor != null)
                cursor.close();
        }
        return accounts;
    }
}
