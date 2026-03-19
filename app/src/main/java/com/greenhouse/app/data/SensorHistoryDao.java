package com.greenhouse.app.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.greenhouse.app.models.SensorHistory;
import java.util.List;

@Dao
public interface SensorHistoryDao {
    @Insert
    void insert(SensorHistory sensorHistory);

    @Query("SELECT * FROM sensor_history ORDER BY timestamp DESC LIMIT :limit")
    List<SensorHistory> getHistory(int limit);

    @Query("DELETE FROM sensor_history WHERE timestamp < :timestamp")
    void deleteOlderThan(long timestamp);
}
