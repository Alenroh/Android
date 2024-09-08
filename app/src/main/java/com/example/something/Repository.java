package com.example.something;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Repository {
    private VacationDao vacationDao;
    private ExcursionDao excursionDao;

    private static final int num_threads = 4;
    static final ExecutorService dbExecutor = Executors.newFixedThreadPool(num_threads);

    public Repository(Application application) {
        LocalDatabaseBuilder ldb = LocalDatabaseBuilder.getDatabase(application);
        vacationDao = ldb.vacationDao();
        excursionDao = ldb.excursionDao();
    }

    // Return LiveData for all vacations
    public LiveData<List<Vacation>> getAllVacations() {
        return vacationDao.getAllVacations();
    }

    // Return LiveData for excursions by vacation ID
    public LiveData<List<Excursion>> getAllExcursionsByVacationId(int vacationId) {
        return excursionDao.getExcursionsByVacation(vacationId);
    }

    // Add method to get all excursions (not filtered by vacation ID)
    public LiveData<List<Excursion>> getAllExcursions() {
        return excursionDao.getAllExcursions();
    }

    // Insert vacation and return the vacation ID as Future<Long>
    public Future<Long> insert(Vacation vacation) {
        return dbExecutor.submit(() -> vacationDao.insert(vacation));  // Perform insert and return Future<Long>
    }

    // Insert excursion and return the excursion ID as Future<Long>
    public Future<Long> insert(Excursion excursion) {
        return dbExecutor.submit(() -> excursionDao.insert(excursion));  // Perform insert and return Future<Long>
    }

    public void update(Vacation vacation) {
        dbExecutor.execute(() -> vacationDao.update(vacation));
    }

    public void deleteVacationById(int vacationId) {
        dbExecutor.execute(() -> vacationDao.deleteVacationById(vacationId));
    }

    public void update(Excursion excursion) {
        dbExecutor.execute(() -> excursionDao.update(excursion));
    }

    public void delete(Excursion excursion) {
        dbExecutor.execute(() -> excursionDao.delete(excursion));
    }
}
