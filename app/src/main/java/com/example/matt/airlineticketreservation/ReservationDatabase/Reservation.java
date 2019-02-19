package com.example.matt.airlineticketreservation.ReservationDatabase;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.UUID;

//RESERVATION BASE CLASS FOR SETTERS AND GETTERS: It implements parcelable to it can be based between activities
public class Reservation implements Parcelable {
    private UUID resId;
    private int reservation_Num;
    private String username;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String time_Of_Departure;
    private int num_Of_Seats;
    private double price;
    private Date fDateCreated;
    private long fTimeCreated;
    private static int counter = 1000;

    private static Reservation reservation;
    public Reservation(){this(UUID.randomUUID());reservation_Num = counter; counter++;}

    public Reservation(UUID id){resId = id; }

    public Reservation(Parcel in){
        in.readParcelable(Reservation.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resId.toString());
        dest.writeInt(reservation_Num);
        dest.writeString(flightNumber);
        dest.writeString(username);
        dest.writeString(departure);
        dest.writeString(time_Of_Departure);
        dest.writeString(arrival);
        dest.writeInt(num_Of_Seats);
        dest.writeDouble(price);
        //dest.writeString(mDateCreated.toString());
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Parcelable.Creator<Reservation> CREATOR = new Parcelable.Creator<Reservation>() {

        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public UUID getResId() {
        return resId;
    }

    public int getReservation_Num() {
        return reservation_Num;
    }

    public String getUsername() {
        return username;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public String getTime_Of_Departure() {
        return time_Of_Departure;
    }

    public int getNum_Of_Seats() {
        return num_Of_Seats;
    }

    public double getPrice() {
        return price;
    }

    public Date getfDateCreated() {
        return fDateCreated;
    }

    public long getfTimeCreated() {
        return fTimeCreated;
    }


    public void setResId(UUID resId) {
        this.resId = resId;
    }

    public void setReservation_Num(int reservation_Num) {
        this.reservation_Num = reservation_Num;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public void setTime_Of_Departure(String time_Of_Departure) {
        this.time_Of_Departure = time_Of_Departure;
    }

    public void setNum_Of_Seats(int num_Of_Seats) {
        this.num_Of_Seats = num_Of_Seats;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setfDateCreated(Date fDateCreated) {
        this.fDateCreated = fDateCreated;
    }

    public void setfTimeCreated(long fTimeCreated) {
        this.fTimeCreated = fTimeCreated;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }


    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
//        sb.append("-------------------------\n");
//        sb.append("Transaction Type: ").append("Reservation").append("\n");
        sb.append("Username: ").append(username).append("\n");
        sb.append("Flight Number: ").append(flightNumber).append("\n");
        sb.append("Departure: ").append(departure).append(",").append(time_Of_Departure).append("\n");
        sb.append("Arrival: ").append(arrival).append("\n");
        sb.append("Number of Tickets: ").append(num_Of_Seats).append("\n");
        sb.append("Reservation Number: ").append(reservation_Num).append("\n");
        sb.append("Total Amount: ").append("$").append(price).append("\n");
//        sb.append("Transaction date: ").append(fDateCreated).append("\n");
//        sb.append("Transaction time: ").append(fTimeCreated).append("\n");
        return sb.toString();
    }
}
