package com.example.something;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Vacation.class, Excursion.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class LocalDatabaseBuilder extends RoomDatabase {
    
    // DAO methods
    public abstract VacationDao vacationDao();
    public abstract ExcursionDao excursionDao();

    // Singleton instance
    private static volatile LocalDatabaseBuilder ldb_instance;

    // Executor service to handle database operations asynchronously
    private static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);

    // Get the singleton database instance
    public static LocalDatabaseBuilder getDatabase(final Context context) {
        if (ldb_instance == null) {
            synchronized (LocalDatabaseBuilder.class) {
                if (ldb_instance == null) {
                    ldb_instance = Room.databaseBuilder(context.getApplicationContext(),
                            LocalDatabaseBuilder.class, "LocalRoomDatabase.db")
                            .fallbackToDestructiveMigration()  // Drops tables when schema changes
                            .build();
                }
            }
        }
        return ldb_instance;
    }

    // Clear all data in the database (runs asynchronously)
    public void clearData() {
        databaseExecutor.execute(() -> {
            if (ldb_instance != null) {
                ldb_instance.clearAllTables();
            }
        });
    }
}
