package com.example.something;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface ExcursionDao {

    @Insert
    void insertExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);

    @Delete
    void deleteExcursion(Excursion excursion);

    @Query("SELECT * FROM excursions WHERE id = :id")
    LiveData<Excursion> getExcursionById(int id);

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    LiveData<List<Excursion>> getExcursionsForVacation(int vacationId);
}
