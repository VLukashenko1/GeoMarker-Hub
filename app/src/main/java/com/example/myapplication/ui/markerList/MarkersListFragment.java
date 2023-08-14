package com.example.myapplication.ui.markerList;

import static com.example.myapplication.Const.SORT_BY_DESTINATION;
import static com.example.myapplication.Const.SORT_BY_NAME;
import static com.example.myapplication.Const.SORT_BY_NOTE_EXIST;
import static com.example.myapplication.Const.SORT_TYPE_REVERSE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.distance.PointListManager;
import com.example.myapplication.data.distance.PointWithDistance;
import com.example.myapplication.databinding.FragmentDashboardBinding;

import java.util.List;

public class MarkersListFragment extends Fragment {

    private FragmentDashboardBinding binding;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    boolean isFirstSuccessPointGetting = true;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MarkersListViewModel markersListViewModel =
                new ViewModelProvider(this).get(MarkersListViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;

        PointListManager.getInstance().getPointsLiveData().observe(getViewLifecycleOwner(), value -> {
            if (value != null) {
                if (isFirstSuccessPointGetting) {
                    initRecyclerView(value);
                    isFirstSuccessPointGetting = false;
                } else {
                    notifyDataSetChanged(value);
                }
            }
        });

        binding.pointsListSortByDistance.setOnClickListener(view -> PointListManager.getInstance().sort(SORT_BY_DESTINATION));
        binding.pointsListSortByName.setOnClickListener(view -> PointListManager.getInstance().sort(SORT_BY_NAME));
        binding.pointsListSortByNote.setOnClickListener(v-> PointListManager.getInstance().sort(SORT_BY_NOTE_EXIST));
        binding.sortReverceButton.setOnClickListener(v->PointListManager.getInstance().sort(SORT_TYPE_REVERSE));

        return root;
    }

    private void initRecyclerView(List<PointWithDistance> points) {
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), points);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void notifyDataSetChanged(List<PointWithDistance> points) {
        recyclerViewAdapter.changeDataSet(points);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}