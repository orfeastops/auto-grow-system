package com.greenhouse.app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.greenhouse.app.data.AppDatabase;
import com.greenhouse.app.models.SensorHistory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HistoryFragment extends Fragment {

    private LineChart chartTempHumidity;
    private AppDatabase db;
    private ExecutorService executorService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        db = AppDatabase.getDatabase(getContext());
        executorService = Executors.newSingleThreadExecutor();

        chartTempHumidity = view.findViewById(R.id.chart_temp_humidity);

        Button btnViewDetailedHistory = view.findViewById(R.id.btn_view_detailed_history);
        btnViewDetailedHistory.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), HistoryTableActivity.class);
            startActivity(intent);
        });

        initializeCharts();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadHistoryData();
    }

    private void initializeCharts() {
        configureChart(chartTempHumidity, new LineData(), 0, 100);
    }

    private void loadHistoryData() {
        executorService.execute(() -> {
            List<SensorHistory> history = db.sensorHistoryDao().getHistory(1000);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (isAdded() && history != null && !history.isEmpty()) {
                    setupTempHumidityChart(history);
                }
            });
        });
    }

    private void setupTempHumidityChart(List<SensorHistory> history) {
        ArrayList<Entry> tempEntries = new ArrayList<>();
        ArrayList<Entry> humidityEntries = new ArrayList<>();

        for (SensorHistory item : history) {
            tempEntries.add(new Entry(item.timestamp, item.temperature));
            humidityEntries.add(new Entry(item.timestamp, item.humidity));
        }

        LineDataSet tempDataSet = new LineDataSet(tempEntries, "Temperature (°C)");
        tempDataSet.setColor(Color.RED);
        tempDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        tempDataSet.setValueTextColor(Color.WHITE);

        LineDataSet humidityDataSet = new LineDataSet(humidityEntries, "Humidity (%)");
        humidityDataSet.setColor(Color.BLUE);
        humidityDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        humidityDataSet.setValueTextColor(Color.WHITE);

        chartTempHumidity.setData(new LineData(tempDataSet, humidityDataSet));
        chartTempHumidity.invalidate();
    }

    private void configureChart(LineChart chart, LineData data, float minY, float maxY) {
        chart.setData(data);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setValueFormatter(new DateAxisFormatter());
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setTextColor(Color.WHITE);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(minY);
        leftAxis.setAxisMaximum(maxY);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        chart.getLegend().setTextColor(Color.WHITE);
        chart.invalidate();
    }

    private static class DateAxisFormatter extends ValueFormatter {
        private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        @Override
        public String getFormattedValue(float value) {
            return sdf.format(new Date((long) value));
        }
    }
}
