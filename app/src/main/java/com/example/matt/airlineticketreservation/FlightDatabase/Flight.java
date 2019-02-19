package com.example.matt.airlineticketreservation.FlightDatabase;

import java.util.Date;
import java.util.UUID;
//Flight base calss with getters and setters of variables
public class Flight {

    private UUID flightId;
    private String flightNumber;
    private String departure;
    private String arrival;
    private String time_Of_Departure;
    private int num_Of_Seats;
    private double price;
    private Date fDateCreated;
    private long fTimeCreated;

    public Flight() {
        this(UUID.randomUUID());
    }

    public Flight(UUID id) {
        flightId = id;
    }


    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public void setFlightNumber(String flightNum) {
        flightNumber = flightNum;
    }

    public void setDeparture(String depart) {
        departure = depart;
    }

    public void setArrival(String arriv) {
        arrival = arriv;
    }

    public void setTime_Of_Departure(String time_Of_Depart) {
        time_Of_Departure = time_Of_Depart;
    }

    public void setNum_Of_Seats(int num_Of_Seat) {
        num_Of_Seats = num_Of_Seat;
    }

    public void setPrice(double pr) {
        price = pr;
    }

    public String getFlightNumber() {
        return flightNumber;
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

    public Date getfDateCreated()
    {
        return fDateCreated;
    }

    public void setfDateCreated(Date dateCreated) { fDateCreated = dateCreated; }

    public long getfTimeCreated(){
        return fTimeCreated;
    }

    public void setfTimeCreated(Date timeCreated){ fTimeCreated = timeCreated.getTime(); }

    public String DisplayFlight()
    {
        return "Flight Number: " + flightNumber + "\n" +
                "Departure: " + departure + " " + time_Of_Departure + "\n" +
                "Arrival: " + arrival + "\n" +
                "Number of Seats: " + num_Of_Seats + "\n" +
                "Price: $" + price + "\n";
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------\n");
        sb.append("Transaction Type: ").append("New Flight Added").append("\n");
        sb.append("Flight Number: ").append(flightNumber).append("\n");
        sb.append("Departure: ").append(departure).append(",").append(time_Of_Departure).append("\n");
        sb.append("Arrival: ").append(arrival).append("\n");
        sb.append("Number of Seats: ").append(num_Of_Seats).append("\n");
        sb.append("Price").append("$").append(price).append("\n");
//        sb.append("Transaction date: ").append(fDateCreated).append("\n");
//        sb.append("Transaction time: ").append(fTimeCreated).append("\n");
        return sb.toString();
    }
}



