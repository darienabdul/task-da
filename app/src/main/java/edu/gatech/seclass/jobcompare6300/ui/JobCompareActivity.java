package edu.gatech.seclass.jobcompare6300.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.Locale;

import edu.gatech.seclass.jobcompare6300.MainMenuActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.logic.JobScorer.RankedJob;

public class JobCompareActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "JobPrefs";
    private static final String KEY_JOB_1 = "Job_1";
    private static final String KEY_JOB_2 = "Job_2";
    private final Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_compare);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.button_back);
        Button compareAgainButton = findViewById(R.id.button_compare_again);
        TableLayout table = findViewById(R.id.table_comparison);

        // back button
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        });

        // compare again button
        compareAgainButton.setOnClickListener(v -> {
            startActivity(new Intent(this, JobRankActivity.class));
            finish();
        });

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json1 = prefs.getString(KEY_JOB_1, null);
        String json2 = prefs.getString(KEY_JOB_2, null);

        if (json1 != null && json2 != null) {
            RankedJob rankedJob1 = gson.fromJson(json1, RankedJob.class);
            RankedJob rankedJob2 = gson.fromJson(json2, RankedJob.class);

            addRow(table, "Title", rankedJob1.job.title, rankedJob2.job.title);
            addRow(table, "Company", rankedJob1.job.company, rankedJob2.job.company);
            addRow(table, "City", rankedJob1.job.location.city, rankedJob2.job.location.state);
            addRow(table, "Salary Adjusted", String.format(Locale.US, "%.2f", rankedJob1.job.getAdjustedSalary()), String.format(Locale.US, "%.2f", rankedJob2.job.getAdjustedSalary()));
            addRow(table, "Bonus Adjusted", String.format(Locale.US, "%.2f", rankedJob1.job.getAdjustedBonus()), String.format(Locale.US, "%.2f", rankedJob2.job.getAdjustedBonus()));
            addRow(table, "401K Match", String.valueOf(rankedJob1.job.match401K), String.valueOf(rankedJob2.job.match401K));
            addRow(table, "Internet Stipend", String.valueOf(rankedJob1.job.internetStipend), String.valueOf(rankedJob2.job.internetStipend));
            addRow(table, "Accident Insurance", String.valueOf(rankedJob1.job.accidentInsurance), String.valueOf(rankedJob2.job.accidentInsurance));
            addRow(table, "Tuition Reimbursement", String.valueOf(rankedJob1.job.tuitionReimbursement), String.valueOf(rankedJob2.job.tuitionReimbursement));
            addRow(table, "Job Score", String.format(Locale.US, "%.2f", rankedJob1.score), String.format(Locale.US, "%.2f", rankedJob2.score));

            prefs.edit()
                    .remove(KEY_JOB_1)
                    .remove(KEY_JOB_2)
                    .apply();
        } else {
            addRow(table, "No data", "-", "-");
        }
    }

    private void addRow(TableLayout table, String label, String value1, String value2) {
        TableRow row = new TableRow(this);

        TextView tvLabel = new TextView(this);
        tvLabel.setText(label);
        tvLabel.setTypeface(null, Typeface.BOLD);
        tvLabel.setTextSize(18);
        tvLabel.setPadding(8, 8, 8, 8);

        TextView tv1 = new TextView(this);
        tv1.setText(value1);
        tv1.setTextSize(18);
        tv1.setPadding(8, 8, 8, 8);
        tv1.setGravity(Gravity.END);
        tv1.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        TextView tv2 = new TextView(this);
        tv2.setText(value2);
        tv2.setTextSize(18);
        tv2.setPadding(8, 8, 8, 8);
        tv2.setGravity(Gravity.END);
        tv2.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);

        row.addView(tvLabel);
        row.addView(tv1);
        row.addView(tv2);
        table.addView(row);
    }
}