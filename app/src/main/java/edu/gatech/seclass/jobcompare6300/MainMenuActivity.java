package edu.gatech.seclass.jobcompare6300;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.gatech.seclass.jobcompare6300.manager.JobManager;
import edu.gatech.seclass.jobcompare6300.ui.*;

public class MainMenuActivity extends AppCompatActivity {

    private Button btnCompareJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JobManager jm = JobManager.initialize(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnEditCurrentJob = findViewById(R.id.btnEditCurrentJob);
        Button btnEnterJobOffer = findViewById(R.id.btnEnterJobOffer);
        Button btnAdjustSettings = findViewById(R.id.btnAdjustSettings);
        btnCompareJobs = findViewById(R.id.btnCompareJobs);
        btnCompareJobs.setEnabled(jm.enableComparison());

        btnEditCurrentJob.setOnClickListener(v -> {
            Intent intent = new Intent(this, JobEntryActivity.class);
            // prop to determine if we're editing a current job or adding a new job offer
            intent.putExtra("isCurrentJob", true);
            startActivity(intent);
        });

        btnEnterJobOffer.setOnClickListener(v -> {
            Intent intent = new Intent(this, JobEntryActivity.class);
            // prop to determine if we're editing a current job or adding a new job offer
            intent.putExtra("isCurrentJob", false);
            startActivity(intent);
        });

        btnAdjustSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });

        btnCompareJobs.setOnClickListener(v ->
                startActivity(new Intent(this, JobRankActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCompareJobs.setEnabled(JobManager.getInstance().enableComparison());
    }
}