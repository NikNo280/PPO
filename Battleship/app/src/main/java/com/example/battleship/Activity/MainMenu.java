package com.example.battleship.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.battleship.R;
import com.example.battleship.ViewModel.MainMenuViewModel;

public class MainMenu extends AppCompatActivity {
    public static final int MAX_SHIP_COUNT = 10;
    public static final int REQUEST_HOST = 101;
    public static final int REQUEST_GUEST = 102;
    public static final String TYPE_VIEW_MODEL = "ViewModel";
    public static final String TYPE_VIEW_MODEL_Preparation = "Preparation";
    public static final String TYPE_VIEW_MODEL_BATTLE = "Battle";
    public static final String ROOM_NAME = "roomName";

    private EditText textView;
    private MainMenuViewModel mainMenuViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mainMenuViewModel = ViewModelProviders.of(this).get(MainMenuViewModel.class);
        mainMenuViewModel.getMoveToRoom().observe(this, text -> goToRoom(Integer.parseInt(text.get("role").toString()), text.get("name").toString()));
        textView = findViewById(R.id.room);
    }

    public void goToRoom(int index, String key) {
        Intent intent = new Intent(MainMenu.this, PreparationRoom.class);
        intent.putExtra(ROOM_NAME, key);
        intent.putExtra("ViewModel", TYPE_VIEW_MODEL_Preparation);
        startActivityForResult(intent, index);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_HOST) {
            if (resultCode == 0) {
                Toast.makeText(MainMenu.this, "Вы проиграли", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_GUEST) {
            if (resultCode == 0) {
                mainMenuViewModel.removeRoom();
                Toast.makeText(MainMenu.this, "Вы победили", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void btnGoTStatistic(View view) {
        Intent intent = new Intent(MainMenu.this, StatisticActivity.class);
        startActivity(intent);
    }

    public void btnGoToProfile(View view) {
        Intent intent  = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void btnConnect(View view) {
        findViewById(R.id.connect).setOnClickListener(v -> mainMenuViewModel.connectToRoom(textView.getText().toString()));
    }

    public void btnCreate(View view) {
        findViewById(R.id.create).setOnClickListener(v -> mainMenuViewModel.createRoom(textView.getText().toString()));
    }
}