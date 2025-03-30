package com.example.amoaiproject.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.amoaiproject.R;
import com.example.amoaiproject.database.History;
import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<History> historyList = new ArrayList<>();
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(History history);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    public void setHistoryList(List<History> list) {
        this.historyList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        History history = historyList.get(position);
        holder.tvTitle.setText("Response");
        holder.tvResponse.setText(history.responseText);
        holder.tvTimestamp.setText(history.timestamp);

        // Set listener pada tombol hapus
        holder.btnDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(history);
            }
        });

        // Expand/Collapse untuk jawaban
        holder.tvResponse.setOnClickListener(v -> {
            if (holder.tvResponse.getMaxLines() == 3) {
                holder.tvResponse.setMaxLines(Integer.MAX_VALUE);
                holder.tvResponse.setEllipsize(null);
            } else {
                holder.tvResponse.setMaxLines(3);
                holder.tvResponse.setEllipsize(TextUtils.TruncateAt.END);
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void removeItem(History history) {
        int pos = historyList.indexOf(history);
        if (pos != -1) {
            historyList.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvResponse, tvTimestamp;
        ImageButton btnDelete;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvResponse = itemView.findViewById(R.id.tvResponse);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}