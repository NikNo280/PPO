package com.example.battleship.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import androidx.lifecycle.ViewModelProviders;

import com.example.battleship.Adapter.StatisticAdapter;
import com.example.battleship.R;
import com.example.battleship.ViewModel.StatisticViewModel;

public class StatisticActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        StatisticViewModel statisticViewModel = ViewModelProviders.of(this).get(StatisticViewModel.class);

        listView = findViewById(R.id.statisticList);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Ожидание");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        statisticViewModel.getListStatistics().observe(this, list -> {
            StatisticAdapter stateAdapter = new StatisticAdapter(getApplicationContext(), R.layout.statistic_game, list);
            listView.setAdapter(stateAdapter);
            progressDialog.dismiss();
        });
        statisticViewModel.readAllName();
    }
}