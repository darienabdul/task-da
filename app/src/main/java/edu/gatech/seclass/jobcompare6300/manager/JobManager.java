package edu.gatech.seclass.jobcompare6300.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.models.Job;

public class JobManager {
    private static final String PREFS_NAME = "JobPrefs";
    private static final String KEY_CURRENT_JOB = "CurrentJob";
    private static final String KEY_JOB_OFFERS = "JobOffers";

    private static JobManager instance;
    private Job currentJob;
    private List<Job> jobOffers;

    private final SharedPreferences prefs;
    private final Gson gson;

    private JobManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        jobOffers = new ArrayList<>();
//        clearAllJobsData(); // ONLY FOR DEV/TESTING USE - REMOVE BEFORE SUBMISSION
        loadData();
    }

    public static JobManager initialize(Context context) {
        if (instance == null) {
            instance = new JobManager(context);
        }
        return instance;
    }

    public static JobManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("JobManager not initialized. Call JobManager.initialize(context) first.");
        }
        return instance;
    }

    public void addOffer(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job cannot be null");
        }
        jobOffers.add(job);
        persistJobOffers();
    }

    public void saveCurrentJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("job cannot be null");
        }
        this.currentJob = job;
        persistCurrentJob();
    }

    public boolean hasCurrentJob(){
        return currentJob != null;
    }

    public List<Job> getOffers() {
        return jobOffers;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public boolean enableComparison(){
        return (currentJob == null) ? getOfferCount()>=2 : getOfferCount()>=1;
    }

    public int getOfferCount(){
        return jobOffers.size();
    }

    private void persistCurrentJob() {
        String json = gson.toJson(currentJob);
        prefs.edit().putString(KEY_CURRENT_JOB, json).apply();
    }

    private void persistJobOffers() {
        String json = gson.toJson(jobOffers);
        prefs.edit().putString(KEY_JOB_OFFERS, json).apply();
    }

    // ONLY FOR DEV/TESTING USE
    // TODO: REMOVE BEFORE SUBMISSION
    private void clearAllJobsData() {
        currentJob = null;
        jobOffers.clear();
        prefs.edit()
                .remove(KEY_CURRENT_JOB)
                .remove(KEY_JOB_OFFERS)
                .apply();
    }

    private void loadData() {
        String currentJobJson = prefs.getString(KEY_CURRENT_JOB, null);
        if (currentJobJson != null) {
            currentJob = gson.fromJson(currentJobJson, Job.class);
        }

        String offersJson = prefs.getString(KEY_JOB_OFFERS, null);
        if (offersJson != null) {
            Job[] jobs = gson.fromJson(offersJson, Job[].class);
            jobOffers = new ArrayList<>(Arrays.asList(jobs));
        } else {
            jobOffers = new ArrayList<>();
        }
    }
}
