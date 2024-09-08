package com.example.something;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Excursions")
public class Excursion {

    @PrimaryKey(autoGenerate = true)
    private int excursionID;

    private String title;

    private Date excursion_date;

    private int vacationID;

    public Excursion(int excursionID, String title, Date excursion_date, int vacationID) {
        this.excursionID = excursionID;
        this.title = title;
        this.excursion_date = excursion_date;
        this.vacationID = vacationID;
    }

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getExcursion_date() {
        return excursion_date;
    }

    public void setExcursion_date(Date excursion_date) {
        this.excursion_date = excursion_date;
    }

    public int getVacationID(){
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }
}
