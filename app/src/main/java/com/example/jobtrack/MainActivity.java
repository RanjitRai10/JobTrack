package com.example.jobtrack;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private Uri selectedResumeUri;
    private ActivityResultLauncher<String> pdfPickerLauncher;
    private TextView selectedResumeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ActivityResultLauncher
        pdfPickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                selectedResumeUri = uri;
                selectedResumeTextView.setText("Selected Resume: " + uri.getLastPathSegment());
                selectedResumeTextView.setVisibility(View.VISIBLE);
            }
        });

        // Floating Action Button
        FloatingActionButton fb = findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFloatAction();
            }
        });

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load job list from SharedPreferences
        jobList = loadJobsFromSharedPreferences();

        // Setup Adapter
        jobAdapter = new JobAdapter(this, jobList, new JobAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                showDeleteConfirmationDialog(position);
            }
        });
        recyclerView.setAdapter(jobAdapter);
    }

    private void showFloatAction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Job");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_job_layout, null);
        builder.setView(dialogView);

        EditText companyNameInput = dialogView.findViewById(R.id.edit_company_name);
        EditText jobStatusInput = dialogView.findViewById(R.id.edit_job_status);
        EditText deadlineInput = dialogView.findViewById(R.id.edit_deadline);
        EditText jobTitleInput = dialogView.findViewById(R.id.edit_job_title);
        EditText jobLinkInput = dialogView.findViewById(R.id.edit_job_link);
        Button uploadResume = dialogView.findViewById(R.id.upload_resume_button);
        selectedResumeTextView = dialogView.findViewById(R.id.selected_resume_text_view);

        uploadResume.setOnClickListener(v -> pdfPickerLauncher.launch("application/pdf"));

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String companyName = companyNameInput.getText().toString();
                String jobStatus = jobStatusInput.getText().toString();
                String deadline = deadlineInput.getText().toString();
                String jobTitle = jobTitleInput.getText().toString();
                String jobLink = jobLinkInput.getText().toString();

                // Validate inputs
                if (!companyName.isEmpty() && !jobStatus.isEmpty() && !deadline.isEmpty() && !jobTitle.isEmpty() && !jobLink.isEmpty()) {
                    // Create new Job object
                    Job newJob = new Job(companyName, jobStatus, deadline, jobTitle, jobLink, selectedResumeUri != null ? selectedResumeUri.toString() : null);
                    // Add the new job to the jobList
                    jobList.add(newJob);
                    // Save the new job to SharedPreferences
                    saveJobsToSharedPreferences();
                    // Notify the adapter of the change
                    jobAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Job added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // Method to save jobs to SharedPreferences
    private void saveJobsToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("jobPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(jobList);
        editor.putString("jobList", json);
        editor.apply();
    }

    // Method to load jobs from SharedPreferences
    private List<Job> loadJobsFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("jobPrefs", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("jobList", null);
        Type type = new TypeToken<ArrayList<Job>>() {}.getType();
        return json != null ? gson.fromJson(json, type) : new ArrayList<>();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Job")
                .setMessage("Are you sure you want to delete this job?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jobList.remove(position);
                        jobAdapter.notifyDataSetChanged();
                        saveJobsToSharedPreferences();
                        Toast.makeText(MainActivity.this, "Job deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
