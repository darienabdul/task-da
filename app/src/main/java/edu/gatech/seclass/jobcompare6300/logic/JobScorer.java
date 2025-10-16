package edu.gatech.seclass.jobcompare6300.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.gatech.seclass.jobcompare6300.models.ComparisonSettings;
import edu.gatech.seclass.jobcompare6300.models.Job;

import static edu.gatech.seclass.jobcompare6300.models.ComparisonSettings.WeightKey;

/// Computes adjusted values and the weighted Job Score,and provides utilities to rank jobs.
public final class JobScorer {

    private static final Map<Job, RankedJob> cachedJobs = new HashMap<>();

    private JobScorer() {}

    /** Adjusted Yearly Salary (AYS) */
    public static double calculateAYS(Job job) {
        Objects.requireNonNull(job, "job is null");     ///?
        return job.getAdjustedSalary();
    }

    /** Adjusted Yearly Bonus (AYB)*/
    public static double calculateAYB(Job job) {
        Objects.requireNonNull(job, "job is null");
        return job.getAdjustedBonus();
    }

    ///Job Score is calculated
    public static double calculateScore(Job job, ComparisonSettings settings) {
        Objects.requireNonNull(job, "job is null");
        Objects.requireNonNull(settings, "settings is null");

        final double adjYearlySalary = calculateAYS(job);
        final double adjYearlyBonus = calculateAYB(job);
        final double calculated401k = adjYearlySalary * (job.match401K / 100.0);
        final double internetStipend = job.internetStipend;
        final double accidentInsurance = job.accidentInsurance;
        final double tuitionReimbursement = job.tuitionReimbursement;

        // raw weights
        int weightS   = settings.getKey(WeightKey.SALARY);
        int weightB   = settings.getKey(WeightKey.BONUS);
        int weight401 = settings.getKey(WeightKey.K401K);
        int weightIS  = settings.getKey(WeightKey.INTERNET);
        int weightAI  = settings.getKey(WeightKey.ACCIDENT);
        int weightTR  = settings.getKey(WeightKey.TUITION);

        int totalWeight = settings.totalWeight();

        double numerator =
                        weightS   * adjYearlySalary +
                        weightB   * adjYearlyBonus +
                        weight401 * calculated401k +
                        weightIS  * internetStipend +
                        weightAI  * accidentInsurance +
                        weightTR  * tuitionReimbursement;

        return numerator / totalWeight;
    }

    /// combine a job with its computed score.
    public static final class RankedJob {
        public final Job job;
        public final double score;
        public RankedJob(Job job, double score) {
            this.job = job;
            this.score = score;
        }
    }

    public static List<RankedJob> rankJobs(List<Job> jobs, ComparisonSettings settings) {
        Objects.requireNonNull(jobs, "jobs is null");
        Objects.requireNonNull(settings, "settings is null");

        List<RankedJob> ranked = new ArrayList<>(jobs.size());
        for (Job job : jobs) {
            if (job != null) {
                ranked.add(getOrRankJob(job, settings));
            }
        }
        /// sort jobs ranked from best to worst
        ranked.sort(Comparator.comparingDouble((RankedJob r) -> r.score).reversed());
        /// return list of ranked jobs
        List<RankedJob> sortedJob = new ArrayList<>(ranked.size());
        for (RankedJob r : ranked) {
            sortedJob.add(r);
        }
        return sortedJob;
    }

    public static RankedJob getOrRankJob(Job job, ComparisonSettings settings) {
        Objects.requireNonNull(job, "job is null");
        Objects.requireNonNull(settings, "settings is null");

        if (cachedJobs.containsKey(job)) {
            return cachedJobs.get(job);
        }

        double score = calculateScore(job, settings);
        RankedJob rankedJob = new RankedJob(job, score);
        cachedJobs.put(job, rankedJob);
        return rankedJob;
    }

    public static void clearCache() {
        cachedJobs.clear();
    }

}
