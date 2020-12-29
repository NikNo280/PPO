package com.example.battleship.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Activity.PreparationRoom;
import com.example.battleship.Enum.TypeField;
import com.example.battleship.Interface.IButtle;
import com.example.battleship.Model.FirebaseAuthenticationModel;
import com.example.battleship.Model.FirebaseDatabaseModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DisplayViewModel extends AndroidViewModel implements IButtle {

    private final MutableLiveData<int[]> iconId = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isEnd = new MutableLiveData<>();
    private final MutableLiveData<Integer> RestShip = new MutableLiveData<>();
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
    String displayReference;
    String restReference;
    String roomName;
    String role;

    public DisplayViewModel(@NonNull Application application) {
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


    @Override
    public LiveData<int[]> getIcon() {
        return iconId;
    }

    @Override
    public void setPoint(String point) {

    }

    public LiveData<Boolean> getIsEnd() {
        return isEnd;
    }

    public LiveData<Integer> getRestShip() {
        return RestShip;
    }

    public void initialization(String roomName) {
        this.roomName = roomName;
        firebaseDatabase.getReference(pathRooms + roomName + "/p1" + pathUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(firebaseAuth.getUserUID())) {
                    role = "host";
                    displayReference = pathRooms + roomName + "/p1" + pathField;
                    restReference = pathRooms + roomName + "/p1" + pathShip;
                    statisticsReference = pathStatistics + roomName + "/p1";
                } else {
                    role = "guest";
                    displayReference = pathRooms + roomName + "/p2" + pathField;
                    restReference = pathRooms + roomName + "/p2" + pathShip;;
                    statisticsReference = pathStatistics + roomName + "/p2";

                }
                firebaseDatabase.setValue(pathRooms + roomName + pathMessages, role);
                firebaseDatabase.getReference(restReference).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        RestShip.setValue(Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firebaseDatabase.getReference(displayReference).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int[] temp = new int[100];
                        for (DataSnapshot child : snapshot.getChildren()) {
                            temp[Integer.parseInt(Objects.requireNonNull(child.getKey()))] = Integer.parseInt(Objects.requireNonNull(child.getValue()).toString());
                        }
                        setIcon(temp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                firebaseDatabase.getReference(restReference).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (Integer.parseInt(Objects.requireNonNull(snapshot.getValue()).toString()) == 0) {
                            firebaseDatabase.setValue(pathRooms + roomName + pathStatistics, "Finish " + role);
                            addStatistic(PreparationRoom.REST_SHIPS);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                firebaseDatabase.getReference(displayReference).addChildEventListener(new ChildEventListener() {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void addStatistic(int count) {
        firebaseDatabase.setValue(statisticsReference + pathName, firebaseAuth.getUserUID());
        firebaseDatabase.setValue(statisticsReference + pathShip, count);
        isEnd.setValue(true);

    }

    private void setIcon(int position, int type) {
        int[] temp = iconId.getValue();
        if (type == TypeField.HIT.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.HIT.getCodeImage();
            checkDestroy(position);
        } else if (type == TypeField.LOSE.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.LOSE.getCodeImage();
        } else if (type == TypeField.DESTROY.getCodeField()) {
            Objects.requireNonNull(temp)[position] = TypeField.DESTROY.getCodeImage();
        }
        iconId.setValue(temp);
    }

    private void setIcon(int[] temp) {
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == TypeField.EMPTY.getCodeField()) {
                temp[i] = TypeField.EMPTY.getCodeImage();
            } else if (temp[i] == TypeField.HIT.getCodeField()) {
                temp[i] = TypeField.HIT.getCodeImage();
            } else if (temp[i] == TypeField.LOSE.getCodeField()) {
                temp[i] = TypeField.LOSE.getCodeImage();
            } else if (temp[i] == TypeField.DESTROY.getCodeField()) {
                temp[i] = TypeField.DESTROY.getCodeImage();
            } else {
                temp[i] = TypeField.SHIP.getCodeImage();
            }
        }
        iconId.setValue(temp);
    }

    private void checkDestroy(int position) {
        int[] field = iconId.getValue();
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(position);
        if (position % 10 == 0) {
            for (int i = 1; i < 5; i++) {
                if (Objects.requireNonNull(field)[position + i] == TypeField.HIT.getCodeImage()) {
                    temp.add(position + i);
                } else if (field[position + i] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
            }
        } else if (position % 10 == 9) {
            for (int i = 1; i < 5; i++) {
                if (Objects.requireNonNull(field)[position - i] == TypeField.HIT.getCodeImage()) {
                    temp.add(position - i);
                } else if (field[position - i] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
            }
        } else {
            for (int i = 1; i < 5; i++) {
                if (Objects.requireNonNull(field)[position + i] == TypeField.HIT.getCodeImage()) {
                    temp.add(position + i);
                } else if (field[position + i] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
                if (position + i % 10 == 9) {
                    break;
                }
            }
            for (int i = 1; i < 5 - temp.size(); i++) {
                if (position - i % 10 == 0) {
                    break;
                }
                else if (field[position - i] == TypeField.HIT.getCodeImage()) {
                    temp.add(position - i);
                } else if (field[position - i] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
            }
        }

        if (temp.size() != 1) {
            Collections.sort(temp);
            deleteShip(temp, true);
        } else if (position < 10) {
            for (int i = 1; i < 5; i++) {
                if (field[position + i * 10] == TypeField.HIT.getCodeImage()) {
                    temp.add(position + i * 10);
                } else if (field[position + i * 10] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
            }
        } else if (position > 89) {
            for (int i = 1; i < 5; i++) {
                if (field[position - i * 10] == TypeField.HIT.getCodeImage()) {
                    temp.add(position - i * 10);
                } else if (field[position - i * 10] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
            }
        } else {
            for (int i = 1; i < 5; i++) {
                if (field[position + i * 10] == TypeField.HIT.getCodeImage()) {
                    temp.add(position + i * 10);
                } else if (field[position + i * 10] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
                if (position + i > 89) {
                    break;
                }
            }
            for (int i = 1; i < 5 - temp.size(); i++) {
                if (field[position - i * 10] == TypeField.HIT.getCodeImage()) {
                    temp.add(position - i * 10);
                } else if (field[position - i * 10] == TypeField.SHIP.getCodeImage() || field[position + i] == TypeField.DESTROY.getCodeImage()) {
                    return;
                } else {
                    break;
                }
                if (position - i < 10) {
                    break;
                }
            }
            Collections.sort(temp);
            deleteShip(temp, false);
        }
    }

    public void deleteShip(ArrayList<Integer> ship, boolean horizontal) {
        Map<String, Object> temp = new HashMap<>();
        if (horizontal) {
            for (int element : ship) {
                temp.put(String.valueOf(element), 4);
                if (element > 9) {
                    temp.put(String.valueOf(element - 10), 3);
                }
                if (element < 90) {
                    temp.put(String.valueOf(element + 10), 3);
                }
            }
            int edge = ship.get(0);
            if (edge % 10 != 0) {
                temp.put(String.valueOf(edge - 1), 3);
                if (edge > 9) {
                    temp.put(String.valueOf(edge - 11), 3);
                }
                if (edge < 90) {
                    temp.put(String.valueOf(edge + 9), 3);
                }
            }
            edge = ship.get(ship.size() - 1);
            if (edge % 10 != 9) {
                temp.put(String.valueOf(edge + 1), 3);
                if (edge > 9) {
                    temp.put(String.valueOf(edge - 9), 3);
                }
                if (edge < 90) {
                    temp.put(String.valueOf(edge + 11), 3);
                }
            }
        } else {
            for (int element : ship) {
                temp.put(String.valueOf(element), 4);
                if (element % 10 != 0) {
                    temp.put(String.valueOf(element - 1), 3);
                }
                if (element % 10 != 9) {
                    temp.put(String.valueOf(element + 11), 3);
                }
            }
            int edge = ship.get(0);
            if (edge > 9) {
                temp.put(String.valueOf(edge - 10), 3);
                if (edge % 10 != 0) {
                    temp.put(String.valueOf(edge - 11), 3);
                }
                if (edge % 10 != 9) {
                    temp.put(String.valueOf(edge - 9), 3);
                }
            }
            edge = ship.get(ship.size() - 1);
            if (edge < 90) {
                temp.put(String.valueOf(edge + 10), 3);
                if (edge % 10 != 0) {
                    temp.put(String.valueOf(edge + 9), 3);
                }
                if (edge % 10 != 9) {
                    temp.put(String.valueOf(edge + 11), 3);
                }
            }
        }
        firebaseDatabase.updateChild(displayReference, temp);
        int restShip = RestShip.getValue();
        firebaseDatabase.setValue(restReference, --restShip);
    }
}
