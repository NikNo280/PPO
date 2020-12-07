package com.example.myconverter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ConvertActivity extends AppCompatActivity {

    Spinner spinner_input;
    Spinner spinner_output;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert);
        Intent intent = getIntent();
        int layoutId = 0;
        switch (intent.getStringExtra("token"))
        {
            case "distance": layoutId = R.array.spinnerItemDistance; break;
            case "weight"  : layoutId = R.array.spinnerItemWeight; break;
            case "currency": layoutId = R.array.spinnerItemCurrency; break;
        }

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, layoutId, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_input = findViewById(R.id.input_spinner);
        spinner_output = findViewById(R.id.output_spinner);
        spinner_input.setAdapter(adapter);
        spinner_output.setAdapter(adapter);
    }
}