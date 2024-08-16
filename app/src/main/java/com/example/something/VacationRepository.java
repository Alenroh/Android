package com.example.something;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class VacationRepository {

    private VacationDao vacationDao;
    private LiveData<List<Vacation>> allVacations;

    public VacationRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        vacationDao = db.vacationDao();
        allVacations = vacationDao.getAllVacations();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public LiveData<Vacation> getVacationById(int id) {
        return vacationDao.getVacationById(id);
    }

    public void insert(Vacation vacation) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.insertVacation(vacation));
    }

    public void update(Vacation vacation) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.updateVacation(vacation));
    }

    public void delete(Vacation vacation) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.deleteVacation(vacation));
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return vacationDao.getExcursionsForVacation(vacationId);
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return vacationDao.getExcursionById(id);
    }

    public void insertExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.insertExcursion(excursion));
    }

    public void updateExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.updateExcursion(excursion));
    }

    public void deleteExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> vacationDao.deleteExcursion(excursion));
    }
}

