package edu.gatech.seclass.jobcompare6300.manager;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.seclass.jobcompare6300.models.Job;
import edu.gatech.seclass.jobcompare6300.models.Location;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.List;

public class JobManagerTest {

    private JobManager jobManager;
    private final Location location = new Location("San Francisco", "California", 204);
    private final Job job = new Job("a","b", location,100000, 10000, 5, 500, 300, 1000);

    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {

        // reset singleton before each test and initialize again at the end of setup
        java.lang.reflect.Field instanceField = JobManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        Context mockContext = mock(Context.class);
        Context mockAppContext = mock(Context.class);
        SharedPreferences mockPrefs = mock(SharedPreferences.class);
        SharedPreferences.Editor mockEditor = mock(SharedPreferences.Editor.class);

        when(mockContext.getApplicationContext()).thenReturn(mockAppContext);
        when(mockAppContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mockPrefs);
        when(mockPrefs.edit()).thenReturn(mockEditor);
        when(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor);

        jobManager = JobManager.initialize(mockContext);

    }

    @Test(expected = IllegalStateException.class)
    public void getInstance_withoutInitialize_throwsException() throws Exception {
        // reset singleton to test get instance before initialization
        java.lang.reflect.Field instanceField = JobManager.class.getDeclaredField("instance");
        instanceField.setAccessible(true);
        instanceField.set(null, null);

        JobManager.getInstance();
    }

    @Test(expected = IllegalArgumentException.class)
    public void saveNullJob_throwsException() {
        jobManager.saveCurrentJob(null);
    }

    @Test
    public void hasCurrentJob_returnsAccurately() {
        assertEquals(false, jobManager.hasCurrentJob());
        jobManager.saveCurrentJob(job);
        assertEquals(true, jobManager.hasCurrentJob());
    }

    @Test
    public void getCurrentJob_returnsSavedJob() {
        jobManager.saveCurrentJob(job);
        assertEquals(job, jobManager.getCurrentJob());
    }

    @Test
    public void addOffer_addsJobToOffers() {
        jobManager.addOffer(job);
        assertEquals(1, jobManager.getOfferCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOffer_addNullJobThrowsException() {
        jobManager.addOffer(null);
    }

    @Test
    public void getOffers_returnsAllJobOffers(){
        jobManager.addOffer(job);
        jobManager.addOffer(job);
        List<Job> jobs = jobManager.getOffers();

        assertEquals(2, jobManager.getOfferCount());
        assertEquals(job, jobs.get(0));
        assertEquals(job,  jobs.get(1));
    }

    @Test
    public void getOfferCount_returnsCorrectSize() {
        int count = (int) (Math.random() * 99) +1;
        for (int i = 0; i < count; i++) {
            jobManager.addOffer(job);
        }
        assertEquals(count, jobManager.getOfferCount());
    }


    @Test
    public void enableComparison_returnsTrueWithCurrentAndOneOffer() {
        jobManager.addOffer(job);
        jobManager.saveCurrentJob(job);
        assertEquals(true, jobManager.enableComparison());
    }

    @Test
    public void enableComparison_returnsTrueWithTwoOffersAndNoCurrentJob() {
        jobManager.addOffer(job);
        jobManager.addOffer(job);
        assertEquals(true, jobManager.enableComparison());
    }

    @Test
    public void enableComparison_returnsFalseWhenOnlyCurrentJobs() {
        jobManager.saveCurrentJob(job);
        assertEquals(false, jobManager.enableComparison());
    }

    @Test
    public void enableComparison_returnsFalseWhenOnlyOneJobOffer() {
        jobManager.addOffer(job);
        assertEquals(false, jobManager.enableComparison());
    }

    @Test
    public void enableComparison_returnsFalseWhenNoCurrentJobAndNoJobOffers() {
        assertEquals(false, jobManager.enableComparison());
    }
}
