package edu.gatech.seclass.jobcompare6300.models;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.gatech.seclass.jobcompare6300.models.Job;
import edu.gatech.seclass.jobcompare6300.models.Location;

public class JobTest {

    private final Location location1 = new Location("San Francisco", "California", 204);
    private Job job;

    @Before
    public void setup(){
        job = new Job("a","b",location1,120000, 5000, 5, 500, 300, 1000);
    }

    @Test
    public void getAdjustSalary_returnsCorrectly(){
        double adjustedSalary = (double) 120000 / ((double) 204 / 100);
        assertEquals(adjustedSalary, job.getAdjustedSalary(), 0.001) ;
    }

    @Test
    public void getBonusSalary_returnsCorrectly(){
        double adjustedBonus = (double) 5000 / ((double) 204 / 100);
        assertEquals(adjustedBonus, job.getAdjustedBonus(), 0.001);
    }
}
