package com.example.battleship.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Model.FirebaseAuthenticationModel;
import com.example.battleship.Model.FirebaseDatabaseModel;
import com.example.battleship.Model.Statistics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StatisticViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Statistics>> listStatistics = new MutableLiveData<>();

    FirebaseDatabaseModel firebaseDatabase;
    FirebaseAuthenticationModel firebaseAuth;
    HashMap<String, List<String>> names;
    private final String pathName = "name";
    private final String pathShip= "ship";

    public StatisticViewModel(@NonNull Application application) {
        super(application);
        firebaseDatabase = new FirebaseDatabaseModel();
        firebaseAuth = new FirebaseAuthenticationModel();
        names = new HashMap<>();
    }

    public LiveData<List<Statistics>> getListStatistics() {
        return listStatistics;
    }

    public void readAllName() {
        firebaseDatabase.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    List<String> temp = new ArrayList<>();
                    temp.add(Objects.requireNonNull(child.child("userName").getValue()).toString());
                    temp.add(Objects.requireNonNull(child.child("Image").getValue()).toString());
                    names.put(child.getKey(), temp);
                }
                addListenerStatistic();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addListenerStatistic() {
        firebaseDatabase.getReference("Statistic").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String p1Name = null, p2Name = null, p1Count = null, p2Count = null, point, currentName = firebaseAuth.getUserUID(), nameRoom = null;
                List<Statistics> statistics = new ArrayList();
                for (DataSnapshot child : snapshot.getChildren()) {
                    for (DataSnapshot child1 : child.getChildren()) {
                        point = child1.getKey();
                        if (!Objects.requireNonNull(point).equals(pathName)) {
                            for (DataSnapshot child2 : child1.getChildren()) {
                                if (point.equals("p1")) {
                                    if (Objects.equals(child2.getKey(), pathShip)) {
                                        p1Count = Objects.requireNonNull(child2.getValue()).toString();
                                    } else {
                                        p1Name = Objects.requireNonNull(child2.getValue()).toString();
                                    }
                                } else {
                                    if (Objects.equals(child2.getKey(), pathShip)) {
                                        p2Count = Objects.requireNonNull(child2.getValue()).toString();
                                    } else {
                                        p2Name = Objects.requireNonNull(child2.getValue()).toString();
                                    }
                                }
                            }
                        } else {
                            nameRoom = Objects.requireNonNull(child1.getValue()).toString();
                        }
                    }
                    if (p1Name.equals(currentName)) {
                        boolean status = !p1Count.equals("0");
                        statistics.add(new Statistics(names.get(p1Name).get(0), names.get(p2Name).get(0), nameRoom, status, Integer.parseInt(p1Count), Integer.parseInt(p2Count)));
                    } else if (p2Name.equals(currentName)) {
                        boolean status = !p2Count.equals("0");
                        statistics.add(new Statistics(names.get(p1Name).get(0), names.get(p2Name).get(0) , nameRoom, status, Integer.parseInt(p1Count), Integer.parseInt(p2Count)));
                    }
                }
                listStatistics.setValue(statistics);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}