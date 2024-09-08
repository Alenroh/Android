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
public interface ExcursionDao {

    @Query("SELECT * FROM Excursions ORDER BY excursionID ASC")
    LiveData<List<Excursion>> getAllExcursions();

    @Query("SELECT * FROM Excursions WHERE vacationID=:id ORDER BY excursionID ASC")
    LiveData<List<Excursion>> getExcursionsByVacation(int id);

    @Query("SELECT * FROM Excursions WHERE excursionID = :id")
    LiveData<Excursion> findExcursionById(int id);

    // Insert method to return the generated excursion ID
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Excursion excursion);  // Returns the inserted excursionID

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);
}
