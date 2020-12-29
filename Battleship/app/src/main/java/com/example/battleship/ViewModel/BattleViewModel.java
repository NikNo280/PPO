package com.example.battleship.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Enum.TypeField;
import com.example.battleship.Interface.IButtle;
import com.example.battleship.Model.FirebaseAuthenticationModel;
import com.example.battleship.Model.FirebaseDatabaseModel;
import com.example.battleship.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.example.battleship.Enum.GameStatus.*;

public class BattleViewModel extends AndroidViewModel implements IButtle {

    private final MutableLiveData<int[]> iconId = new MutableLiveData<>();
    private final MutableLiveData<String> information = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEnd = new MutableLiveData<>();
    boolean available;
    int RestShip;
    private final String pathRooms = "Rooms/";
    private final String pathStatistics = "Statistics/";
    private final String pathMessages = "/message";
    private final String pathName = "/name";
    private final String pathShip= "/ship";
    private final String pathField= "/field";
    private final String pathUser = "/user";
    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;
    String statisticsReference;
    String battleReference;
    String roomName;
    String role;


    public BattleViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseDatabase = new FirebaseDatabaseModel();
        int[] tempId = new int[100];
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                tempId[i * 10 + j] = TypeField.EMPTY.getCodeImage();
            }
        iconId.setValue(tempId);
    }

    public LiveData<String> getInformation() {
        return information;
    }

    public LiveData<Boolean> getIsEnd() {
        return isEnd;
    }

    public void setRestShip(int restShip) {
        RestShip = restShip;
    }

    @Override
    public void setPoint(String point) {
        if (available) {
            firebaseDatabase.getReference(battleReference + '/' + Integer.parseInt(point)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString()) == TypeField.EMPTY.getCodeField()) {
                        firebaseDatabase.setValue(pathRooms + roomName + pathMessages, role);
                        firebaseDatabase.setValue(battleReference + Integer.parseInt(point), TypeField.LOSE.getCodeField());
                        available = false;
                        information.setValue("Ход противника");
                    } else if (Integer.parseInt(snapshot.getValue().toString()) == TypeField.SHIP.getCodeField()) {
                        firebaseDatabase.setValue(battleReference + Integer.parseInt(point), TypeField.HIT.getCodeField());
                    } else {
                        Toast.makeText(getApplication(), "Выберите другую клетку", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public LiveData<int[]> getIcon() {
        return iconId;
    }

    public void initialization(String roomName) {
        this.roomName = roomName;
        firebaseDatabase.getReference(pathRooms + roomName + "/p1" + pathUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(firebaseAuth.getUserUID())) {
                    role = "host";
                    battleReference = pathRooms + roomName + "/p2" + pathField;
                    statisticsReference = pathStatistics + roomName + "/p1";
                } else {
                    role = "guest";
                    battleReference = pathRooms + roomName + "/p1" + pathField;
                    statisticsReference = pathStatistics + roomName + "/p2";
                }
                firebaseDatabase.getReference(pathRooms + roomName + pathMessages).setValue(role);
                firebaseDatabase.getReference(battleReference).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        setIcon(Integer.parseInt(Objects.requireNonNull(snapshot.getKey())), Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString()));
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firebaseDatabase.getReference(pathRooms + roomName + pathMessages).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (role.equals("host")) {
                            if (Objects.equals(snapshot.getValue(String.class), GUEST.getName())) {
                                available = true;
                                information.setValue("Ваш ход");
                            } else if (Objects.equals(snapshot.getValue(String.class), FINISH_GUEST.getName())) {
                                available = false;
                                addStatistic(RestShip);
                            }
                        } else {
                            if (Objects.equals(snapshot.getValue(String.class), HOST.getName())) {
                                available = true;
                                information.setValue("Ваш ход");
                            } else if (Objects.equals(snapshot.getValue(String.class), FINISH_HOST.getName())) {
                                available = false;
                                addStatistic(RestShip);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firebaseDatabase.getReference(pathRooms + roomName + pathName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        firebaseDatabase.setValue(pathStatistics + roomName + pathName, Objects.requireNonNull(snapshot.getValue()).toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                information.setValue("Ход противника");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void setIcon(int position, int type) {
        int[] temp = iconId.getValue();
        if (type == TypeField.HIT.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.HIT.getCodeImage();
        } else if (type == TypeField.LOSE.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.LOSE.getCodeImage();
        } else if (type == TypeField.DESTROY.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.DESTROY.getCodeImage();
        }
        iconId.setValue(temp);
    }

    private void addStatistic(int count) {
        firebaseDatabase.setValue(statisticsReference + pathName, firebaseAuth.getUserUID());
        firebaseDatabase.setValue(statisticsReference + pathShip, count);
        isEnd.setValue(true);
    }
}
