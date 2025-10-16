package edu.gatech.seclass.jobcompare6300.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.models.Job;
import edu.gatech.seclass.jobcompare6300.models.Location;

public class JobScorerTest {

    private final Location location1 = new Location("city", "state", 6);
    private final Location location2 = new Location("city", "state", 5);
    private final Job job1 = new Job("a","b",location1,100000, 10000, 5, 500, 300, 1000);
    private final Job job2 = new Job("a","b",location2,100000,10000, 5, 500, 300, 1000);
    private final ComparisonSettings settings = new ComparisonSettings();

    @Test
    public void calcScore_returnsCorrectly(){
// TODO
    }

    @Test
    public void rankJobs_returnsCorrectOrder(){
        List<JobScorer.RankedJob> ranked = JobScorer.rankJobs(Arrays.asList(job1, job2), settings);

        assertEquals(2, ranked.size());
        assertEquals(job2, ranked.get(0).job);
        assertEquals(job1, ranked.get(1).job);
    }

    @Test
    public void rankJobs_withEqualJobScores(){
        List<JobScorer.RankedJob> ranked = JobScorer.rankJobs(Arrays.asList(job1, job1), settings);
        assertEquals(2, ranked.size());
        assertEquals(ranked.get(0).score, ranked.get(1).score, 0.0001);
        assertEquals(ranked.get(0).job, ranked.get(1).job);
    }

    @Test
    public void rank_withLessThanTwoJobs(){
        List<JobScorer.RankedJob> ranked = JobScorer.rankJobs(Arrays.asList(job1), settings);
        assertEquals(1, ranked.size());
    }

    @Test(expected = NullPointerException.class)
    public void calculateScore_withNullJob_throwsException() {
        ComparisonSettings settings = new ComparisonSettings();
        JobScorer.calculateScore(null, settings);
    }

    @Test(expected = NullPointerException.class)
    public void calculateScore_withNullSettings_throwsException() {
        JobScorer.calculateScore(job1, null);
    }
}
