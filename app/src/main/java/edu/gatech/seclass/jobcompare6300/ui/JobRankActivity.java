package edu.gatech.seclass.jobcompare6300.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.seclass.jobcompare6300.MainMenuActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.manager.JobManager;
import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.models.Job;
import edu.gatech.seclass.jobcompare6300.logic.JobScorer;
import edu.gatech.seclass.jobcompare6300.logic.JobScorer.RankedJob;

public class JobRankActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "JobPrefs";
    private static final String KEY_JOB_1 = "Job_1";
    private static final String KEY_JOB_2 = "Job_2";

    private TableLayout table;
    private Button compareButton;
    private List<RankedJob> rankedJobs;
    private RankedJob selectedJob1 = null;
    private RankedJob selectedJob2 = null;
    private Map<Integer, TableRow> rowMap = new HashMap<>();
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_rank);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.returnButton);
        compareButton = findViewById(R.id.compareButton);
        compareButton.setEnabled(false);

        // back button
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        });

        // compare button
        compareButton.setOnClickListener(v -> {
            if (selectedJob1 != null && selectedJob2 != null) {
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String json1 = gson.toJson(selectedJob1);
                String json2 = gson.toJson(selectedJob2);
                prefs.edit()
                        .putString(KEY_JOB_1, json1)
                        .putString(KEY_JOB_2, json2)
                        .apply();

                // clear memory values of selected jobs
                selectedJob1 = null;
                selectedJob2 = null;

                startActivity(new Intent(this, JobCompareActivity.class));
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        gson = new Gson();
        table = findViewById(R.id.tableRankedJobs);

        List<Job> allJobs = new ArrayList<>(JobManager.getInstance().getOffers());
        if(JobManager.getInstance().hasCurrentJob()){
            allJobs.add(JobManager.getInstance().getCurrentJob());
        }

        // get ranked jobs from job scorer
        ComparisonSettings compSettings = ComparisonSettings.loadCompSettings(this);
        rankedJobs = JobScorer.rankJobs(allJobs, compSettings);
        populateTable();
    }

    private void populateTable() {
        for (int i = 0; i < rankedJobs.size(); i++) {
            int index = i;
            RankedJob rankedJob = rankedJobs.get(index);
            TableRow row = new TableRow(this);

            CheckBox checkbox = new CheckBox(this);
            checkbox.setOnClickListener(view -> onCheckboxClicked(checkbox, index));
            row.addView(checkbox);

            TextView textViewCompany = new TextView(this);
            textViewCompany.setText(rankedJob.job.company+ (rankedJob.job.isCurrentJob ? " - Current" : ""));
            textViewCompany.setTextSize(20);
            textViewCompany.setPadding(8, 8, 8, 8);
            row.addView(textViewCompany);

            TextView textViewTitle = new TextView(this);
            textViewTitle.setText(rankedJob.job.title);
            textViewTitle.setPadding(8, 8, 8, 8);
            row.addView(textViewTitle);

            row.setOnClickListener(view -> onRowClicked(checkbox));
            rowMap.put(index, row);
            table.addView(row);
        }
    }

    private void onCheckboxClicked(CheckBox checkbox, int position) {
        // 1. Only 2 checkboxes can be checked
        RankedJob clickedJob = rankedJobs.get(position);
        Boolean checkboxChecked = checkbox.isChecked();

        if (selectedJob1 == null && checkboxChecked) {
            selectedJob1 = clickedJob;
        }

        if (selectedJob2 == null && !clickedJob.equals(selectedJob1) && checkboxChecked) {
            selectedJob2 = clickedJob;
        }

        // if checkbox is unchecked and it is the same position of selectedJob1
        if (!checkboxChecked && clickedJob.equals(selectedJob1)) {
            selectedJob1 = null;
            enableOtherCheckboxes();
            return;
        }

        if (!checkboxChecked && clickedJob.equals(selectedJob2)) {
            selectedJob2 = null;
            enableOtherCheckboxes();
            return;
        }

        if (selectedJob1 != null && selectedJob2 != null) {
            disableOtherCheckboxes();
            return;
        }
        return;
    }

    private void disableOtherCheckboxes() {
        Integer selectJob1Position = rankedJobs.indexOf(selectedJob1);
        Integer selectJob2Position = rankedJobs.indexOf(selectedJob2);

        for (Map.Entry<Integer, TableRow> entry: rowMap.entrySet()) {
            Integer position = entry.getKey();
            TableRow tableRow = entry.getValue();
            CheckBox checkbox = (CheckBox) tableRow.getChildAt(0);
            if (!position.equals(selectJob1Position) && !position.equals(selectJob2Position)) {
                checkbox.setClickable(false);
            }
        }
        compareButton.setEnabled(true);
    }

    private void enableOtherCheckboxes() {
        Integer selectJob1Position = rankedJobs.indexOf(selectedJob1);
        Integer selectJob2Position = rankedJobs.indexOf(selectedJob2);

        for (Map.Entry<Integer, TableRow> entry: rowMap.entrySet()) {
            Integer position = entry.getKey();
            TableRow tableRow = entry.getValue();
            CheckBox checkbox = (CheckBox) tableRow.getChildAt(0);
            if (!position.equals(selectJob1Position) && !position.equals(selectJob2Position)) {
                checkbox.setClickable(true);
            }
        }
        compareButton.setEnabled(false);
    }

    private void onRowClicked(CheckBox checkbox) {
        if (!checkbox.isClickable()) {
            Toast.makeText(this, "Only 2 jobs can be selected!", Toast.LENGTH_SHORT).show();
        }
    }
}