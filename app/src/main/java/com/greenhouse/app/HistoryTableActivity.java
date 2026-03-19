package com.greenhouse.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.greenhouse.app.data.AppDatabase;
import com.greenhouse.app.models.SensorHistory;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryTableActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_table);

        db = AppDatabase.getDatabase(this);

        TableView<String[]> tableView = findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, "Time", "Temp", "Hum", "M1", "P1", "M2", "P2", "M3", "P3"));

        Button btnClose = findViewById(R.id.btn_close_history_table);
        btnClose.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        db.sensorHistoryDao().getHistory(20000).observe(this, history -> {
            if (history != null) {
                updateTable(history);
            }
        });
    }

    private void updateTable(List<SensorHistory> history) {
        List<String[]> data = new ArrayList<>();
        for (SensorHistory item : history) {
            String[] row = new String[9];
            row[0] = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(item.timestamp));
            row[1] = String.format(Locale.getDefault(), "%.1f", item.temperature);
            row[2] = String.format(Locale.getDefault(), "%.0f", item.humidity);
            row[3] = String.valueOf(item.moisture1);
            row[4] = item.pump1_on ? "ON" : "OFF";
            row[5] = String.valueOf(item.moisture2);
            row[6] = item.pump2_on ? "ON" : "OFF";
            row[7] = String.valueOf(item.moisture3);
            row[8] = item.pump3_on ? "ON" : "OFF";
            data.add(row);
        }

        TableView<String[]> tableView = findViewById(R.id.tableView);
        tableView.setDataAdapter(new SimpleTableDataAdapter(this, data));
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(Color.parseColor("#1A1A1A"), Color.parseColor("#0D0D0D")));
    }
}
