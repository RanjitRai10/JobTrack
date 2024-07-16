package com.example.jobtrack;

public class Job {
    private String companyName;
    private String deadline;
    private String status;
    private String jobTitle;
    private String url;
    private String resumeUri;

    // Default constructor for Gson
    public Job() {
    }

    public Job(String companyName, String status, String deadline, String jobTitle, String url, String resumeUri) {
        this.companyName = companyName;
        this.status = status;
        this.deadline = deadline;
        this.jobTitle = jobTitle;
        this.url = url;
        this.resumeUri = resumeUri;
    }

    public String getResumeUri() {
        return resumeUri;
    }

    public void setResumeUri(String resumeUri) {
        this.resumeUri = resumeUri;
    }
    // Getters
    public String getCompanyName() {
        return companyName;
    }

    public String getStatus() {
        return status;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getUrl() {
        return url;
    }

    // Setters
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
