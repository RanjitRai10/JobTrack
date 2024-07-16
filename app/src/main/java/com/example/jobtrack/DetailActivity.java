package com.example.jobtrack;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Find the TextViews
        TextView companyValue = findViewById(R.id.companyValue);
        TextView jobTitleValue = findViewById(R.id.jobTitleValue);
        TextView urlValue = findViewById(R.id.urlValue);
        TextView deadlineValue = findViewById(R.id.deadlineValue);
        TextView statusValue = findViewById(R.id.statusValue);
        TextView resumeValue = findViewById(R.id.resumeValue);

        // Get the intent that started this activity
        Intent intent = getIntent();

        // Retrieve the data from the intent
        String company = intent.getStringExtra("company");
        String jobTitle = intent.getStringExtra("jobTitle");
        String url = intent.getStringExtra("url");
        String deadline = intent.getStringExtra("deadline");
        String status = intent.getStringExtra("status");
        String resumeUri = intent.getStringExtra("resumeUri");

        // Set the data to the TextViews
        companyValue.setText(company);
        jobTitleValue.setText(jobTitle);
        urlValue.setText(url);
        deadlineValue.setText(deadline);
        statusValue.setText(status);

        // Check if resumeUri is not null or empty
        if (resumeUri != null && !resumeUri.isEmpty()) {
            resumeValue.setText("Uploaded resume.pdf");
            resumeValue.setOnClickListener(v -> openPDF(resumeUri));
        } else {
            resumeValue.setText("No resume uploaded");
        }
    }

    private void openPDF(String resumeUri) {
        if (resumeUri == null || resumeUri.isEmpty()) {
            Toast.makeText(this, "No PDF available", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Uri pdfUri = Uri.parse(resumeUri);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "No application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error opening PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
