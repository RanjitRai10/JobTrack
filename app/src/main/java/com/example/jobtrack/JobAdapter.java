package com.example.jobtrack;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private List<Job> jobList;
    private Context context;
    private OnItemLongClickListener onItemLongClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public JobAdapter(Context context, List<Job> jobList, OnItemLongClickListener onItemLongClickListener) {
        this.context = context;
        this.jobList = jobList;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler, parent, false);
        return new JobViewHolder(view, onItemLongClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.companyName.setText(job.getCompanyName());
        holder.status.setText(job.getStatus());
        holder.deadline.setText(job.getDeadline());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("company", job.getCompanyName());
            intent.putExtra("jobTitle", job.getJobTitle());
            intent.putExtra("url", job.getUrl());
            intent.putExtra("deadline", job.getDeadline());
            intent.putExtra("status", job.getStatus());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView companyName, status, deadline;
        OnItemLongClickListener onItemLongClickListener;

        public JobViewHolder(@NonNull View itemView, OnItemLongClickListener onItemLongClickListener) {
            super(itemView);
            companyName = itemView.findViewById(R.id.companyName);
            status = itemView.findViewById(R.id.status);
            deadline = itemView.findViewById(R.id.deadline);
            this.onItemLongClickListener = onItemLongClickListener;

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(getAdapterPosition());
            return true;
        }
    }
}
