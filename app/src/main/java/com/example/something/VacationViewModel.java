package com.example.something;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class VacationViewModel extends AndroidViewModel {

    private VacationRepository repository;
    private LiveData<List<Vacation>> allVacations;

    public VacationViewModel(Application application) {
        super(application);
        repository = new VacationRepository(application);
        allVacations = repository.getAllVacations();
    }

    public LiveData<List<Vacation>> getAllVacations() {
        return allVacations;
    }

    public LiveData<Vacation> getVacationById(int id) {
        return repository.getVacationById(id);
    }

    public void insert(Vacation vacation) {
        repository.insert(vacation);
    }

    public void update(Vacation vacation) {
        repository.update(vacation);
    }

    public void delete(Vacation vacation) {
        repository.delete(vacation);
    }

    public LiveData<List<Excursion>> getExcursionsForVacation(int vacationId) {
        return repository.getExcursionsForVacation(vacationId);
    }

    public LiveData<Excursion> getExcursionById(int id) {
        return repository.getExcursionById(id);
    }

    public void insertExcursion(Excursion excursion) {
        repository.insertExcursion(excursion);
    }

    public void updateExcursion(Excursion excursion) {
        repository.updateExcursion(excursion);
    }

    public void deleteExcursion(Excursion excursion) {
        repository.deleteExcursion(excursion);
    }
}
