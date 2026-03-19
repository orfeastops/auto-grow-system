package com.greenhouse.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sensor_history")
public class SensorHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public long timestamp;
    public float temperature;
    public float humidity;
    public int moisture1;
    public int moisture2;
    public int moisture3;
    public boolean pump1_on;
    public boolean pump2_on;
    public boolean pump3_on;
}
