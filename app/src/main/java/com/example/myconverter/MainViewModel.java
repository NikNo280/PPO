package com.example.myconverter;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Objects;

public class MainViewModel extends AndroidViewModel {
    private final MutableLiveData<String> spinnerInputLiveData = new MutableLiveData<>("");
    private final MutableLiveData<String> spinnerOutputLiveData = new MutableLiveData<>("");
    private final MutableLiveData<String> inputEditLiveData = new MutableLiveData<>("");
    private final MutableLiveData<String> outputEditLiveData = new MutableLiveData<>("");

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public void setSpinnerInputLiveData(String item)
    {
        spinnerInputLiveData.setValue(item);
    }

    public void setSpinnerOutputLiveData(String item)
    {
        spinnerOutputLiveData.setValue(item);
    }

    public void exchangeLiveData()
    {
        String temp = inputEditLiveData.getValue();
        inputEditLiveData.setValue(outputEditLiveData.getValue());
        outputEditLiveData.setValue(temp);
    }

    public void addValue(String str)
    {
        inputEditLiveData.setValue(inputEditLiveData.getValue() + str);
    }

    public void addPoint()
    {
        if(Objects.requireNonNull(inputEditLiveData.getValue()).length() > 0 && inputEditLiveData.getValue().indexOf(".") < 1)
        {
            inputEditLiveData.setValue(inputEditLiveData.getValue() + ".");
        }
    }

    public void clearData(){
        inputEditLiveData.setValue("");
    }
    public LiveData<String> getInputEditData()
    {
        return inputEditLiveData;
    }

    public LiveData<String> getOutputEditData()
    {
        return outputEditLiveData;
    }

    public void convert()
    {
        String result = Converter.convert(spinnerInputLiveData.getValue(), spinnerOutputLiveData.getValue(), inputEditLiveData.getValue());
        if(Objects.requireNonNull(result) != "ERROR")
        {
            outputEditLiveData.setValue(result);
        }
        else
        {
            Toast toast = Toast.makeText(getApplication(), "Empty set", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void saveInBuffer(int field, ClipboardManager clipboardManager) {
        switch (field) {
            case 1:
                ClipData clipData = ClipData.newPlainText("text", inputEditLiveData.getValue());
                clipboardManager.setPrimaryClip(clipData);
                break;
            case 2:
                clipData = ClipData.newPlainText("text", outputEditLiveData.getValue());
                clipboardManager.setPrimaryClip(clipData);
                break;
        }
        Toast toast = Toast.makeText(getApplication(), "Save", Toast.LENGTH_SHORT);
        toast.show();

    }
}
