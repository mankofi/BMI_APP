package com.example.bmi_app;

import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.github.anastr.speedviewlib.SpeedView;
import com.github.anastr.speedviewlib.components.indicators.NormalIndicator;

public class MainActivity extends AppCompatActivity {
    private EditText heightEditText, weightEditText;
    private SpeedView speedView;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heightEditText = findViewById(R.id.heightEditText);
        weightEditText = findViewById(R.id.weightEditText);
        speedView = findViewById(R.id.speedView);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        setupSpeedView();
    }

    private void setupSpeedView() {
        speedView.setIndicator(new NormalIndicator(this));
        speedView.setMinSpeed(0);
        speedView.setMaxSpeed(40);
        speedView.setUnit("BMI");
        speedView.setTickNumber(8);
        speedView.setWithTremble(false);
        speedView.setSpeedTextSize(40);

        // Add sections for BMI categories
        speedView.addSections(
                new com.github.anastr.speedviewlib.components.Section(0f, 18.5f, getColor(R.color.underweight)),
                new com.github.anastr.speedviewlib.components.Section(18.5f, 25f, getColor(R.color.normal)),
                new com.github.anastr.speedviewlib.components.Section(25f, 30f, getColor(R.color.overweight)),
                new com.github.anastr.speedviewlib.components.Section(30f, 40f, getColor(R.color.obese))
        );
    }

    public void calculateBMI(View view) {
        try {
            // Trigger haptic feedback
            if (vibrator != null && vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            }

            double height = Double.parseDouble(heightEditText.getText().toString());
            double weight = Double.parseDouble(weightEditText.getText().toString());

            if (height <= 0 || weight <= 0) {
                Toast.makeText(this, "Please enter valid values", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert height from cm to m
            height = height / 100;
            double bmi = weight / (height * height);

            // Animate the speed view to the calculated BMI
            speedView.speedTo((float) bmi, 1500);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter both height and weight", Toast.LENGTH_SHORT).show();
        }
    }
}