package com.example.battleship.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.Activity.MainMenu;
import com.example.battleship.Adapter.FieldAdapter;
import com.example.battleship.Interface.IButtle;
import com.example.battleship.R;
import com.example.battleship.ViewModel.DisplayViewModel;
import com.example.battleship.ViewModel.PreparationViewModel;

import java.util.Objects;

public class PreparationFragment extends Fragment  implements FieldAdapter.ItemListener {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    IButtle shipViewModel;

    protected String[] nameList = new String[100];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();
        if (intent.getStringExtra(MainMenu.TYPE_VIEW_MODEL).equals(MainMenu.TYPE_VIEW_MODEL_Preparation)) {
            shipViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(PreparationViewModel.class);
        } else {
            shipViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(DisplayViewModel.class);
        }
        shipViewModel.getIcon().observe(Objects.requireNonNull(requireActivity()), s -> {
            FieldAdapter fieldAdapter = new FieldAdapter(s, nameList, this);
            recyclerView.setAdapter(fieldAdapter);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewHierarchy = inflater.inflate(R.layout.fragment_preparation, container, false);

        recyclerView = viewHierarchy.findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getContext(), 10);
        recyclerView.setLayoutManager(gridLayoutManager);
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++) {
                nameList[i * 10 + j] = String.valueOf(i) + j;
            }

        return viewHierarchy;
    }

    @Override
    public void onItemClick(String idField) {
        shipViewModel.setPoint(idField);
    }
}