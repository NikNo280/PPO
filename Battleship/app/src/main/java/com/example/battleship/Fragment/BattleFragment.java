package com.example.battleship.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;
import com.example.battleship.Adapter.FieldAdapter;
import com.example.battleship.Interface.IButtle;
import com.example.battleship.R;
import com.example.battleship.ViewModel.BattleViewModel;

public class BattleFragment extends Fragment implements FieldAdapter.ItemListener{

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;
    IButtle shipViewModel;

    protected String[] nameList = new String[100];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shipViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(BattleViewModel.class);

        shipViewModel.getIcon().observe(Objects.requireNonNull(requireActivity()), s -> {
            FieldAdapter fieldAdapter = new FieldAdapter(s, nameList, this);
            recyclerView.setAdapter(fieldAdapter);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_battle, container, false);

        recyclerView = viewHierarchy.findViewById(R.id.recyclerView1);
        layoutManager = new GridLayoutManager(getContext(), 10);
        recyclerView.setLayoutManager(layoutManager);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                nameList[i * 10 + j] = String.valueOf(i) + j;
            }
        }
        return viewHierarchy;
    }

    @Override
    public void onItemClick(String idField) {
        shipViewModel.setPoint(idField);
    }
}