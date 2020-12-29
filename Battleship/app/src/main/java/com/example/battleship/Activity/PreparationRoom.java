package com.example.battleship.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;

import com.example.battleship.R;
import com.example.battleship.ViewModel.PreparationViewModel;

import java.util.Objects;

import static com.example.battleship.Enum.Ship.FOUR;
import static com.example.battleship.Enum.Ship.ONE;
import static com.example.battleship.Enum.Ship.THREE;
import static com.example.battleship.Enum.Ship.TWO;

public class PreparationRoom extends AppCompatActivity {

    PreparationViewModel shipViewModel;
    Button btn1, btn2, btn3, btn4, btnDelete, btnBattle;
    TextView textView4, textView3, textView2, textView1;
    String roomName;

    private ProgressDialog progressDialog;

    public static final int REST_SHIPS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparation_room);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btnDelete = findViewById(R.id.btnDelete);
        btnBattle = findViewById(R.id.btnButtle);
        textView1 = findViewById(R.id.textView6);
        textView2 = findViewById(R.id.textView5);
        textView3 = findViewById(R.id.textView4);
        textView4 = findViewById(R.id.textView2);

        Intent postIntent = getIntent();
        roomName = postIntent.getStringExtra(MainMenu.ROOM_NAME);
        progressDialog = new ProgressDialog(this);

        shipViewModel = ViewModelProviders.of(this).get(PreparationViewModel.class);

        btn1.setOnClickListener(item -> {
            if (Integer.parseInt(textView1.getText().toString()) > REST_SHIPS)
                shipViewModel.setShip(ONE);
            else
                Toast.makeText(this, "Выберите другую клетку", Toast.LENGTH_SHORT).show();
        });
        btn2.setOnClickListener(item -> {
            if (Integer.parseInt(textView2.getText().toString()) > REST_SHIPS)
                shipViewModel.setShip(TWO);
            else
                Toast.makeText(this, "Выберите другую клетку", Toast.LENGTH_SHORT).show();
        });
        btn3.setOnClickListener(item -> {
            if (Integer.parseInt(textView3.getText().toString()) > REST_SHIPS)
                shipViewModel.setShip(THREE);
            else
                Toast.makeText(this, "Выберите другую клетку", Toast.LENGTH_SHORT).show();
        });
        btn4.setOnClickListener(item -> {
            if (Integer.parseInt(textView4.getText().toString()) > REST_SHIPS)
                shipViewModel.setShip(FOUR);
            else
                Toast.makeText(this,"Выберите другую клетку", Toast.LENGTH_SHORT).show();
        });

        btnBattle.setOnClickListener(item -> {
            if (Integer.parseInt(textView1.getText().toString()) +
                    Integer.parseInt(textView2.getText().toString()) +
                    Integer.parseInt(textView3.getText().toString()) +
                    Integer.parseInt(textView4.getText().toString()) == REST_SHIPS) {
                shipViewModel.battle();
            } else {
                Toast.makeText(this, "Раствате все карабли", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(item -> {
            if (Integer.parseInt(textView1.getText().toString()) +
                    Integer.parseInt(textView2.getText().toString()) +
                    Integer.parseInt(textView3.getText().toString()) +
                    Integer.parseInt(textView4.getText().toString()) != MainMenu.MAX_SHIP_COUNT) {
                shipViewModel.deleteShip();
            }
        });

        shipViewModel.initialization(roomName);
        shipViewModel.getDialog().observe(this, text -> {
            progressDialog.setMessage(text);
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        });

        shipViewModel.startGame().observe(this, status -> {
            if (status) {
                progressDialog.dismiss();
                Intent intent = new Intent(PreparationRoom.this, PlayRoom.class);
                intent.putExtra(MainMenu.ROOM_NAME, roomName);
                intent.putExtra(MainMenu.TYPE_VIEW_MODEL, MainMenu.TYPE_VIEW_MODEL_BATTLE);
                startActivity(intent);
                finish();
            } else {
                progressDialog.dismiss();
            }
        });

        shipViewModel.getResultShip().observe(this, size -> {
            switch (size) {
                case 4:
                    textView4.setText(String.valueOf(Integer.parseInt(textView4.getText().toString()) - 1));
                    break;
                case -4:
                    textView4.setText(String.valueOf(Integer.parseInt(textView4.getText().toString()) + 1));
                    break;
                case 3:
                    textView3.setText(String.valueOf(Integer.parseInt(textView3.getText().toString()) - 1));
                    break;
                case -3:
                    textView3.setText(String.valueOf(Integer.parseInt(textView3.getText().toString()) + 1));
                    break;
                case 2:
                    textView2.setText(String.valueOf(Integer.parseInt(textView2.getText().toString()) - 1));
                    break;
                case -2:
                    textView2.setText(String.valueOf(Integer.parseInt(textView2.getText().toString()) + 1));
                    break;
                case 1:
                    textView1.setText(String.valueOf(Integer.parseInt(textView1.getText().toString()) - 1));
                    break;
                case -1:
                    textView1.setText(String.valueOf(Integer.parseInt(textView1.getText().toString()) + 1));
                    break;
            }
        });
    }
}