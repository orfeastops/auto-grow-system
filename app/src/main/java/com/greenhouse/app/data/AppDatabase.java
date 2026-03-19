package com.greenhouse.app.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.greenhouse.app.models.SensorHistory;

@Database(entities = {SensorHistory.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SensorHistoryDao sensorHistoryDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "greenhouse_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
