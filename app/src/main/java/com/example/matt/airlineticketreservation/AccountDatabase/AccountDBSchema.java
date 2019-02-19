package com.example.matt.airlineticketreservation.AccountDatabase;
//ACOUNT DB SCHEMA
public class AccountDBSchema {

    public static final class AccountTable{
        public static final String NAME = "ACCOUNTS";

        public static final class Cols {
            public static final String UUID         = "uuid";
            public static final String USERNAME     = "username";
            public static final String PASSWORD     = "password";
//            public static final String DATE         = "date_created";
//            public static final String TIME         = "time_created";
        }
    }
}
