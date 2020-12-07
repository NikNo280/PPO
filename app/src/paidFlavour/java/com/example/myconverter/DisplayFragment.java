package com.example.myconverter;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

public class DisplayFragment extends Fragment {

    MainViewModel mainViewModel;
    ClipboardManager clipboard;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_display, container, false);
        EditText editInput = layout.findViewById(R.id.input_edit);
        EditText editOutput = layout.findViewById(R.id.output_edit);
        Spinner spinnerInput = layout.findViewById(R.id.input_spinner);
        Spinner spinnerOutput = layout.findViewById(R.id.output_spinner);

        spinnerInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                mainViewModel.setSpinnerInputLiveData(spinnerInput.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerOutput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent,
                                       View itemSelected, int selectedItemPosition, long selectedId) {

                mainViewModel.setSpinnerOutputLiveData(spinnerOutput.getSelectedItem().toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mainViewModel.getInputEditData().observe(requireActivity(), value -> editInput.setText(value));
        mainViewModel.getOutputEditData().observe(requireActivity(), value -> editOutput.setText(value));
        clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        layout.findViewById(R.id.exchange_button).setOnClickListener(item -> mainViewModel.exchangeLiveData());
        layout.findViewById(R.id.convert_button).setOnClickListener(item -> mainViewModel.convert());
        layout.findViewById(R.id.save_input_button).setOnClickListener(item -> mainViewModel.saveInBuffer(1, clipboard));
        layout.findViewById(R.id.save_output_button).setOnClickListener(item -> mainViewModel.saveInBuffer(2, clipboard));
        return layout;
    }
}