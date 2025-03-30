package com.example.amoaiproject.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amoaiproject.R;
import com.example.amoaiproject.adapter.HistoryAdapter;
import com.example.amoaiproject.database.AppDatabase;
import com.example.amoaiproject.database.History;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class FragmentHistory extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;

    public FragmentHistory() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        historyAdapter = new HistoryAdapter();
        recyclerView.setAdapter(historyAdapter);

        // Set listener untuk tombol delete pada setiap item
        historyAdapter.setOnDeleteClickListener(history -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete History")
                    .setMessage("Are you sure you want to delete this record?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase.getInstance(getContext()).historyDao().deleteHistory(history);
                            if(getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    loadHistory();
                                    Toast.makeText(getContext(), "History deleted", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }).start();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        loadHistory();
    }

    private void loadHistory() {
        new Thread(() -> {
            List<History> historyList = AppDatabase.getInstance(getContext()).historyDao().getAllHistory();
            if(getActivity() != null) {
                getActivity().runOnUiThread(() -> historyAdapter.setHistoryList(historyList));
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistory();
    }
}