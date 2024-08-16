package com.example.something;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "id",
                childColumns = "vacationId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int vacationId;
    private String title;
    private String date;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getVacationId() { return vacationId; }
    public void setVacationId(int vacationId) { this.vacationId = vacationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
