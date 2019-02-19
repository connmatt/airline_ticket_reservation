package com.example.matt.airlineticketreservation.AccountDatabase;

import java.util.Date;
import java.util.UUID;
// this is the base class for account that has the getters and setters of each variable.
public class CreateAccount {
    private UUID aId;
    private String aUsername;
    private String aPassword;
    private Date aDateCreated;
    private long aTimeCreated;

    public CreateAccount(){
        this(UUID.randomUUID());
    }

    public CreateAccount(UUID id){
        aId = id;
//        aDateCreated = new Date();
    }

    public String getaUsername(){ return aUsername;}

    public void setUsername(String usernm) {aUsername = usernm;}

    public String getaPassword(){ return aPassword;}

    public void setaPassword(String passwd) {aPassword = passwd;}

    public Date getaDateCreated()
    {
        return aDateCreated;
    }

    public void setaDateCreated(Date dateCreated) { aDateCreated = dateCreated; }

    public long getaTimeCreated(){
        return aTimeCreated;
    }

    public void setaTimeCreated(Date timeCreated){ aTimeCreated = timeCreated.getTime(); }

    public UUID getaId(){ return aId; }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------\n");
        sb.append("Transaction Type: ").append("New Account").append("\n");
        sb.append("Username: ").append(aUsername).append("\n");
        sb.append("Password: ").append(aPassword).append("\n");
//        sb.append("Transaction date: ").append(aDateCreated).append("\n");
//        sb.append("Transaction time: ").append(aTimeCreated).append("\n");
        return sb.toString();
    }
}
