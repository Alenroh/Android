package com.example.something;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class VacationRepository {

    private VacationDao vacationDao;
    private ExcursionDao excursionDao;
    private LiveData<List<Vacation>> allVacations;

    public VacationRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        vacationDao = db.vacationDao();
        excursionDao = db.excursionDao();
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
        AppDatabase.databaseWriteExecutor.execute(() -> {
            // Ensure there are no excursions associated with the vacation before deleting
            if (excursionDao.getExcursionsForVacation(vacation.getId()).getValue().isEmpty()) {
                vacationDao.deleteVacation(vacation);
            }
        });
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return excursionDao.getExcursionsForVacation(vacationId);
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return excursionDao.getExcursionById(id);
    }

    public void insertExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> excursionDao.insertExcursion(excursion));
    }

    public void updateExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> excursionDao.updateExcursion(excursion));
    }

    public void deleteExcursion(Excursion excursion) {
        AppDatabase.databaseWriteExecutor.execute(() -> excursionDao.deleteExcursion(excursion));
    }
}

