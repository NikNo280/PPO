package com.example.battleship.ViewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Enum.GameStatus;
import com.example.battleship.Enum.Ship;
import com.example.battleship.Enum.TypeField;
import com.example.battleship.Interface.IButtle;
import com.example.battleship.Model.FirebaseAuthenticationModel;
import com.example.battleship.Model.FirebaseDatabaseModel;
import com.example.battleship.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PreparationViewModel extends AndroidViewModel implements IButtle {
    private final MutableLiveData<Integer> countPoint = new MutableLiveData<>();
    private final MutableLiveData<Integer> resultOfSetting = new MutableLiveData<>();
    private final MutableLiveData<int[]> iconId = new MutableLiveData<>();
    private final MutableLiveData<String> showDialog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> startGame = new MutableLiveData<>();
    private final String pathRooms = "Rooms/";
    private final String pathMessages = "/message";
    private final String pathField= "/field";
    private final String pathUser = "/user";
    boolean deleter = false;
    String tempPoints = "";
    String connectionString = "";
    String roomName = "";

    FirebaseAuthenticationModel firebaseAuth;
    FirebaseDatabaseModel firebaseDatabase;

    public PreparationViewModel(@NonNull Application application) {
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

    public void setShip(Ship count) {
        countPoint.postValue(count.getSize());
    }

    public void deleteShip() {
        deleter = true;
    }

    public LiveData<Integer> getResultShip() {
        return resultOfSetting;
    }

    public LiveData<String> getDialog() {
        return showDialog;
    }

    public LiveData<Boolean> startGame() {
        return startGame;
    }

    public LiveData<int[]> getIcon() {

        return iconId;
    }

    public void initialization(String roomName) {
        this.roomName = roomName;
        firebaseDatabase.getReference(pathRooms + roomName + "/p1" + pathUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message;
                if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(firebaseAuth.getUserUID())) {
                    connectionString = "/" + pathRooms + roomName + "/p1" + pathField;
                    showDialog.setValue("Ожидание входа участников" + " название комнаты: " + roomName);
                    message = GameStatus.WAITING_SECOND_PLAYER.getName();
                } else {
                    message = GameStatus.PLACEMENT_START.getName();
                    connectionString = "/" + pathRooms + roomName + "/p2" + pathField;
                }
                firebaseDatabase.setValue(pathRooms + roomName + pathMessages, message);
                firebaseDatabase.getReference(pathRooms + roomName + pathMessages).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Objects.requireNonNull(snapshot.getValue(String.class)).equals(GameStatus.PLACEMENT_START.getName())) {
                            startGame.setValue(false);
                        } else if (Objects.requireNonNull(snapshot.getValue(String.class)).equals(GameStatus.START_GAME.getName())) {
                            firebaseDatabase.getReference(pathRooms + roomName + pathMessages).removeEventListener(this);
                            startGame.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void battle() {
        firebaseDatabase.getReference(pathRooms + roomName + pathMessages).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String message;
                if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(GameStatus.PLACEMENT_START.getName())) {
                    showDialog.setValue("Ожидание входа участников");
                    message = GameStatus.WAITING_SECOND_PLAYER.getName();
                } else {
                    message = GameStatus.START_GAME.getName();
                }
                firebaseDatabase.setValue(pathRooms + roomName + pathMessages, message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void changeIcon(int[] s) {
        if (connectionString != null) {
            Map<String, Object> values = new HashMap<>();
            for (int i = 0; i < s.length; i++)
                if (s[i] == TypeField.EMPTY.getCodeImage()) {
                    values.put(String.valueOf(i), TypeField.EMPTY.getCodeField());
                } else {
                    values.put(String.valueOf(i), TypeField.SHIP.getCodeField());
                }
            firebaseDatabase.updateChild(connectionString, values);
        }
    }

    public void setPoint(String point) {
        int sizeShip = countPoint.getValue();
        if (sizeShip >= 2) {
            if (tempPoints.length() < 2) {
                tempPoints = point;
            } else {
                countPoint.postValue(0);
                SetShip(tempPoints + point, sizeShip);
                tempPoints = "";
            }
        }
        else if (deleter) {
            SetShip(point, 0);
            deleter = false;
        }
        else if (sizeShip == 1) {
            countPoint.postValue(0);
            SetShip(point, sizeShip);}
    }

    private void SetShip(String value, int sizeShip) {
        int[] temperIconId = iconId.getValue();
        if (sizeShip >= 2) {
            if (!check(Integer.parseInt(value.substring(0, 2))) || !check(Integer.parseInt(value.substring(2, 4)))) {
                Toast.makeText(getApplication(), "Нельзя расположить корабль на этих клетках", Toast.LENGTH_SHORT).show();
                return;
            }
            if (value.charAt(0) == value.charAt(2) && Math.abs(value.charAt(1) - value.charAt(3)) == sizeShip - 1) {
                for (int i = Math.min(value.charAt(1), value.charAt(3)) - '0'; i <= Math.max(value.charAt(1), value.charAt(3)) - '0'; i++) {
                    Objects.requireNonNull(temperIconId)[(value.charAt(0) - '0') * 10 + i] = TypeField.SHIP.getCodeImage();
                }
            } else if (value.charAt(1) == value.charAt(3) && Math.abs(value.charAt(0) - value.charAt(2)) == sizeShip - 1) {
                for (int i = Math.min(value.charAt(0), value.charAt(2)) - '0'; i <= Math.max(value.charAt(0), value.charAt(2)) - '0'; i++) {
                    Objects.requireNonNull(temperIconId)[i * 10 + (value.charAt(1) - '0')] = TypeField.SHIP.getCodeImage();
                }
            } else {
                Toast.makeText(getApplication(), "Нельзя расположить корабль на этих клетках", Toast.LENGTH_SHORT).show();
                return;
            }
            resultOfSetting.setValue(sizeShip);
        } else if (sizeShip == 0 && deleter) {
            int size = deleteShip(Integer.parseInt(value));
            resultOfSetting.setValue(size * -1);
        } else {
            if (!check(Integer.parseInt(value.substring(0, 2)))) {
                Toast.makeText(getApplication(), "Нельзя расположить корабль на этих клетках", Toast.LENGTH_SHORT).show();
                return;
            }
            Objects.requireNonNull(temperIconId)[(value.charAt(0) - '0') * 10 + (value.charAt(1) - '0')] = TypeField.SHIP.getCodeImage();
            resultOfSetting.setValue(sizeShip);
        }
        iconId.setValue(temperIconId);
        changeIcon(temperIconId);
    }

    public int deleteShip(int position) {
        int[] temperIconId = iconId.getValue();
        int fullField = Objects.requireNonNull(temperIconId)[position];
        temperIconId[position] = TypeField.EMPTY.getCodeImage();
        int sizeShip = 1;
        if (position % 10 == 0 && fullField == temperIconId[position + 1]) {
            return sizeShip + deleteShip(position + 1);
        } else if (position % 10 == 9 && fullField == temperIconId[position - 1]) {
            return sizeShip + deleteShip(position - 1);
        } else {
            if (fullField == temperIconId[position + 1])
                sizeShip += deleteShip(position + 1);
            if (fullField == temperIconId[position - 1])
                return sizeShip + deleteShip(position - 1);
        }
        if (position - 10 < 0 && fullField == temperIconId[position + 10]) {
            return sizeShip + deleteShip(position + 10);
        } else if (position + 10 > 100 && fullField == temperIconId[position - 10]) {
            return sizeShip + deleteShip(position - 10);
        } else {
            if (position < 90 && fullField == temperIconId[position + 10])
                sizeShip += deleteShip(position + 10);
            if (position > 9 && fullField == temperIconId[position - 10])
                return sizeShip + deleteShip(position - 10);
        }
        return sizeShip;
    }

    public boolean check(int position) {
        int[] temperIconId = iconId.getValue();
        int emptyField = Objects.requireNonNull(temperIconId)[position];
        int[] massCheck;
        if (position % 10 == 0) {
            massCheck = new int[]{
                    position - 10, position - 9,
                    position, position + 1,
                    position + 10, position + 11
            };
        } else if (position % 10 == 9) {
            massCheck = new int[]{
                    position - 11, position - 10,
                    position - 1, position,
                    position + 9, position + 10
            };
        } else {
            massCheck = new int[]{
                    position - 11, position - 10, position - 9,
                    position - 1, position, position + 1,
                    position + 9, position + 10, position + 11
            };
        }
        for (int item : massCheck) {
            if (item < 100 && item >= 0) {
                if (emptyField != temperIconId[item]) {
                    return false;
                }
            }
        }
        return true;
    }
}
