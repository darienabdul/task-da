package edu.gatech.seclass.jobcompare6300.uiTests;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.seclass.jobcompare6300.MainMenuActivity;
import edu.gatech.seclass.jobcompare6300.R;
import edu.gatech.seclass.jobcompare6300.helpers.HelperMethods;
import edu.gatech.seclass.jobcompare6300.manager.JobManager;
import edu.gatech.seclass.jobcompare6300.models.Job;

public class MainMenuActivityTest extends HelperMethods {
    private JobManager jobManager;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        jobManager = JobManager.initialize(context);
        jobManager.clearAllJobsData();
        ActivityScenario.launch(MainMenuActivity.class);
    }

    @Test
    // verify that the app loads correctly with all 4 functions
    public void verifyMainMenuLoad() {
        navigate(R.id.btnEditCurrentJob).check(matches(isDisplayed()));
        navigate(R.id.btnEnterJobOffer).check(matches(isDisplayed()));
        navigate(R.id.btnAdjustSettings).check(matches(isDisplayed()));
        navigate(R.id.btnCompareJobs).check(matches(isDisplayed()));
    }

    @Test
    // verify enter/edit currentJob redirect
    public void verifyCurrentJobBtn() {
        goToId(R.id.btnEditCurrentJob);
        navigate(R.id.input_title_layout).check(matches(isDisplayed()));
    }

    @Test
    // verify enter jobOffer redirect
    public void verifyJobOfferBtn() {
        goToId(R.id.btnEnterJobOffer);
        navigate(R.id.input_title_layout).check(matches(isDisplayed()));
    }

    @Test
    // verify compare button is disabled/enabled based on number of jobs
    public void verifyCompareButtonState() {
        // No jobs, button should be disabled
        assertFalse(jobManager.enableComparison());

        // 1 job, button should be disabled
        jobManager.addOffer(new Job());
        assertFalse(jobManager.enableComparison());

        // 2 jobs, button should be enabled
        jobManager.addOffer(new Job());
        assertTrue(jobManager.enableComparison());
    }
}