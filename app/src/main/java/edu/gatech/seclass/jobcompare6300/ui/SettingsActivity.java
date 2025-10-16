package edu.gatech.seclass.jobcompare6300.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.gatech.seclass.jobcompare6300.MainMenuActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings;
import static edu.gatech.seclass.jobcompare6300.models.ComparisonSettings.WeightKey;

public class SettingsActivity extends AppCompatActivity {

    // numerical weights for comparison settings
    private static final Integer[] weights = {0,1,2,3,4,5,6,7,8,9};

    // General helper method to setup dropdowns for comp settings
    private static void setupWeightDropdown(AutoCompleteTextView dropdown, Context context){
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, weights);
        dropdown.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // boilerplate
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // load comparison settings
        ComparisonSettings compSettings = ComparisonSettings.loadCompSettings(this);

        // dropdowns as AutoCompleteTextView
        AutoCompleteTextView salaryWeightDropdown = findViewById(R.id.salaryWeight);
        AutoCompleteTextView bonusWeightDropdown = findViewById(R.id.bonusWeight);
        AutoCompleteTextView matchWeightDropdown = findViewById(R.id.matchWeight);
        AutoCompleteTextView internetWeightDropdown = findViewById(R.id.internetWeight);
        AutoCompleteTextView accidentWeightDropdown = findViewById(R.id.accidentWeight);
        AutoCompleteTextView tuitionWeightDropdown = findViewById(R.id.tuitionWeight);


        // Set dropdowns to their stored values
        salaryWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.SALARY)));
        bonusWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.BONUS)));
        matchWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.K401K)));
        internetWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.INTERNET)));
        accidentWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.ACCIDENT)));
        tuitionWeightDropdown.setText(String.valueOf(compSettings.getKey(WeightKey.TUITION)));


        // give dropdowns their array values
        setupWeightDropdown(bonusWeightDropdown, this);
        setupWeightDropdown(salaryWeightDropdown, this);
        setupWeightDropdown(matchWeightDropdown, this);
        setupWeightDropdown(internetWeightDropdown, this);
        setupWeightDropdown(accidentWeightDropdown, this);
        setupWeightDropdown(tuitionWeightDropdown, this);

        // How to setup dropdowns without helper method
        // ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weights);
        // salaryWeightDropdown.setAdapter(adapter);

        Button backButton = findViewById(R.id.button_back);
        Button saveButton = findViewById(R.id.button_save);

        // back button
        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        });

        // save button
        saveButton.setOnClickListener(v -> {

            // updating the class variables with the dropdown variables
            compSettings.setKey(WeightKey.SALARY, Integer.parseInt(salaryWeightDropdown.getText().toString()));
            compSettings.setKey(WeightKey.BONUS, Integer.parseInt(bonusWeightDropdown.getText().toString()));
            compSettings.setKey(WeightKey.K401K, Integer.parseInt(matchWeightDropdown.getText().toString()));
            compSettings.setKey(WeightKey.INTERNET, Integer.parseInt(internetWeightDropdown.getText().toString()));
            compSettings.setKey(WeightKey.ACCIDENT, Integer.parseInt(accidentWeightDropdown.getText().toString()));
            compSettings.setKey(WeightKey.TUITION, Integer.parseInt(tuitionWeightDropdown.getText().toString()));

            // commiting these changes to the prefs file
            compSettings.saveCompSettings(this);

            // can't divide by 0 on the formula, so if total is 0, show toast error.
            if(compSettings.totalWeight() == 0){
                Toast.makeText(this, "All values can't be 0. Please update again.", Toast.LENGTH_LONG).show();
            } else {
                //success message
                Toast.makeText(this, "Comparison settings saved successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
            }
        });


    }
}