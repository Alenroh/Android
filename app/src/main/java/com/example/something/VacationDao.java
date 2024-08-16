package com.example.something;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface VacationDao {

    @Insert
    void insertVacation(Vacation vacation);

    @Update
    void updateVacation(Vacation vacation);

    @Delete
    void deleteVacation(Vacation vacation);

    @Query("SELECT * FROM vacations WHERE id = :id")
    LiveData<Vacation> getVacationById(int id);

    @Query("SELECT * FROM vacations")
    LiveData<List<Vacation>> getAllVacations();

    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    LiveData<List<Excursion>> getExcursionsForVacation(int vacationId);

    @Query("SELECT * FROM excursions WHERE id = :id")
    LiveData<Excursion> getExcursionById(int id);

    @Insert
    void insertExcursion(Excursion excursion);

    @Update
    void updateExcursion(Excursion excursion);

    @Delete
    void deleteExcursion(Excursion excursion);
}
