package com.example.battleship.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import java.util.Objects;

import com.example.battleship.R;
import com.example.battleship.ViewModel.BattleViewModel;
import com.example.battleship.ViewModel.DisplayViewModel;

public class PlayRoom extends AppCompatActivity {

    BattleViewModel battleViewModel;
    DisplayViewModel displayViewModel;
    TextView currentPlayer;
    String roomName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_room);
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent postIntent = getIntent();
        roomName = postIntent.getStringExtra(MainMenu.ROOM_NAME);
        displayViewModel = ViewModelProviders.of(this).get(DisplayViewModel.class);
        battleViewModel = ViewModelProviders.of(this).get(BattleViewModel.class);
        currentPlayer = findViewById(R.id.currentPlayer);
        battleViewModel.initialization(roomName);
        displayViewModel.initialization(roomName);
        battleViewModel.getInformation().observe(this, text -> currentPlayer.setText(text));

        battleViewModel.getIsEnd().observe(this, text -> {
            Toast.makeText(PlayRoom.this, "Вы победили!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        });

        displayViewModel.getIsEnd().observe(this, text -> {
            currentPlayer.setText("Вы проиграли!");
            Toast.makeText(PlayRoom.this, "Вы проиграли!", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        });

        displayViewModel.getRestShip().observe(this, value -> battleViewModel.setRestShip(value));
    }

}