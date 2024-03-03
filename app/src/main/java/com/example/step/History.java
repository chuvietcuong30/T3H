package com.example.step;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.viewpage.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    TextView historyTextView;
    List<HistoryEntry> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        historyTextView = findViewById(R.id.historyTextView);


        // Khởi tạo danh sách lịch sử và đọc từ SharedPreferences
        initHistoryList();
        loadHistory();

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        float caloValue = intent.getFloatExtra("caloKey", 0.0f);
        float kmValue = intent.getFloatExtra("kmKey", 0.0f);
        String buttonTextData = intent.getStringExtra("buttonTextKey");

        // Hiển thị dữ liệu trên TextView
        String historyEntry = " Calo: " + caloValue + ", Km: " + kmValue + "\n";

        // Lưu vào danh sách lịch sử
        historyList.add(new HistoryEntry(caloValue, kmValue, historyEntry, buttonTextData));

        // Hiển thị và lưu danh sách lịch sử
        updateHistoryTextView();
        saveHistory();
    }

    // Hãy gọi phương thức này trong phương thức onCreate để khởi tạo danh sách lịch sử
    private void initHistoryList() {
        historyList = new ArrayList<>();
    }

    // Hiển thị lịch sử trên TextView
    private void updateHistoryTextView() {
        StringBuilder historyText = new StringBuilder();
        for (HistoryEntry entry : historyList) {
            historyText.append("Thời gian:").append(entry.getButtonTextData());
            historyText.append(entry.getFormattedEntry()).append('\n');

        }
        historyTextView.setText(historyText.toString());
    }

    // Lưu danh sách lịch sử vào SharedPreferences
    private void saveHistory() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(historyList);
        editor.putString("historyList", json);
        editor.apply();
    }

    // Đọc danh sách lịch sử từ SharedPreferences
    private void loadHistory() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("historyList", "");
        Type type = new TypeToken<List<HistoryEntry>>() {
        }.getType();
        historyList = gson.fromJson(json, type);

        // Kiểm tra nếu danh sách rỗng, khởi tạo mới
        if (historyList == null) {
            historyList = new ArrayList<>();
        }
    }
}