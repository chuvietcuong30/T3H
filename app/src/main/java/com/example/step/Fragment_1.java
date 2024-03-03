package com.example.step;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viewpage.R;

public class Fragment_1 extends Fragment implements SensorEventListener {

    private SensorManager mSensorManager = null;
    private Sensor stepSensor;
    private int totalSteps = 0;
    private int previewsTotalSteps = 0;
    private ProgressBar progressBar;
    private TextView steps;
    private SharedViewModel sharedViewModel;
    Button goalButton;
    EditText goalEditText;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        steps = view.findViewById(R.id.steps);
        goalButton = view.findViewById(R.id.Goal);
        goalEditText = view.findViewById(R.id.editTextGoal);
        goalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalText = goalEditText.getText().toString();
                if (!goalText.isEmpty()) {
                    int goalValue = Integer.parseInt(goalText);
                    progressBar.setMax(goalValue);
                    goalButton.setText("Mục tiêu: " + goalValue);
                    // Ẩn EditText khi nút Goal được nhấn
                    goalEditText.setVisibility(View.GONE);

                    // Hiển thị lại EditText khi ProgressBar đầy
                    if (progressBar.getProgress() >= goalValue) {
                        goalEditText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        resetSteps();
        loadData();
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        return view;
    }

    private void loadData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        previewsTotalSteps = sharedPref.getInt("key1", 0);
    }

    private void resetSteps() {
        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Nhấn và giữ để đặt lại bước", Toast.LENGTH_SHORT).show();
            }
        });
        steps.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                previewsTotalSteps = totalSteps;
                steps.setText("0");
                progressBar.setProgress(0);
                saveData();
                return true;
            }
        });
    }
    private void saveData() {
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("key1", previewsTotalSteps);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepSensor == null) {
            Toast.makeText(getActivity(), "Thiết bị này không có cảm biến", Toast.LENGTH_SHORT).show();
        } else {
            mSensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            totalSteps = (int) event.values[0];
            int currentStep = totalSteps - previewsTotalSteps;
            steps.setText(String.valueOf(currentStep));
            progressBar.setProgress(currentStep);
            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
            sharedViewModel.setSharedData("" + currentStep);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}