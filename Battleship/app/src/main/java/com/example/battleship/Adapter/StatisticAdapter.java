package com.example.battleship.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.battleship.Model.Statistics;
import com.example.battleship.R;

import java.util.List;

public class StatisticAdapter extends ArrayAdapter<Statistics>{

    private final LayoutInflater layoutInflater;
    private final int resource;
    private final List<Statistics> statistics;

    public StatisticAdapter(Context context, int resource, List<Statistics> statistics) {
        super(context, resource, statistics);
        this.statistics = statistics;
        this.resource = resource;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder")
        View view = layoutInflater.inflate(this.resource, parent, false);

        TextView p1Name = view.findViewById(R.id.p1Name);
        TextView p1Count = view.findViewById(R.id.p1Count);
        TextView p2Name = view.findViewById(R.id.p2Name);
        TextView p2Count = view.findViewById(R.id.p2Count);
        TextView nameRoom = view.findViewById(R.id.nameRoom);
        TextView status = view.findViewById(R.id.status);

        Statistics statistic = statistics.get(position);

        p1Name.setText("Игрок 1" + statistic.getP1Name());
        p1Count.setText( "Колличество кораблей:" + statistic.getP1Ship());
        p2Name.setText(statistic.getP2Name() + "Игрок 2");
        if (statistic.isStatus()) {
            status.setText("Вы победили");
            view.setBackgroundColor(Color.GREEN);
        } else {
            status.setText("Вы проиграли");
            view.setBackgroundColor(Color.RED);
        }
        p2Count.setText(statistic.getP2Ship() + "Колличество кораблей:");
        nameRoom.setText("Название комнаты" + statistic.getNameRoom());
        return view;
    }
}
