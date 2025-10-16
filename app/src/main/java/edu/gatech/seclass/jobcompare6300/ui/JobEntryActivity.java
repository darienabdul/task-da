package edu.gatech.seclass.jobcompare6300.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.logic.JobScorer;
import edu.gatech.seclass.jobcompare6300.logic.JobScorer.RankedJob;
import edu.gatech.seclass.jobcompare6300.manager.JobManager;
import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.models.Job;
import edu.gatech.seclass.jobcompare6300.MainMenuActivity;
import edu.gatech.seclass.jobcompare6300.models.Location;

public class JobEntryActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "JobPrefs";
    private static final String KEY_JOB_1 = "Job_1";
    private static final String KEY_JOB_2 = "Job_2";
    private boolean isCurrentJob;

    private SharedPreferences prefs;
    private Gson gson;

    TextInputEditText titleField, companyField, cityField, stateField, costOfLivingField,
            salaryField, bonusField, internetField, matchField,
            accidentField, tuitionField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_job_entry);
        isCurrentJob = getIntent().getBooleanExtra("isCurrentJob", false);

        Button backButton = findViewById(R.id.button_back);
        Button saveButton = findViewById(R.id.button_save);
        Button addAnotherButton = findViewById(R.id.button_add_another);
        Button compareButton = findViewById(R.id.button_compare_offer);

        // all input fields
        titleField = findViewById(R.id.input_title);
        companyField = findViewById(R.id.input_company);
        cityField = findViewById(R.id.input_city);
        stateField = findViewById(R.id.input_state);
        costOfLivingField = findViewById(R.id.input_col);
        salaryField = findViewById(R.id.input_salary);
        bonusField = findViewById(R.id.input_bonus);
        matchField = findViewById(R.id.input_401k);
        internetField = findViewById(R.id.input_internet);
        accidentField = findViewById(R.id.input_accident);
        tuitionField = findViewById(R.id.input_tuition);

        Job editingJob;
        if(isCurrentJob){
            backButton.setText(R.string.cancel_exit);
            addAnotherButton.setVisibility(Button.GONE);
            compareButton.setVisibility(Button.GONE);

            editingJob = JobManager.getInstance().getCurrentJob();
            if (editingJob == null){
                editingJob = new Job();
            }
        } else {
            backButton.setText(R.string.return_to_main_menu);
            addAnotherButton.setVisibility(Button.VISIBLE);
            compareButton.setVisibility(Button.VISIBLE);
            editingJob = new Job();
            compareButton.setEnabled(JobManager.getInstance().hasCurrentJob());

            addAnotherButton.setOnClickListener(v -> {
                if (validateInputs()) {
                    Job job = createJobFromInputs();
                    JobManager.getInstance().addOffer(job);
                    Toast.makeText(this, "Job saved successfully!", Toast.LENGTH_SHORT).show();
                    clearInputs();
                }else {
                    Toast.makeText(this, "Please fill in all fields with valid values.", Toast.LENGTH_LONG).show();
                }
            });

            compareButton.setOnClickListener(v -> {
                if(validateInputs()){
                    Job job = createJobFromInputs();
                    JobManager.getInstance().addOffer(job);

                    ComparisonSettings cm = ComparisonSettings.loadCompSettings(this);
                    RankedJob rankedJob1 = JobScorer.getOrRankJob(JobManager.getInstance().getCurrentJob(), cm);
                    RankedJob rankedJob2 = JobScorer.getOrRankJob(job, cm);
                    String json1 = gson.toJson(rankedJob1);
                    String json2 = gson.toJson(rankedJob2);
                    prefs.edit()
                            .putString(KEY_JOB_1, json1)
                            .putString(KEY_JOB_2, json2)
                            .apply();

                    Toast.makeText(this, "Job saved successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, JobCompareActivity.class));
                    finish();
                } else{
                    Toast.makeText(this, "Please fill in all fields with valid values.", Toast.LENGTH_LONG).show();
                }
            });
        }

        // populate fields using previously saved data if any
        populateFields(editingJob);

        // back button
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        });

        // save button
        saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                Job job = createJobFromInputs();

                if (isCurrentJob) {
                    JobManager.getInstance().saveCurrentJob(job);
                } else {
                    JobManager.getInstance().addOffer(job);
                }
                Toast.makeText(this, "Job saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please fill in all fields with valid values.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        TextInputEditText[] stringFields = new TextInputEditText[] {titleField, companyField, cityField, stateField};
        for (TextInputEditText field: stringFields) {
            if (!validateStringField(field)) isValid = false;
        }

        if (!validateIntField(costOfLivingField,  Integer.MAX_VALUE, "")) isValid = false;
        if (!validateIntField(salaryField,  -1,"")) isValid = false;
        if (!validateIntField(bonusField,  -1,"")) isValid = false;
        if (!validateIntField(matchField,  6, "Value must be between 0 and 6 inclusive")) isValid = false;
        if (!validateIntField(internetField, 360, "Value must be between 0 and 360 inclusive")) isValid = false;
        if (!validateIntField(accidentField,   50000, "Value must be between 0 and 50000 inclusive")) isValid = false;
        if (!validateIntField(tuitionField,   12000, "Value must be between 0 and 12000 inclusive")) isValid = false;

        return isValid;
    }

    private boolean validateStringField(TextInputEditText field){
        String input = field.getText() != null ? field.getText().toString().trim() : "";
        if (input.isEmpty()){
            field.setError("Field is required");
            return false;
        }
        return true;
    }

    private boolean validateIntField(TextInputEditText field, int maxAllowed, String message){
        String input = field.getText() != null ? field.getText().toString().trim() : "";
        if (input.isEmpty()) {
            field.setError("Field is required");
            return false;
        }
        long value = Long.parseLong(input);
        if (value < 0){
            field.setError(message);
            return false;
        }
        if (maxAllowed != -1 && value >maxAllowed){
            field.setError(message);
            return false;
        }
        field.setError(null);
        return true;
    }

    private Job createJobFromInputs() {
        Job job = new Job();
        job.title = titleField.getText().toString();
        job.company = companyField.getText().toString();
        job.location = new Location(
                cityField.getText().toString(),
                stateField.getText().toString(),
                Integer.parseInt(costOfLivingField.getText().toString())
        );
        job.salary = Long.parseLong(salaryField.getText().toString());
        job.bonus = Long.parseLong(bonusField.getText().toString());
        job.match401K = Integer.parseInt(matchField.getText().toString());
        job.internetStipend = Integer.parseInt(internetField.getText().toString());
        job.accidentInsurance = Integer.parseInt(accidentField.getText().toString());
        job.tuitionReimbursement = Integer.parseInt(tuitionField.getText().toString());
        job.isCurrentJob = this.isCurrentJob;
        return job;
    }

    private void populateFields(Job job) {
        if (job == null) return;

        titleField.setText(job.title);
        companyField.setText(job.company);
        if (job.location != null) {
            cityField.setText(job.location.city);
            stateField.setText(job.location.state);
            costOfLivingField.setText(String.valueOf(job.location.costOfLivingIndex));
        }
        salaryField.setText(String.valueOf(job.salary));
        bonusField.setText(String.valueOf(job.bonus));
        matchField.setText(String.valueOf(job.match401K));
        internetField.setText(String.valueOf(job.internetStipend));
        accidentField.setText(String.valueOf(job.accidentInsurance));
        tuitionField.setText(String.valueOf(job.tuitionReimbursement));
    }

    private void clearInputs() {
        titleField.setText("");
        companyField.setText("");
        cityField.setText("");
        stateField.setText("");
        costOfLivingField.setText("");
        salaryField.setText("");
        bonusField.setText("");
        matchField.setText("");
        internetField.setText("");
        accidentField.setText("");
        tuitionField.setText("");
    }

}
