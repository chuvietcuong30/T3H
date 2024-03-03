package com.example.step;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.viewpage.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Fragment_2 extends Fragment {
    TextView transferredStepsTextView,calo,km;
    Button time;
    ColumnChartView columnChartView;
    private SharedViewModel sharedViewModel;
    private List<Integer> chartData;
    private float CaloEpKieu;
    private float KmEpKieu;
    private String buttonTextData;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false);
        transferredStepsTextView = view.findViewById(R.id.transferredStepsTextView);
        columnChartView = view.findViewById(R.id.columnChartView);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        time = view.findViewById(R.id.NhatKy);
        calo = view.findViewById(R.id.calo);
        km = view.findViewById(R.id.km);
        long currentTimeMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        time.setText(formattedDate);
        // Lưu trữ dữ liệu của buttonTime
        buttonTextData = time.getText().toString();

        chartData = new ArrayList<>();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getSharedData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String data) {
                transferredStepsTextView.setText(data);
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int intValue = Integer.parseInt(transferredStepsTextView.getText().toString());
                    updateChartData(intValue);
                    updateChart();
                    double Calo = Double.parseDouble(transferredStepsTextView.getText().toString())*0.035;
                    CaloEpKieu = Float.parseFloat(Calo+"");
                    double Km = Double.parseDouble(transferredStepsTextView.getText().toString())*0.0008;
                    KmEpKieu = Float.parseFloat(Km+"");
                    calo.setText(""+CaloEpKieu);
                    km.setText(""+KmEpKieu);
                } catch (NumberFormatException e) {
                }
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent và truyền dữ liệu
                Intent intent = new Intent(getActivity(), History.class);
                intent.putExtra("caloKey", CaloEpKieu);
                intent.putExtra("kmKey", KmEpKieu);
                intent.putExtra("buttonTextKey", buttonTextData);

                // Bắt đầu Activity mới
                startActivity(intent);
            }
        });

        return view;
    }

    private void updateChartData(int value) {
        // Add data to the chart array
        chartData.add(value);

        // Check if the chartData size exceeds 7, clear the list and add the new value at the beginning
        if (chartData.size() > 7) {
            chartData.clear();
            chartData.add(value);
        }
    }


    private void updateChart() {
        // Update the chart with the latest data
        columnChartView.setColumnData(chartData);
    }
    @Override
    public void onPause() {
        super.onPause();
        saveChartData();
    }

    private void saveChartData() {
        // Lưu dữ liệu cột vào SharedPreferences
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        StringBuilder stringBuilder = new StringBuilder();
        for (int value : chartData) {
            stringBuilder.append(value).append(",");
        }

        // Lưu chuỗi dữ liệu cột
        editor.putString("chart_data", stringBuilder.toString());
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadChartData();
    }

    private void loadChartData() {
        // Đọc dữ liệu cột từ SharedPreferences
        SharedPreferences preferences = requireActivity().getPreferences(Context.MODE_PRIVATE);
        String chartDataString = preferences.getString("chart_data", "");

        // Chuyển đổi chuỗi dữ liệu thành danh sách các giá trị
        List<Integer> loadedChartData = new ArrayList<>();
        if (!chartDataString.isEmpty()) {
            String[] values = chartDataString.split(",");
            for (String value : values) {
                loadedChartData.add(Integer.parseInt(value));
            }
        }

        // Cập nhật chartData với dữ liệu đã tải
        chartData.clear();
        chartData.addAll(loadedChartData);

        // Cập nhật biểu đồ
        updateChart();
    }

}