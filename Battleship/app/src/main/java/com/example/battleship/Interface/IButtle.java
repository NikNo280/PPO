package com.example.battleship.Interface;

import androidx.lifecycle.LiveData;

public interface IButtle {
    void setPoint(String point);
    LiveData<int[]> getIcon();
}
