package com.example.battleship.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Activity.MainMenu;
import com.example.battleship.Model.FirebaseAuthenticationModel;
import com.example.battleship.Model.FirebaseDatabaseModel;
import com.example.battleship.Model.FirebaseStorageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainMenuViewModel extends AndroidViewModel {

    private final MutableLiveData<HashMap<String, Object>> moveToRoom = new MutableLiveData<>();
    private final String pathRooms = "Rooms";
    private final String pathName = "name";
    private final String pathUser = "user";
    private final String pathShip= "ship";
    private final String pathRole= "role";

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;

    public MainMenuViewModel(@NonNull Application application) {
        super(application);
        firebaseAuth = new FirebaseAuthenticationModel();
        firebaseDatabase = new FirebaseDatabaseModel();
    }

    public LiveData<HashMap<String, Object>> getMoveToRoom() {
        return moveToRoom;
    }

    public void createRoom(String roomName) {
        if (roomName.isEmpty()) {
            Toast.makeText(getApplication(), "Поле пустое", Toast.LENGTH_SHORT).show();
            return;
        }
        String key = firebaseDatabase.push(pathRooms);

        Map<String, Object> values = new HashMap<>();
        values.put(pathName, roomName);
        firebaseDatabase.updateChild("/"+ pathRooms + "/" + key, values);

        values = new HashMap<>();
        values.put(pathUser, firebaseAuth.getUserUID());
        values.put(pathShip, MainMenu.MAX_SHIP_COUNT);
        firebaseDatabase.updateChild("/"+ pathRooms + "/" + key + "/p1", values);

        HashMap<String, Object> value = new HashMap<>();
        value.put(pathRole, MainMenu.REQUEST_HOST);
        value.put(pathName, key);
        moveToRoom.setValue(value);
    }

    public void connectToRoom(String key) {
        if (key.isEmpty()) {
            Toast.makeText(getApplication(), "Комнаты не существует", Toast.LENGTH_SHORT).show();
            return;
        }
        firebaseDatabase.getReference(pathRooms + "/" + key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    firebaseDatabase.setValue(pathRooms + "/" + key + "/p2/" + pathName, firebaseAuth.getUserUID());
                    firebaseDatabase.setValue(pathRooms + "/" + key + "/p2/" + pathShip, MainMenu.MAX_SHIP_COUNT);
                    HashMap<String, Object> value = new HashMap<>();
                    value.put(pathRole, MainMenu.REQUEST_GUEST);
                    value.put(pathName, key);
                    moveToRoom.setValue(value);
                } else {
                    Toast.makeText(getApplication(), "Комнаты не существует", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void removeRoom() {
        firebaseDatabase.remove(pathRooms + "/" + moveToRoom.getValue().get(pathName).toString());
    }
}