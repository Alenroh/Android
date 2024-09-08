package com.example.something;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VacationDao {

    // Query to get all vacations ordered by vacationID
    @Query("SELECT * FROM Vacations ORDER BY vacationID ASC")
    LiveData<List<Vacation>> getAllVacations();

    // Query to get a specific vacation by its ID
    @Query("SELECT * FROM Vacations WHERE vacationID = :id")
    LiveData<Vacation> getVacationById(int id);

    // Insert a new vacation and return the generated vacationID
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Vacation vacation);

    // Update an existing vacation
    @Update
    void update(Vacation vacation);

    // Delete an existing vacation
    @Delete
    void delete(Vacation vacation);

    // Delete a vacation by its ID
    @Query("DELETE FROM Vacations WHERE vacationID = :id")
    void deleteVacationById(int id);
}
