package edu.gatech.seclass.jobcompare6300.uiTests;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;


import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.helpers.HelperMethods;
import edu.gatech.seclass.jobcompare6300.manager.JobManager;
import edu.gatech.seclass.jobcompare6300.ui.JobEntryActivity;


public class JobEntryActivityTest extends HelperMethods {

    private JobManager jobManager;
    private Context context;
    private final Intent intent = new Intent(ApplicationProvider.getApplicationContext(), JobEntryActivity.class);

    /*
    //only needed if we were looking at one page with a single intent
    @Rule
    ActivityScenarioRule<JobEntryActivity> activityRule =
            new ActivityScenarioRule<>(
                    new Intent(ApplicationProvider.getApplicationContext(),
                            JobEntryActivity.class)
                            .putExtra("isCurrentJob", true));
    */

    @Before
    public void setup(){
        context = ApplicationProvider.getApplicationContext();
        jobManager = JobManager.initialize(context);
    }


    @Test
    // Test inputting info into currentJob form and save correctly, reopen and validating that info was saved
    public void enterCurrentJob_entersAndSavesCorrectly(){
        // define activity for this test with intent for currentJob vs jobOffer
        intent.putExtra("isCurrentJob", true);

        ActivityScenario.launch(intent);

        //input all fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());
        goToId(R.id.input_salary).perform(replaceText("100000"), closeSoftKeyboard());
        goToId(R.id.input_bonus).perform(replaceText("5000"), closeSoftKeyboard());
        goToId(R.id.input_401k).perform(replaceText("5"), closeSoftKeyboard());
        goToId(R.id.input_internet).perform(replaceText("200"), closeSoftKeyboard());
        goToId(R.id.input_accident).perform(replaceText("20000"), closeSoftKeyboard());
        goToId(R.id.input_tuition).perform(replaceText("10000"), closeSoftKeyboard());

        // perform save
        //onView(withId(R.id.button_save)).perform(click());
        goToId(R.id.button_save);


        // return to currentJob Form
        ActivityScenario.launch(intent);

        //verify fields
        goToId(R.id.input_title).check(checkText("Software Engineer"));
        goToId(R.id.input_company).check(checkText("Paramount+"));
        goToId(R.id.input_city).check(checkText("San Francisco"));
        goToId(R.id.input_state).check(checkText("California"));
        goToId(R.id.input_col).check(checkText("204"));
        goToId(R.id.input_salary).check(checkText("100000"));
        goToId(R.id.input_bonus).check(checkText("5000"));
        goToId(R.id.input_401k).check(checkText("5"));
        goToId(R.id.input_internet).check(checkText("200"));
        goToId(R.id.input_accident).check(checkText("20000"));
        goToId(R.id.input_tuition).check(checkText("10000"));
    }

    @Test
    public void editCurrentJob(){
        // TO:DO WRITE CODE
        // fill prefs file before
    }

    @Test
    // Test inputting info into jobOffer form and save
    public void enterJobOffer_entersAndSavesCorrectly(){
        // define activity for this test with the intent for currentJob vs jobOffer
        intent.putExtra("isCurrentJob", false);

        ActivityScenario.launch(intent);

        goToId(R.id.input_title).perform(typeText("QA Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Netflix"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("Miami"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("Florida"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("193"), closeSoftKeyboard());
        goToId(R.id.input_salary).perform(replaceText("200000"), closeSoftKeyboard());
        goToId(R.id.input_bonus).perform(replaceText("0"), closeSoftKeyboard());
        goToId(R.id.input_401k).perform(replaceText("2"), closeSoftKeyboard());
        goToId(R.id.input_internet).perform(replaceText("0"), closeSoftKeyboard());
        goToId(R.id.input_accident).perform(replaceText("10000"), closeSoftKeyboard());
        goToId(R.id.input_tuition).perform(replaceText("12000"), closeSoftKeyboard());

        // perform save
        //onView(withId(R.id.button_save)).perform(click());
        goToId(R.id.button_save);
    }

    @Test
    // validate thrown error for invalid text inputs (empty text)
    public void validateTextErrors(){
        ActivityScenario.launch(intent);

        goToId(R.id.button_save);

        onView(withId(R.id.input_title)).check(matches(hasErrorText("Field is required")));
        onView(withId(R.id.input_company)).check(matches(hasErrorText("Field is required")));
        onView(withId(R.id.input_city)).check(matches(hasErrorText("Field is required")));
        onView(withId(R.id.input_state)).check(matches(hasErrorText("Field is required")));
        onView(withId(R.id.input_col)).check(matches(hasErrorText("Field is required")));
    }

    @Test
    // validate thrown error for invalid 401k input
    public void validate401k(){
        ActivityScenario.launch(intent);

        goToId(R.id.input_401k).perform(typeText("10"), closeSoftKeyboard());

        goToId(R.id.button_save);

        onView(withId(R.id.input_401k)).check(matches(hasErrorText("Value must be between 0 and 6 inclusive")));
    }

    @Test
    //crashes app
    public void validateSalary(){
        ActivityScenario.launch(intent);

        //these fields need values before we can save and really test the other fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());

        goToId(R.id.input_salary).perform(replaceText("2147483648"), closeSoftKeyboard());
        goToId(R.id.button_save);

    }

    @Test
    //crashes app
    public void validateBonus(){
        ActivityScenario.launch(intent);

        //these fields need values before we can save and really test the other fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());

        goToId(R.id.input_bonus).perform(replaceText("2147483648"), closeSoftKeyboard());
        goToId(R.id.button_save);

    }

    @Test
    // validate thrown error for invalid internet input
    public void validateInternetField(){
        ActivityScenario.launch(intent);

        //these fields need values before we can save and really test the other fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());

        goToId(R.id.input_internet).perform(replaceText("420"), closeSoftKeyboard());
        goToId(R.id.button_save);

        onView(withId(R.id.input_internet)).check(matches(hasErrorText("Value must be between 0 and 360 inclusive")));

    }

    @Test
    public void validateAccidentField(){
        ActivityScenario.launch(intent);

        //these fields need values before we can save and really test the other fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());

        goToId(R.id.input_accident).perform(replaceText("-100"), closeSoftKeyboard());
        goToId(R.id.button_save);

        onView(withId(R.id.input_accident)).check(matches(hasErrorText("Value must be between 0 and 50000 inclusive")));
    }

    @Test
    // validate thrown error for invalid tuition input
    public void validateTuitionField(){
        ActivityScenario.launch(intent);

        //these fields need values before we can save and really test the other fields
        goToId(R.id.input_title).perform(typeText("Software Engineer"), closeSoftKeyboard());
        goToId(R.id.input_company).perform(typeText("Paramount+"), closeSoftKeyboard());
        goToId(R.id.input_city).perform(typeText("San Francisco"), closeSoftKeyboard());
        goToId(R.id.input_state).perform(typeText("California"), closeSoftKeyboard());
        goToId(R.id.input_col).perform(typeText("204"), closeSoftKeyboard());

        goToId(R.id.input_tuition).perform(replaceText("50000"), closeSoftKeyboard());
        goToId(R.id.button_save);

        onView(withId(R.id.input_tuition)).check(matches(hasErrorText("Value must be between 0 and 12000 inclusive")));
    }

    @Test
    // validate that user can enter another offer by clicking the "add another offer" button
    public void validateAddAnotherOffer(){
        //  TO:DO WRITE CODE
    }

    @Test
    // validate that user can compare their newly entered job with their current job
    public void validateCompareWithCurrentJob(){
        // TO:DO WRITE CODE
    }

    @Test
    // validate cancel jobEntry
    public void validateCancelJobEntry(){
        // TO:DO WRITE CODE
    }

}

