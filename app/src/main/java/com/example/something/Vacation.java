package com.example.something;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Vacations")
public class Vacation {
    public Vacation(int vacationID, String title, String hotel, Date start_date, Date end_date){
        this.vacationID = vacationID;
        this.title = title;
        this.hotel = hotel;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @PrimaryKey(autoGenerate = true)
    private int vacationID;

    private String title;

    private String hotel;

    private Date start_date;

    private Date end_date;

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHotel(){
        return this.hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Date getStart_date(){
        return this.start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date(){
        return this.end_date;
    }

    public void setEnd_date(Date end_date){
        this.end_date = end_date;
    }
}
